package pl.net.oth.weedcontroller.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.model.Rule;

@Component
public class RuleService {
	@Autowired
	private RuleDAO ruleDAO;
	public List<Rule> getAllActiveRules(){
		return ruleDAO.getAllActiveRules();
	}
	public List<Rule> getAllActiveSMSRules(){
		return ruleDAO.getAllActiveSMSRules();
	}
	@Transactional
	public void setNextTimeExecution(Integer actualRuleId, int minutes) {
		Rule r=ruleDAO.getRuleById(actualRuleId);
		r.setNextTimeExecution(new Date(new Date().getTime()+minutes*60*1000));
		ruleDAO.update(r);
		
	}
}
