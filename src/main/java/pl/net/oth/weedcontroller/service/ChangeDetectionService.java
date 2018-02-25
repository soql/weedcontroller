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
import pl.net.oth.weedcontroller.dao.ChangeDetectionDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.ChangeDetection;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;
import pl.net.oth.weedcontroller.model.dto.ChangeDetectionDTO;

@Component
public class ChangeDetectionService {
	@Autowired
	private ChangeDetectionDAO changeDetectionDAO;
	
	@Transactional
	public void save(ChangeDetection changeDetection){
		changeDetectionDAO.persist(changeDetection);
	}
	public ChangeDetection getLast(Sensor sensor){
		return changeDetectionDAO.getLast(sensor);
	}
	public List<ChangeDetectionDTO> getChangeDetectionLog(Integer number) {
		List result=new ArrayList<ChangeDetectionDTO>();
		List<ChangeDetection> changeDetections=changeDetectionDAO.getChangeDetectionLog(number);
		for (ChangeDetection changeDetection : changeDetections) {			
			result.add(new ChangeDetectionDTO(changeDetection.getSensor().getName(), changeDetection.getDate(), changeDetection.getWorse()));
		}
		return result;
	}
	public ChangeDetection getChangeDetectionToSend() {
		return changeDetectionDAO.getChangeDetectionToSend();
	}
	public void updateNotification(ChangeDetection changeDetection) {
		changeDetectionDAO.updateNotification(changeDetection);
		
	}
}
