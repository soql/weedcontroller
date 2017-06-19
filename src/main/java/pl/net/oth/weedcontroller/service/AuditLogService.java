package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.AuditOperation;
import pl.net.oth.weedcontroller.dao.AuditLogDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;

@Component
public class AuditLogService {
	@Autowired
	private AuditLogDAO auditLogDAO;
	
	@Transactional
	public void save(AuditLog auditLog){
		auditLogDAO.persist(auditLog);
	}
	
	public List<AuditLogDTO> getAuditLog(int number){		
		List result=new ArrayList<AuditLogDTO>();
		List<AuditLog> auditLogs=auditLogDAO.getAuditLog(number);
		for (AuditLog auditLog : auditLogs) {			
			result.add(new AuditLogDTO(auditLog.getUser().getFullName(), auditLog.getIp(), auditLog.getOperation(), auditLog.getTime()));
		}
		return result;
	}	
}
