package pl.net.oth.weedcontroller.mqtt;

import java.util.List;

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
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorService;

@Component
public class SensorMqttClient implements MqttCallback {
	private final static Log LOGGER = LogFactory.getLog(GpioPiExternalController.class);
	
	private MqttClient myClient;
	private MqttConnectOptions connOpt;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SensorService sensorService;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Inicjalizacja MQTT");
		String clientID = "WEED_CONTROLLER";
		connOpt = new MqttConnectOptions();
		
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		
		try {
			myClient = new MqttClient("tcp://zjc.oth.net.pl:1883", clientID);
			myClient.setCallback(this);
			myClient.connect(connOpt);	
			subscripeTopics();
			
		}catch(Exception e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
	}

	private void subscripeTopics() {
		List<Sensor> mqttSensors=sensorService.getAllMQTTSensors();
		mqttSensors.stream().forEach(sensor -> {
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

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.debug("Odebrano wiadomość MQTT. Topic: "+topic+" 	"+message);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

}
