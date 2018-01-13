package pl.net.oth.weedcontroller.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class Switch {
	@Id	
	@Column(columnDefinition="varchar(255) default 'BRAK'")	
	private String name;		
	@Column
	private Boolean revert;
	@Column
	private Double powerUsage;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="parent")	
	private List<SwitchGPIO> gpios;

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
	
	public List<SwitchGPIO> getGpios() {
		return gpios;
	}

	public void setGpios(List<SwitchGPIO> gpios) {
		this.gpios = gpios;
	}	
	
	
}
