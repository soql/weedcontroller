package pl.net.oth.weedcontroller.model.dto;

public class SensorResultDataDTO {
	private Float result;
	private String description;
	private String cssName;
	private String unit;
	
	
	public Float getResult() {
		return result;
	}
	public void setResult(Float result) {
		this.result = result;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCssName() {
		return cssName;
	}
	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}