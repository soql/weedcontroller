package pl.net.oth.weedcontroller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.model.Rule;

@Component
public class RuleService {
	@Autowired
	private RuleDAO ruleDAO;
	public List<Rule> getAllActiveRules(){
		return ruleDAO.getAllActiveRules();
	}
}
