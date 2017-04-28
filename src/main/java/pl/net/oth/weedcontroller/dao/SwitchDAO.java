package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.Switch;

@Component
public class SwitchDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void persist(Switch switch_) {
		em.persist(switch_);
	}
	public List<Switch> getAllSwitches(){
		Query query=em.createQuery("SELECT e FROM Switch e");
		return (List<Switch>)query.getResultList();
	}
}
