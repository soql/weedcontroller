package pl.net.oth.weedcontroller.mqtt;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.external.impl.GpioPiExternalController;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.dto.SensorResultDataDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Component
public class SensorMqttClient implements MqttCallback {
	private final static Log LOGGER = LogFactory.getLog(SensorMqttClient.class);
	
	private final static String MQTT_ADDRESS="MQTT_ADDRESS";
	
	private MqttClient myClient;
	private MqttConnectOptions connOpt;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private SensorTask sensorTask;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Inicjalizacja MQTT");
		String clientID = "WEED_CONTROLLER ";
		connOpt = new MqttConnectOptions();
		
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(3);
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

		
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.error("Utracono połączenie z MQTT");		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.debug("Odebrano wiadomość MQTT. Topic: "+topic+" 	"+message);
		Sensor sensor=sensorService.getSensorByMQTTTopic(topic);
		sensorTask.readFromMqtt(sensor, message.toString());
		sensorTask.checkSensorCorrectResult(sensor);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

}
