package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SensorResultJson {
	public String sensorName;
	public Integer lastReadTimeElapse;
	public Map<String, SensorResultDataDTO> results ;	
	
	
	public SensorResultJson(){
		
	}
	
	public SensorResultJson(SensorResultDTO sensorResultDTO, String sensorName){
		SensorResultJson result=new SensorResultJson();
		this.lastReadTimeElapse=new Integer((int)(new Date().getTime()-sensorResultDTO.getLastSuccesfullRead().getTime()));
		this.lastReadTimeElapse=this.lastReadTimeElapse/1000;		
		this.results=new HashMap<>(sensorResultDTO.getResults());		
		this.sensorName=sensorName;
	}
	
	public Integer getLastReadTimeElapse() {
		return lastReadTimeElapse;
	}
	public void setLastReadTimeElapse(Integer lastReadTimeElapse) {
		this.lastReadTimeElapse = lastReadTimeElapse;
	}
	

	
	public Map<String, SensorResultDataDTO> getResults() {
		return results;
	}

	public void setResults(Map<String, SensorResultDataDTO> results) {
		this.results = results;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	
}
