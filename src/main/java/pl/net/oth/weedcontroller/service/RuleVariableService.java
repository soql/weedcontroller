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
import pl.net.oth.weedcontroller.dao.RuleVariableDAO;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.RuleVariable;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;

@Component
public class RuleVariableService {
	@Autowired
	private RuleVariableDAO ruleVariableDAO;
	
	@Transactional
	public void save(RuleVariable ruleVariable) {
		ruleVariableDAO.persist(ruleVariable);
	}
	
	public List<RuleVariable> getAll(){		
		return ruleVariableDAO.getAll();
	}	
}
