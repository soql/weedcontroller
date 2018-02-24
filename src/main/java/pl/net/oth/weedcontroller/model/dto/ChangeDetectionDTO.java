package pl.net.oth.weedcontroller.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import pl.net.oth.weedcontroller.AuditOperation;
import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.Sensor;

public class ChangeDetectionDTO {
	
	private String sensorName;		
	private String date;			
	private Integer value;
	
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");


	public ChangeDetectionDTO(String sensorName, Date date, Integer value) {
		super();
		this.sensorName = sensorName;
		this.date = sdf.format(date);
		this.value = value;
	}

	
	

	public String getSensorName() {
		return sensorName;
	}






	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}






	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}
			

}