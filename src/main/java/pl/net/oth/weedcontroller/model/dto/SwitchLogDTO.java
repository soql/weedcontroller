package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;

import pl.net.oth.weedcontroller.SwitchState;

public class SwitchLogDTO {
	private String userName;
	private String switchName;
	private SwitchState switchState;		
	private Date date;
	
	public SwitchLogDTO(String userName, String switchName, SwitchState switchState, Date date) {
		super();
		this.userName = userName;
		this.switchName = switchName;
		this.switchState = switchState;
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	public SwitchState getSwitchState() {
		return switchState;
	}
	public void setSwitchState(SwitchState switchState) {
		this.switchState = switchState;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
