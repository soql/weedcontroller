package pl.net.oth.weedcontroller.model.dto;

public class SwitchGpioDTO {
	private String name;
	private Boolean active;
	private Integer gpioNumber;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Integer getGpioNumber() {
		return gpioNumber;
	}
	public void setGpioNumber(Integer gpioNumber) {
		this.gpioNumber = gpioNumber;
	}
	
}
