package pl.net.oth.weedcontroller.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.net.oth.weedcontroller.AuditOperation;
import pl.net.oth.weedcontroller.SwitchState;

public class AuditLogDTO {
	private String userName;
	private String ip;
	private AuditOperation auditOperation;		
	private String date;
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
			
	public AuditLogDTO() {
		
	}
	public AuditLogDTO(String userName, String ip, AuditOperation auditOperation, Date date) {
		super();
		this.userName = userName;
		this.ip = ip;
		this.auditOperation = auditOperation;
		this.date = sdf.format(date);
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public AuditOperation getAuditOperation() {
		return auditOperation;
	}
	public void setAuditOperation(AuditOperation auditOperation) {
		this.auditOperation = auditOperation;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	
}
