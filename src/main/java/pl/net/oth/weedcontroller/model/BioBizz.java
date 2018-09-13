package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BioBizz {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column
	private String bioBizzName;
	
	@ManyToOne
	private Phase phase;
		
	@Column
	private Integer week;
	
	
	@Column
	private Integer quantity;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getBioBizzName() {
		return bioBizzName;
	}


	public void setBioBizzName(String bioBizzName) {
		this.bioBizzName = bioBizzName;
	}


	public Phase getPhase() {
		return phase;
	}


	public void setPhase(Phase phase) {
		this.phase = phase;
	}


	public Integer getWeek() {
		return week;
	}


	public void setWeek(Integer week) {
		this.week = week;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
