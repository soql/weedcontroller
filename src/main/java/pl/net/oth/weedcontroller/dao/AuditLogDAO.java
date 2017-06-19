package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class AuditLogDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(AuditLog auditLog){
		em.persist(auditLog);
	}
	
	public List<AuditLog> getAuditLog(int number){
		Query query=em.createQuery("SELECT e FROM AuditLog e order by id desc");
		query.setMaxResults(number);
		return (List<AuditLog>)query.getResultList();
	}			
}
