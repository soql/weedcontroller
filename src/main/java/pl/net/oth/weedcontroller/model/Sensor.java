package pl.net.oth.weedcontroller.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;


@Entity
public class Sensor {
	@Id
	@Column
	private int number;
	
	@Column
	private String name;

	@Column
	private String command;
	
	@Column
	private Boolean active;

	@Column
	private String mqttTopic;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="parent")	
	@OrderBy("id ASC")
	private List<SensorData> sensorDatas;
	
	private Boolean visibleOnGui;
	
	private Boolean checkChanges;
	
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

	public Boolean getCheckChanges() {
		return checkChanges;
	}

	public void setCheckChanges(Boolean checkChanges) {
		this.checkChanges = checkChanges;
	}

	public Boolean getVisibleOnGui() {
		return visibleOnGui;
	}

	public void setVisibleOnGui(Boolean visibleOnGui) {
		this.visibleOnGui = visibleOnGui;
	}

	public String getMqttTopic() {
		return mqttTopic;
	}

	public void setMqttTopic(String mqttTopic) {
		this.mqttTopic = mqttTopic;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	

}
