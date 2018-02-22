package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.ChangeDetection;
import pl.net.oth.weedcontroller.model.Sensor;


@Component
@EnableTransactionManagement
public class ChangeDetectionDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(ChangeDetection changeDetection){
		em.persist(changeDetection);
	}
	
	public ChangeDetection getLast(Sensor sensor){
		Query query=em.createQuery("SELECT e FROM ChangeDetection e where e.id=(select max(f.id) from ChangeDetection f where f.sensor=:sensor)");
		query.setParameter("sensor", sensor);
		List<ChangeDetection> res=(List<ChangeDetection>)query.getResultList();
		if(res==null || res.size()==0)
			return null;
		return res.get(0);
	}			
}

