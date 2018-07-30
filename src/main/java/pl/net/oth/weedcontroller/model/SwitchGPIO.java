package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SwitchGPIO {
	@Id
	@Column
	private int gpioNumber;
	
	@Column
	private boolean active;
	
	@Column
	private String description;

	@ManyToOne
	private Switch parent;
	
	@Column
	private Double powerUsage;
	
	public int getGpioNumber() {
		return gpioNumber;
	}

	public void setGpioNumber(int gpioNumber) {
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
	
	
	
}
