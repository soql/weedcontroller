package pl.net.oth.weedcontroller.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Configuration
@EnableScheduling
public class SensorTask {
	private final static Log LOGGER=LogFactory.getLog(SensorTask.class);	
		
	@Autowired
	private SensorExternalController sensorExternalController;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private SensorService sensorService;
	
	public static final String MAX_TEMP_ERROR_KEY="MAX_TEMP_ERROR_KEY";
	
	public static final String MAX_HUMI_ERROR_KEY="MAX_HUMI_ERROR_KEY";
	
	private Map<Integer, SensorResultDTO> lastSuccesfullSensorResult=new HashMap<>();
		
	private Map<Integer,SensorResultDTO> lastSensorResult=new HashMap<>();
	
	private Map<Integer,SensorResultDTO> previousSuccessfullSensorResult=new HashMap<>();
	
	public void readFromExternal(Integer number, String command){
		lastSensorResult.put(number,sensorExternalController.check(command));
	}
	
	@Scheduled(fixedDelay = 15000)
	public void check() {
		for(Sensor sensor:sensorService.getAllSensors()){			
			readFromExternal(sensor.getNumber(), sensor.getCommand());
			Integer sensorNumber=sensor.getNumber();
			
			if(lastSensorResult.get(sensorNumber)!=null){			
				if(!checkErrors(previousSuccessfullSensorResult.get(sensorNumber), lastSensorResult.get(sensorNumber))){
					lastSuccesfullSensorResult.put(sensorNumber, lastSensorResult.get(sensorNumber));
					previousSuccessfullSensorResult.put(sensorNumber, lastSensorResult.get(sensorNumber));
				}else{
					LOGGER.debug("Odczyt "+lastSensorResult.get(sensorNumber).getTemperature()+" "+lastSensorResult.get(sensorNumber).getHumidity()+" uznany za nieprawidłowy !!");
					previousSuccessfullSensorResult.put(sensor.getNumber(), lastSensorResult.get(sensor.getNumber()));
				}
			}
		}
	}


	private boolean checkErrors(SensorResultDTO previousSuccessfullSensorResult, SensorResultDTO lastSensorResult) {
		if(previousSuccessfullSensorResult==null){
			return false;
		}
		if(!configurationService.isConfigurationExist(MAX_TEMP_ERROR_KEY, MAX_HUMI_ERROR_KEY))
			return false;
		float maxErrorTemp=Float.parseFloat(configurationService.getByKey(MAX_TEMP_ERROR_KEY).getValue());
		if(Math.abs(previousSuccessfullSensorResult.getTemperature()-lastSensorResult.getTemperature())>maxErrorTemp)
			return true;
		float maxErrorHumi=Float.parseFloat(configurationService.getByKey(MAX_HUMI_ERROR_KEY).getValue());
		if(Math.abs(previousSuccessfullSensorResult.getHumidity()-lastSensorResult.getHumidity())>maxErrorHumi)
			return true;
		return false;
	}

	public Map<Integer, SensorResultDTO> getLastSuccesfullSensorResult() {
		return lastSuccesfullSensorResult;
	}	
	
}
