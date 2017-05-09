package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.Rule;

@Component
public class RuleDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Rule> getAllActiveRules(){
		Query query=em.createQuery("SELECT e FROM Rule e where e.active=true");
		return (List<Rule>)query.getResultList();
	}
}
