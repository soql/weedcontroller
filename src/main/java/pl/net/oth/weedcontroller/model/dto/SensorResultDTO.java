package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;

public class SensorResultDTO {
	public Date lastSuccesfullRead;
	public float temperature;
	public float humidity;

	
	public SensorResultDTO(Date lastSuccesfullRead, float temperature, float humidity) {
		super();
		this.lastSuccesfullRead = lastSuccesfullRead;
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public Date getLastSuccesfullRead() {
		return lastSuccesfullRead;
	}

	public void setLastSuccesfullRead(Date lastSuccesfullRead) {
		this.lastSuccesfullRead = lastSuccesfullRead;
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

}
