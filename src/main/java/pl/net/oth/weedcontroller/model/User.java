package pl.net.oth.weedcontroller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@Column
	private String login;
	@Column
	private String fullName;
	@Column
	private String password;
	@Column
	private Boolean enabled;
	@Column
	private String phoneNumber;
	@Column
	private Boolean sendSMS;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Boolean getSendSMS() {
		return sendSMS;
	}
	public void setSendSMS(Boolean sendSMS) {
		this.sendSMS = sendSMS;
	}
	
}
