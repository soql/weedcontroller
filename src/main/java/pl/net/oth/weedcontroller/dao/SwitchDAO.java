package pl.net.oth.weedcontroller.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.external.impl.GpioMockExternalController;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class SwitchDAO {
	private final static Log LOGGER = LogFactory.getLog(SwitchDAO.class);
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(Switch switch_) {
		em.persist(switch_);
	}	
	
	@Transactional
	public void saveOrUpdate(SwitchGPIO switchGpio) {
		em.persist(switchGpio);
	}
	public List<SwitchGPIO> getAllManagedSwitches(){
		Query query=em.createQuery("SELECT e FROM SwitchGPIO e");
		return (List<SwitchGPIO>)query.getResultList();
	}
	
	public List<SwitchGPIO> getAllMqttSwitches(){
		Query query=em.createQuery("SELECT e FROM SwitchGPIO e where e.switchType=1 and e.mqttTopic is not null");
		return (List<SwitchGPIO>)query.getResultList();
	}
	
	public List<Switch> getAllSwitches(){
		Query query=em.createQuery("SELECT e FROM Switch e");
		return (List<Switch>)query.getResultList();
	}
	
	public Switch getSwitchByName(String name){
		return em.find(Switch.class, name);
	}

	

	public String getLastSwitchStateChangeUser(Switch switch_, SwitchState state) {
		Query query=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.state=:state and e.id=(select max(f.id) from SwitchLog f where f.switch_=:switch and f.state=:state)");
		
		query.setParameter("switch", switch_);
		query.setParameter("state", state);
		SwitchLog sw=((SwitchLog)query.getResultList().get(0));
		return sw.getUser()!=null? sw.getUser().getFullName(): sw.getRuleUser();
	}

	public SwitchState getLastState(Switch switch_) {		
			Query query=em.createQuery("SELECT e.state FROM SwitchLog e where e.switch_.name=:switchName and e.id=(SELECT max(f.id) FROM SwitchLog f where f.switch_.name in :switchName)");		
			query.setParameter("switchName", switch_.getName());	
			List result=query.getResultList();
			if(result==null || result.size()==0)
				return SwitchState.OFF;
			return (SwitchState)result.get(0);		
	}

	public Date getLastSwitchStateChangeTime(Switch switch_) {		
		Query query=em.createQuery("SELECT e.date FROM SwitchLog e where e.switch_.name=:switchName and e.id=(SELECT max(f.id) FROM SwitchLog f where f.switch_.name in :switchName)");
		query.setParameter("switchName", switch_.getName());
		List<Timestamp> results=query.getResultList();
		if(results.size()==0)
			return null;
		Date date=new Date(((Timestamp) results.get(0)).getTime());
		LOGGER.debug("getLastSwitchStateChangeTime "+date);
		return date;
	}
	public Date getLastManagedSwitchStateChangeTime(SwitchGPIO switchGpio) {
		Query query=em.createQuery("SELECT e.date FROM SwitchGpioLog e where e.switchGpio.description=:switchName and e.id=(SELECT max(f.id) FROM SwitchGpioLog f where f.switchGpio.gpioNumber in :switchName)");
		query.setParameter("switchName", switchGpio.getGpioNumber());
		List<Timestamp> results=query.getResultList();
		if(results.size()==0)
			return null;
		Date date=new Date(((Timestamp) results.get(0)).getTime());
		LOGGER.debug("getLastManagedSwitchStateChangeTime "+date);
		return date;	}
	private List<Integer> getGpioList(Switch switch_){
		List<Integer> gpioList=new ArrayList<>();
		for(SwitchGPIO switchGPIO : switch_.getGpios()) {
			gpioList.add(switchGPIO.getGpioNumber());
		}
		return gpioList;
	}
	
	@Transactional	
	public void updateGpioActive(Integer gpioNumber, boolean active) {
		SwitchGPIO switchGPIO=em.find(SwitchGPIO.class, gpioNumber);
		switchGPIO.setActive(active);
		saveOrUpdate(switchGPIO);
	}

	public SwitchGPIO getSwitchGpioByNumber(Integer gpioNumber) {
		return em.find(SwitchGPIO.class, gpioNumber);
	}

	public SwitchGPIO getManagedSwitchByName(String name) {
		Query query=em.createQuery("SELECT e FROM SwitchGPIO e where e.description=:switchName");
		query.setParameter("switchName", name);
		List<SwitchGPIO> results=query.getResultList();
		if(results.size()>0) {
			return results.get(0);
		}
		return null;
	}

	public SwitchGPIO getSwitchByMQTTTopic(String topic) {
		Query query=em.createQuery("SELECT e FROM SwitchGPIO e where e.mqttTopic=:mqttTopic");
		query.setParameter("mqttTopic", topic);
		List<SwitchGPIO> results=query.getResultList();
		if(results.size()>0) {
			return results.get(0);
		}
		return null;
	}


	
}
