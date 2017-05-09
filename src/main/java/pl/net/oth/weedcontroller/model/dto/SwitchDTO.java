package pl.net.oth.weedcontroller.model.dto;

import pl.net.oth.weedcontroller.SwitchState;

public class SwitchDTO {
	private int gpioNumber;
	private String name;
	private SwitchState state;
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
	public SwitchState getState() {
		return state;
	}
	public void setState(SwitchState state) {
		this.state = state;
	}	
}
