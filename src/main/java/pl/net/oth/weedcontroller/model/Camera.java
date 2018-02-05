package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Camera {
	@Id
	@Column(name="name_")
	private String name;
	
	@Column
	private String takeFotoCommand;
	
	@Column
	private String lastFoto;

	@Column
	private Boolean active;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTakeFotoCommand() {
		return takeFotoCommand;
	}

	public void setTakeFotoCommand(String takeFotoCommand) {
		this.takeFotoCommand = takeFotoCommand;
	}

	public String getLastFoto() {
		return lastFoto;
	}

	public void setLastFoto(String lastFoto) {
		this.lastFoto = lastFoto;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
