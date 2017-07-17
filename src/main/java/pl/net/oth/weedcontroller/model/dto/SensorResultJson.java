package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;

public class SensorResultJson {
	public String sensorName;
	public Integer lastReadTimeElapse;
	public float temperature;	
	public float humidity;
	
	public SensorResultJson(){
		
	}
	
	public SensorResultJson(SensorResultDTO sensorResultDTO, String sensorName){
		SensorResultJson result=new SensorResultJson();
		this.lastReadTimeElapse=new Integer((int)(new Date().getTime()-sensorResultDTO.getLastSuccesfullRead().getTime()));
		this.lastReadTimeElapse=this.lastReadTimeElapse/1000;
		this.humidity=sensorResultDTO.getHumidity();
		this.temperature=sensorResultDTO.getTemperature();	
		this.sensorName=sensorName;
	}
	
	public Integer getLastReadTimeElapse() {
		return lastReadTimeElapse;
	}
	public void setLastReadTimeElapse(Integer lastReadTimeElapse) {
		this.lastReadTimeElapse = lastReadTimeElapse;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	
}
