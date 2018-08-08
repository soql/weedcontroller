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
	@Column(columnDefinition="varchar(255) default 'NULL'")	
	private String name;		
	@Column
	private Boolean revert;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="parent")	
	private List<SwitchGPIO> gpios;

	@Column
	private String color;
	
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
	
	public List<SwitchGPIO> getGpios() {
		return gpios;
	}

	public void setGpios(List<SwitchGPIO> gpios) {
		this.gpios = gpios;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}
