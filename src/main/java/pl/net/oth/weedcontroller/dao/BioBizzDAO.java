package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.BioBizz;
import pl.net.oth.weedcontroller.model.Phase;
import pl.net.oth.weedcontroller.model.RuleVariable;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class BioBizzDAO {

	@PersistenceContext
	private EntityManager em;

	
	public List<BioBizz> getData(Phase phase, Integer week){
		Query query=em.createQuery("SELECT e FROM BioBizz e where phase=:phase and week=:week");
		query.setParameter("phase", phase);
		query.setParameter("week", week);
		return (List<BioBizz>)query.getResultList();
	}			
}
