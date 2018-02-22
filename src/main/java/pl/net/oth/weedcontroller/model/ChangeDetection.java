package pl.net.oth.weedcontroller.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ChangeDetection {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne
	private Sensor sensor;
	
	@Column
	private Date date;
	
	@Column
	private Integer best;
	
	@Column
	private Integer worse;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getBest() {
		return best;
	}

	public void setBest(Integer best) {
		this.best = best;
	}

	public Integer getWorse() {
		return worse;
	}

	public void setWorse(Integer worse) {
		this.worse = worse;
	}
	
}
