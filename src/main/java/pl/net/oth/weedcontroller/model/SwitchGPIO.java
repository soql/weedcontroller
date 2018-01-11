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
	private Sensor parent;
	
	
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

	public Sensor getParent() {
		return parent;
	}

	public void setParent(Sensor parent) {
		this.parent = parent;
	}
	
	
}