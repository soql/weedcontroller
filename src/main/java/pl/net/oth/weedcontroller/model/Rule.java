package pl.net.oth.weedcontroller.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	@Column
	private Boolean active;
	@Column(length=1000)
	private String condition_;
	@Column(length=1000)	
	private String expression_;
	@Column
	private Date nextTimeExecution;
	@Column
	private Boolean sms;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCondition_() {
		return condition_;
	}
	public void setCondition_(String condition_) {
		this.condition_ = condition_;
	}
	public String getExpression_() {
		return expression_;
	}
	public void setExpression_(String expression_) {		
		this.expression_ = expression_;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getNextTimeExecution() {
		return nextTimeExecution;
	}
	public void setNextTimeExecution(Date nextTimeExecution) {
		this.nextTimeExecution = nextTimeExecution;
	}
	public Boolean getSms() {
		return sms;
	}
	public void setSms(Boolean sms) {
		this.sms = sms;
	}				
}
