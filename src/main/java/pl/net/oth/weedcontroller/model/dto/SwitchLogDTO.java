package pl.net.oth.weedcontroller.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.net.oth.weedcontroller.SwitchState;

public class SwitchLogDTO {
	public static final int LOG_SWITCH=0;
	public static final int LOG_SWITCH_GPIO=1;
	private String userName;
	private String switchName;
	private SwitchState switchState;		
	private Date realDate;
	private String date;	
	private static SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
	private int logType;		
	
	public SwitchLogDTO() {
		
	}
	public SwitchLogDTO(String userName, String switchName, SwitchState switchState, Date date, int logType) {
		super();
		this.userName = userName;
		this.switchName = switchName;
		this.switchState = switchState;
		this.date = sdf.format(date);
		this.realDate=date;
		this.logType=logType;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Date getRealDate() {
		return realDate;
	}
	public void setRealDate(Date realDate) {
		this.realDate = realDate;
	}
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	
	
}
