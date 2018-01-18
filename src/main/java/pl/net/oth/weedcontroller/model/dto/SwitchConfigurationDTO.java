package pl.net.oth.weedcontroller.model.dto;

public class SwitchConfigurationDTO {
	private String name;
	private String color;
	private boolean activeLog;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isActiveLog() {
		return activeLog;
	}
	public void setActiveLog(boolean activeLog) {
		this.activeLog = activeLog;
	}
	
	
}
