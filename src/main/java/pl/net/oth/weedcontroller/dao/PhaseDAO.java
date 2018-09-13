package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Camera;
import pl.net.oth.weedcontroller.model.Phase;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class PhaseDAO {

	@PersistenceContext
	private EntityManager em;
			
	public Phase getPhaseById(Integer id){
		return em.find(Phase.class, id);
	}

	public List<Phase> getAll() {
		Query query=em.createQuery("SELECT e FROM Phase e");		
		return (List<Phase>)query.getResultList();
	}			
}
