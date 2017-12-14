package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Switch {
	@Id
	@Column
	private int gpioNumber;
	@Column
	private String name;		
	@Column
	private Boolean revert;
	@Column
	private Double powerUsage;

	public int getGpioNumber() {
		return gpioNumber;
	}

	public void setGpioNumber(int gpioNumber) {
		this.gpioNumber = gpioNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getRevert() {
		return revert;
	}

	public void setRevert(Boolean revert) {
		this.revert = revert;
	}

	public Double getPowerUsage() {
		return powerUsage;
	}

	public void setPowerUsage(Double powerUsage) {
		this.powerUsage = powerUsage;
	}		
}
