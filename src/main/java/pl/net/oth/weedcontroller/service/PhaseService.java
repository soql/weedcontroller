package pl.net.oth.weedcontroller.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.AuditOperation;
import pl.net.oth.weedcontroller.dao.AuditLogDAO;
import pl.net.oth.weedcontroller.dao.ConfigurationDAO;
import pl.net.oth.weedcontroller.dao.PhaseDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Phase;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;

@Component
public class PhaseService {	
	private final static Log LOGGER = LogFactory.getLog(PhaseService.class);
	
	@Autowired
	private PhaseDAO phaseDAO;
	
	@Autowired
	private ConfigurationDAO configurationDAO;
		
	public Phase getPhaseById(Integer id){				
		return phaseDAO.getPhaseById(id);
	}

	public Phase getActualPhase() {
		Integer phaseId=Integer.parseInt(configurationDAO.getByKey(ConfigurationService.ACTUAL_PHASE).getValue());
		return getPhaseById(phaseId);
	}

	public int getNumberOfDays() {
		pl.net.oth.weedcontroller.model.Configuration configuration=configurationDAO.getByKey("START_DATE");
		if(configuration==null){
			return 0;
		}
		try {
			Date startDate=Helper.START_DATE_FORMAT.parse(configuration.getValue());
			return (int) ((new Date().getTime()-startDate.getTime())/1000/60/60/24);
		} catch (ParseException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
			return 0;
		}		
	}	
}
