package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Switch;

@Component
public class RuleDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Rule> getAllActiveRules(){
		Query query=em.createQuery("SELECT e FROM Rule e where (e.sms is null or e.sms=false) and e.active=true and (e.nextTimeExecution is null or e.nextTimeExecution<:nowDate)");
		query.setParameter("nowDate", new Date());
		return (List<Rule>)query.getResultList();
	}
	
	public List<Rule> getAllActiveSMSRules(){
		Query query=em.createQuery("SELECT e FROM Rule e where e.sms=true and e.active=true");		
		return (List<Rule>)query.getResultList();
	}


	public Rule getRuleById(Integer actualRuleId) {
		return em.find(Rule.class, actualRuleId);		
	}

	@Transactional
	public void update(Rule r) {
		em.merge(r);		
	}

	public List<Rule> getAllActiveRulesByPhase(Integer phaseId) {
		Query query=em.createQuery("SELECT e FROM Rule e where (e.sms is null or e.sms=false) and e.active=true and (e.nextTimeExecution is null or e.nextTimeExecution<:nowDate) and (e.phase is null or e.phase.id=:phaseId)");
		query.setParameter("nowDate", new Date());
		query.setParameter("phaseId", phaseId);
		return (List<Rule>)query.getResultList();
	}
}
