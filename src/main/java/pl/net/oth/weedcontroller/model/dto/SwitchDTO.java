package pl.net.oth.weedcontroller.model.dto;

import java.util.List;

import pl.net.oth.weedcontroller.SwitchState;

public class SwitchDTO {	
	private String name;
	private SwitchState state;	
	private Boolean isRevert;
	private List<SwitchGpioDTO> gpio;
	
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
	
	public List<SwitchGpioDTO> getGpio() {
		return gpio;
	}
	public void setGpio(List<SwitchGpioDTO> gpio) {
		this.gpio = gpio;
	}
	public Boolean getIsRevert() {
		return isRevert;
	}
	public void setIsRevert(Boolean isRevert) {
		this.isRevert = isRevert;
	}
	
	
	
}
