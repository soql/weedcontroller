package pl.net.oth.weedcontroller.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Sensor {
	@Id
	@Column
	private int number;
	
	@Column
	private String name;

	@Column
	private String command;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="parent")	
	private List<SensorData> sensorDatas;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<SensorData> getSensorDatas() {
		return sensorDatas;
	}

	public void setSensorDatas(List<SensorData> sensorDatas) {
		this.sensorDatas = sensorDatas;
	}	
	
	
}
