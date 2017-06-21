package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class SwitchDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(Switch switch_) {
		em.persist(switch_);
	}	
	
	public List<Switch> getAllSwitches(){
		Query query=em.createQuery("SELECT e FROM Switch e");
		return (List<Switch>)query.getResultList();
	}
	
	public Switch getSwitchByNumber(Integer number){
		return em.find(Switch.class, number);
	}

	public Switch getSwitchByName(String name) {
		Query query=em.createQuery("SELECT e FROM Switch e where e.name=:name");
		query.setParameter("name", name);
		return (Switch) query.getResultList().get(0);
	}

	public String getLastSwitchStateChangeUser(Switch switch_, SwitchState state) {
		Query query=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.state=:state and e.id=(select max(e.id) from SwitchLog where switch_=:switch and state=:state)");
		
		query.setParameter("switch", switch_);
		query.setParameter("state", state);
		SwitchLog sw=((SwitchLog)query.getResultList().get(0));
		return sw.getUser()!=null? sw.getUser().getFullName(): sw.getRuleUser();
	}

	public SwitchState getLastState(int gpioNumber) {		
		Query query=em.createQuery("SELECT e.state FROM SwitchLog e where e.switch_.gpioNumber=:gpioNumber and e.id=(SELECT max(id) FROM SwitchLog e where e.switch_.gpioNumber=:gpioNumber)");
		query.setParameter("gpioNumber", gpioNumber);
		return (SwitchState) query.getResultList().get(0);
	}
	
	
}
