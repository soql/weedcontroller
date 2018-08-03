package pl.net.oth.weedcontroller.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import groovy.lang.GroovyShell;
import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorData;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SensorResultDataDTO;
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
		
	private Map<Integer, SensorResultDTO> lastSuccesfullSensorResult=new HashMap<>();
		
	private Map<Integer,SensorResultDTO> lastSensorResult=new HashMap<>();
	
	private Map<Integer,SensorResultDTO> previousSuccessfullSensorResult=new HashMap<>();
	
	/*TODO refactor do jednej*/
	public void readFromExternal(Sensor sensor){
		long timeBefore=new Date().getTime();
		String result=sensorExternalController.check(sensor.getCommand());
		long timeAfter=new Date().getTime();
		if(result==null)
			return;
		LOGGER.debug("Odczyt z sensora "+sensor.getName()+": "+result+" ( czas trwania: "+(timeAfter-timeBefore)/(float)1000+" s. ).");
		SensorResultDTO sensorResultDTO = parseSensorResponse(result, sensor);		
		sensorResultDTO.setLastSuccesfullRead(new Date());
		lastSensorResult.put(sensor.getNumber(), sensorResultDTO);
	}
	
	public void readFromMqtt(Sensor sensor, String mqttMessage){
		long timeBefore=new Date().getTime();
		String result=mqttMessage;
		long timeAfter=new Date().getTime();
		if(result==null)
			return;
		LOGGER.debug("Odczyt z sensora MQTT "+sensor.getName()+": "+result+" ( czas trwania: "+(timeAfter-timeBefore)/(float)1000+" s. ).");
		try {
		SensorResultDTO sensorResultDTO = parseSensorResponse(result, sensor);		
		sensorResultDTO.setLastSuccesfullRead(new Date());
		lastSensorResult.put(sensor.getNumber(), sensorResultDTO);
		}catch(Exception e) {
			LOGGER.error("Błąd przy obsłudze odpowiedzi z MQTT "+sensor.getName());
			LOGGER.error(Helper.STACK_TRACE, e);
		}
	}
	
	private SensorResultDTO parseSensorResponse(String result, Sensor sensor) {
		SensorResultDTO sensorResultDTO = new SensorResultDTO();		
		for(SensorData sensorData : sensor.getSensorDatas()) {			
			Pattern pattern = Pattern.compile(sensorData.getRegexp());			
			Matcher matcher = pattern.matcher(result);
			if(matcher.matches()) {
				LOGGER.debug("PUT:"+sensorData.getName());
				
				Float resultData=Float.parseFloat(matcher.group(1));
				LOGGER.debug("RESULT:"+resultData);
				Float transformedResultData=null;
				if(!StringUtils.isEmpty(sensorData.getTransformExpression())) {
					GroovyShell gs=new GroovyShell();
					gs.setVariable("VALUE", resultData);
					Double resultDataAsDouble=(Double) gs.evaluate(new StringReader(sensorData.getTransformExpression()));
					transformedResultData=resultDataAsDouble.floatValue();
					LOGGER.debug("RESULT_TRANSFORM:"+transformedResultData);
				}
				SensorResultDataDTO sensorResultDataDTO=new SensorResultDataDTO();
				sensorResultDataDTO.setResult(resultData);
				sensorResultDataDTO.setTransformedResult(transformedResultData);
				sensorResultDataDTO.setDescription(sensorData.getDescription());
				sensorResultDataDTO.setCssName(sensorData.getCssName());
				sensorResultDataDTO.setUnit(sensorData.getUnit());
				sensorResultDTO.setSortOrder(sensor.getSortOrder());
				sensorResultDTO.setVisibleOnGui(sensor.getVisibleOnGui());
				
				sensorResultDTO.getResults().put(sensorData.getName(), sensorResultDataDTO);											
			}else {
				LOGGER.error("Nieudane dopasowanie paternu z sensora "+sensor.getName()+" Odpowiedź: "+result+" Patern: "+sensorData.getRegexp());				
			}			
		}	
		return sensorResultDTO;
	}

	@Scheduled(fixedDelay = 2000)
	public void check() {
		for(Sensor sensor:sensorService.getAllCommandSensors()){			
			readFromExternal(sensor);						
			checkSensorCorrectResult(sensor);
		}
	}
	public void checkSensorCorrectResult(Sensor sensor) {
		Integer sensorNumber=sensor.getNumber();
		
		if(lastSensorResult.get(sensorNumber)!=null){			
			if(!checkErrors(previousSuccessfullSensorResult.get(sensorNumber), lastSensorResult.get(sensorNumber), sensor)){
				lastSuccesfullSensorResult.put(sensorNumber, lastSensorResult.get(sensorNumber));
				previousSuccessfullSensorResult.put(sensorNumber, lastSensorResult.get(sensorNumber));
			}else{
				LOGGER.debug("Odczyt "+printSensorResults(lastSensorResult.get(sensorNumber).getResults())+" uznany za nieprawidłowy !!");
				previousSuccessfullSensorResult.put(sensor.getNumber(), lastSensorResult.get(sensor.getNumber()));
			}
		}
	}
	
	private String printSensorResults(Map<String, SensorResultDataDTO> results) {
		String result="";
		for(String key: results.keySet()) {
			result+="key="+key+": "+results.get(key).getResult()+" ";
		}
		return result;
		
	}

	private boolean checkErrors(SensorResultDTO previousSuccessfullSensorResult, SensorResultDTO lastSensorResult, Sensor sensor) {
		if(previousSuccessfullSensorResult==null){
			return false;
		}
		for(SensorData sensorData : sensor.getSensorDatas()) {
			float maxError=sensorData.getMaxError();
			if(Math.abs(previousSuccessfullSensorResult.getResults().get(sensorData.getName()).getResult()
					-lastSensorResult.getResults().get(sensorData.getName()).getResult())>maxError) {
				return true;
			}
			Float maxValue=sensorData.getMaxValue();
			if(maxValue!=null && lastSensorResult.getResults().get(sensorData.getName()).getResult()>maxValue.floatValue()) {
				return true;
			}
			
		}
		return false;		
	}

	public Map<Integer, SensorResultDTO> getLastSuccesfullSensorResult() {
		return lastSuccesfullSensorResult;
	}	
	
}
