package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SensorData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;	
	
	@ManyToOne
	private Sensor parent;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Column
	private String unit;
	@Column
	private String cssName;
	
	@Column(name="regexp_")
	private String regexp;
	
	@Column
	private Float maxError;
	
	@Column
	private Float maxValue;

	@Column
	private String transformExpression;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Sensor getParent() {
		return parent;
	}

	public void setParent(Sensor parent) {
		this.parent = parent;
	}

	public Float getMaxError() {
		return maxError;
	}

	public void setMaxError(Float maxError) {
		this.maxError = maxError;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}

	public String getTransformExpression() {
		return transformExpression;
	}

	public void setTransformExpression(String transformExpression) {
		this.transformExpression = transformExpression;
	}

	public Float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}
	
	
	
	
}
