package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import pl.net.oth.weedcontroller.SwitchType;

@Entity
public class SwitchGPIO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
		
	@Column
	private Integer gpioNumber;
	
	@Column
	private boolean active;
	
	@Column
	private String description;

	@ManyToOne
	private Switch parent;
	
	@Column
	private Double powerUsage;
	
	@Column
	private SwitchType switchType;
	
	@Column
	private String mqttTopic;
	
	

	public Integer getGpioNumber() {
		return gpioNumber;
	}

	public void setGpioNumber(Integer gpioNumber) {
		this.gpioNumber = gpioNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Switch getParent() {
		return parent;
	}

	public void setParent(Switch parent) {
		this.parent = parent;
	}

	public Double getPowerUsage() {
		return powerUsage;
	}

	public void setPowerUsage(Double powerUsage) {
		this.powerUsage = powerUsage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SwitchType getSwitchType() {
		return switchType;
	}

	public void setSwitchType(SwitchType switchType) {
		this.switchType = switchType;
	}

	public String getMqttTopic() {
		return mqttTopic;
	}

	public void setMqttopic(String mqqTopic) {
		this.mqttTopic=mqqTopic;
	}
	
	
	
}
