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
		Query query=em.createQuery("SELECT e FROM Rule e where e.active=true and (e.nextTimeExecution is null or e.nextTimeExecution<:nowDate)");
		query.setParameter("nowDate", new Date());
		return (List<Rule>)query.getResultList();
	}

	public Rule getRuleById(Integer actualRuleId) {
		return em.find(Rule.class, actualRuleId);		
	}

	@Transactional
	public void update(Rule r) {
		em.merge(r);
		
	}
}
