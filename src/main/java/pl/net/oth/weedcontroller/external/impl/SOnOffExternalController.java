package pl.net.oth.weedcontroller.external.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.external.SwitchController;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.mqtt.SensorMqttClient;
import pl.net.oth.weedcontroller.service.SwitchService;

@Component
public class SOnOffExternalController {
	private final static Log LOGGER = LogFactory.getLog(SOnOffExternalController.class);
	
	private Map<Integer, SwitchState> sonoffSwitchesStates;
	

	
	@Autowired
	private SensorMqttClient sensorMqttClient;
	
	@Autowired
	private SwitchService switchService;
	
	@PostConstruct
	private void init() {		
		sonoffSwitchesStates=new HashMap<>();
		
		switchService.getAllMqttSwitches().stream().forEach(switch_ -> {
			sensorMqttClient.publish(switch_.getMqttTopic()+"cmd", "state");
			getState(switch_);
		});
	}
	public void setState(SwitchGPIO switchGPIO, SwitchState state) {		
		if(state.equals(getState(switchGPIO))){
			return;
		}
		sensorMqttClient.publish(switchGPIO.getMqttTopic()+"cmd", state.name().toLowerCase());		
		sonoffSwitchesStates.put(switchGPIO.getId(), state);
	}

	public SwitchState getState(SwitchGPIO switchGPIO) {		
		return sonoffSwitchesStates.get(switchGPIO.getId());
	}	
	public void putToMap(String topic, String state) {
		LOGGER.debug("Odebrano wiadomość "+state+" na sonoff o kolejce: "+topic);
		SwitchGPIO switchGPIO=switchService.getSwitchByMQTTTopic(topic.replaceAll("state", ""));
		if(switchGPIO!=null) {
			SwitchState switchState=state.equals("on")?SwitchState.ON:SwitchState.OFF;
			LOGGER.debug("Zapisuję stan SONOFF o ID "+switchGPIO.getId()+" na "+switchState);
			sonoffSwitchesStates.put(switchGPIO.getId(), switchState);
		}
	}

}
