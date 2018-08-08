package pl.net.oth.weedcontroller.mqtt;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.external.ExternalSwitchDispatcher;
import pl.net.oth.weedcontroller.external.impl.GpioPiExternalController;
import pl.net.oth.weedcontroller.external.impl.SOnOffExternalController;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.dto.SensorResultDataDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Component
public class SensorMqttClient implements MqttCallback {
	private final static Log LOGGER = LogFactory.getLog(SensorMqttClient.class);
	
	private final static String MQTT_ADDRESS="MQTT_ADDRESS";
	
	private boolean mqttStopped=false;
	
	private MqttClient myClient;
	private MqttConnectOptions connOpt;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private SensorTask sensorTask;
	
	@Autowired
	private SwitchService switchService;
	
	@Autowired
	private SOnOffExternalController sonoffExternalController;
	
	@Autowired
	private ExternalSwitchDispatcher externalSwitchDispatcher;
	
	@PostConstruct
	public void init() {
		if(mqttStopped)
			return;
		LOGGER.info("Inicjalizacja MQTT");
		String clientID = "WEED_CONTROLLER "+UUID.randomUUID();
		connOpt = new MqttConnectOptions();
		
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(300);
		connOpt.setMqttVersion(3);
		
		try {
			String mqttAddress=configurationService.getByKey(MQTT_ADDRESS).getValue();
			myClient = new MqttClient(mqttAddress, clientID);
			myClient.setCallback(this);						
			myClient.connect(connOpt);	
			subscripeTopics();			
		}catch(Exception e) {
			LOGGER.error(Helper.STACK_TRACE, e);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			init();
		}
	}

	@PreDestroy
	private void predestroy() {
		try {
			LOGGER.info("Zatrzymuję server MQTT");
			mqttStopped=true;
			myClient.disconnect();
			myClient.close();
		} catch (MqttException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
	}
	
	private void subscripeTopics() {
		List<Sensor> mqttSensors=sensorService.getAllMQTTSensors();
		mqttSensors.stream().forEach(sensor -> {
			LOGGER.info("Rejestruję sensor MQTT "+sensor.getMqttTopic());
			try {				
				myClient.subscribe(sensor.getMqttTopic());				
			} catch (MqttException e) {
				LOGGER.error(Helper.STACK_TRACE, e);
			}
		});
		List<SwitchGPIO> sonoffSwitches=switchService.getAllMqttSwitches();
		if(sonoffSwitches!=null) {
			sonoffSwitches.stream().forEach(switch_ -> {
				LOGGER.info("Rejestruję przełącznik MQTT "+switch_.getMqttTopic()+"state");
				try {				
					myClient.subscribe(switch_.getMqttTopic()+"state");				
				} catch (MqttException e) {
					LOGGER.error(Helper.STACK_TRACE, e);
				}		
			});
		}
	}
	public boolean publish(String myTopic, String messageTxt) {
		LOGGER.info("Wysyłka MQTT. Kolejka: "+myTopic+". Wiadomość: "+messageTxt);		
		MqttMessage message = new MqttMessage(messageTxt.getBytes());
    	message.setQos(0);
    	message.setRetained(false);    	
    	try {
    		// publish message to broker
			myClient.publish(myTopic, message);	    			
			return true;
		} catch (Exception e) {
			LOGGER.error(Helper.STACK_TRACE, e);
			return false;
		}		
	}

	@Override
	public void connectionLost(Throwable cause) {
		if(mqttStopped)
			return;
		LOGGER.error("Utracono połączenie z MQTT");		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		init();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.debug("Odebrano wiadomość MQTT. Kolejka: "+topic+" 	"+message);		
		Sensor sensor=sensorService.getSensorByMQTTTopic(topic);
		if(sensor!=null) {
			LOGGER.debug("Przekazuję wiadomość MQTT. Kolejka: "+topic+" 	"+message+" do sensora "+sensor.getName());
			sensorTask.readFromMqtt(sensor, message.toString());
			sensorTask.checkSensorCorrectResult(sensor);
			return;
		}
		SwitchGPIO switchGPIO=switchService.getSwitchByMQTTTopic(topic.replaceAll("state", ""));
		if(switchGPIO!=null) {
			LOGGER.debug("Przekazuję wiadomość MQTT. Kolejka: "+topic+" 	"+message+" do przekaźnika "+switchGPIO.getDescription());
			sonoffExternalController.putToMap(topic, message.toString());
			return;
		}
		

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			LOGGER.info("Wiadomość MQTT dostarczona. Kolejka: "+token.getTopics()[0]+" Wiadomość: "+token.getMessage().toString());
		} catch (MqttException e) {
			LOGGER.error("Utracono połączenie z MQTT");
		}

	}

}
