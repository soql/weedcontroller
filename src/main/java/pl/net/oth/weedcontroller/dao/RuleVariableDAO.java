package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.RuleVariable;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class RuleVariableDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(RuleVariable ruleVariable){
		em.persist(ruleVariable);
	}
	
	public List<RuleVariable> getAll(){
		Query query=em.createQuery("SELECT e FROM RuleVariable e");		
		return (List<RuleVariable>)query.getResultList();
	}			
}
