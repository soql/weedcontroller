package pl.net.oth.weedcontroller.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Configuration
@EnableScheduling
public class SensorTask {
	private final static Log LOGGER=LogFactory.getLog(SensorTask.class);	
		
	@Autowired
	private SensorExternalController sensorExternalController;
	
	@Autowired
	private ConfigurationService configurationService;
	
	public static final String MAX_TEMP_ERROR_KEY="MAX_TEMP_ERROR_KEY";
	
	public static final String MAX_HUMI_ERROR_KEY="MAX_HUMI_ERROR_KEY";
	
	private SensorResultDTO lastSuccesfullSensorResult;
		
	private SensorResultDTO lastSensorResult;
	
	private SensorResultDTO previousSuccessfullSensorResult;
	
	public void readFromExternal(){
		lastSensorResult=sensorExternalController.check();
	}
	
	@Scheduled(fixedDelay = 15000)
	public void check() {
		readFromExternal();
		
		if(lastSensorResult!=null){			
			if(!checkErrors(previousSuccessfullSensorResult, lastSensorResult)){
				lastSuccesfullSensorResult=lastSensorResult;
				previousSuccessfullSensorResult=lastSensorResult;
			}else{
				LOGGER.debug("Odczyt "+lastSensorResult.getTemperature()+" "+lastSensorResult.getHumidity()+" uznany za nieprawidłowy !!");
				previousSuccessfullSensorResult=lastSensorResult;
			}
		}
	}


	private boolean checkErrors(SensorResultDTO previousSuccessfullSensorResult, SensorResultDTO lastSensorResult) {
		if(previousSuccessfullSensorResult==null){
			return false;
		}
		if(!configurationService.isConfigurationExist(MAX_TEMP_ERROR_KEY, MAX_HUMI_ERROR_KEY))
			return false;
		int maxErrorTemp=Integer.parseInt(configurationService.getByKey(MAX_TEMP_ERROR_KEY).getValue());
		if(Math.abs(previousSuccessfullSensorResult.getTemperature()-lastSensorResult.getTemperature())>maxErrorTemp)
			return true;
		int maxErrorHumi=Integer.parseInt(configurationService.getByKey(MAX_HUMI_ERROR_KEY).getValue());
		if(Math.abs(previousSuccessfullSensorResult.getHumidity()-lastSensorResult.getHumidity())>maxErrorHumi)
			return true;
		return false;
	}


	public SensorResultDTO getLastSensorResult() {
		return lastSensorResult;
	}


	public SensorResultDTO getLastSuccesfullSensorResult() {
		return lastSuccesfullSensorResult;
	}


	public void setLastSensorResult(SensorResultDTO lastSensorResult) {
		this.lastSensorResult = lastSensorResult;
	}
	


	
}
