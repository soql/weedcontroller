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
import pl.net.oth.weedcontroller.service.SwitchService;

@Configuration
@EnableScheduling
public class SensorTask {
	private final static Log LOGGER=LogFactory.getLog(SensorTask.class);
	private static final String ERROR_MESSAGE = "Failed to get reading. Try again!";
	private static final int MAX_ERROR_TEMP=5;
		
	@Autowired
	private SensorExternalController sensorExternalController;
	
	private SensorResultDTO lastSuccesfullSensorResult;
		
	private SensorResultDTO lastSensorResult;
	
	private SensorResultDTO previousSuccessfullSensorResult;
	
	@Scheduled(fixedDelay = 15000)
	public void check() {
		lastSensorResult=sensorExternalController.check();
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
		if(Math.abs(previousSuccessfullSensorResult.getTemperature()-lastSensorResult.getTemperature())>MAX_ERROR_TEMP)
			return true;
		return false;
	}


	public SensorResultDTO getLastSensorResult() {
		return lastSensorResult;
	}


	public SensorResultDTO getLastSuccesfullSensorResult() {
		return lastSuccesfullSensorResult;
	}


	
}
