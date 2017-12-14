package pl.net.oth.weedcontroller.model.dto;

public class PowerUsageDTO {
	private String switchName;
	private Double powerOnTime;
	private Double MaxTime;
	private Double powerUsage;
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	public Double getPowerOnTime() {
		return powerOnTime;
	}
	public void setPowerOnTime(Double powerOnTime) {
		this.powerOnTime = powerOnTime;
	}
	public Double getPowerUsage() {
		return powerUsage;
	}
	public void setPowerUsage(Double powerUsage) {
		this.powerUsage = powerUsage;
	}
	public Double getMaxTime() {
		return MaxTime;
	}
	public void setMaxTime(Double maxTime) {
		MaxTime = maxTime;
	}
	
	
}
