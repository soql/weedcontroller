package pl.net.oth.weedcontroller.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.net.oth.weedcontroller.SwitchState;

@Entity
@Table(indexes={@Index(columnList="date", name="SwitchGpioLog_date")})
public class SwitchGpioLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne
	private User user;
	
	@Column
	private String ruleUser;
		
	@ManyToOne
	private SwitchGPIO switchGpio;
	
	@Column
	private Date date;
	
	@Column
	private SwitchState state;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public SwitchState getState() {
		return state;
	}
	public void setState(SwitchState state) {
		this.state = state;
	}	
	public String getRuleUser() {
		return ruleUser;
	}
	public void setRuleUser(String ruleUser) {
		this.ruleUser = ruleUser;
	}
	public SwitchGPIO getSwitchGpio() {
		return switchGpio;
	}
	public void setSwitchGpio(SwitchGPIO switchGpio) {
		this.switchGpio = switchGpio;
	}
	
	
	
	
}
