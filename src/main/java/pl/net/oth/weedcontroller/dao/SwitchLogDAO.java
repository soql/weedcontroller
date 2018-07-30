package pl.net.oth.weedcontroller.dao;

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
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.SwitchGpioLog;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class SwitchLogDAO {
	private final static Log LOGGER = LogFactory.getLog(SwitchLogDAO.class);
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(SwitchLog switchLog){
		em.persist(switchLog);
	}
	
	@Transactional
	public void persist(SwitchGpioLog switchLog){
		em.persist(switchLog);
	}
	
	public List<SwitchLog> getSwitchLog(int number, List<String> switches){
		if(switches==null || switches.size()==0) {
			return new ArrayList<>();
		}
		Query query=em.createQuery("SELECT e FROM SwitchLog e where e.switch_.name in (:switchesNames) order by id desc");
		query.setParameter("switchesNames", switches);
		query.setMaxResults(number);
		return (List<SwitchLog>)query.getResultList();
	}

	public List<SwitchGpioLog> getSwitchGpioLog(int number, List<String> switches){
		if(switches==null || switches.size()==0) {
			return new ArrayList<>();
		}
		Query query=em.createQuery("SELECT e FROM SwitchGpioLog e where e.switchGpio.parent.name in (:switchesNames) order by id desc");
		query.setParameter("switchesNames", switches);
		query.setMaxResults(number);
		return (List<SwitchGpioLog>)query.getResultList();
	}
	
	public List<SwitchLog> getLogsForDate(Switch switch_, Date dateFrom, Date dateTo) {
		List<SwitchLog> resultsToReturn=new ArrayList<>();
		Query query=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.date>:dateFrom and e.date<=:dateTo order by e.id asc");
		query.setParameter("switch", switch_);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		List<SwitchLog> results=query.getResultList();
		Query firstEntry=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.date<:dateFrom and e.id=(SELECT max(f.id) FROM SwitchLog f where f.switch_=:switch and f.date<:dateFrom)");
		firstEntry.setParameter("switch", switch_);
		firstEntry.setParameter("dateFrom", dateFrom);		
		
		Query lastEntry=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.date>:dateTo and e.id=(SELECT min(f.id) FROM SwitchLog f where f.switch_=:switch and f.date>:dateTo)");
		lastEntry.setParameter("switch", switch_);
		lastEntry.setParameter("dateTo", dateTo);		
						
		List<SwitchLog> firstResults=(List<SwitchLog>) firstEntry.getResultList();
		List<SwitchLog> lastResults=(List<SwitchLog>) lastEntry.getResultList();
				
		
		if(results.size()==0 && firstResults.size()==0 && lastResults.size()==0) {
			return resultsToReturn;
		}
		
		if(firstResults!=null && firstResults.size()>0) {
			SwitchLog firstResult=firstResults.get(0);
			firstResult.setDate(dateFrom);
			resultsToReturn.add(firstResult);
		}else {
			SwitchLog firstMock=new SwitchLog();			
			firstMock.setDate(dateFrom);
			firstMock.setState(SwitchState.OFF);
			firstMock.setSwitch_(switch_);	
			resultsToReturn.add(firstMock);
		}
		resultsToReturn.addAll(results);
		
		
		
		if(lastResults!=null && lastResults.size()>0) {
			LOGGER.debug("Last result for "+switch_.getName()+" = "+lastResults.get(0));
			SwitchLog lastResult=lastResults.get(0);
			lastResult.setDate(dateTo);
			resultsToReturn.add((SwitchLog) lastResults.get(0));
		}else {
			if(results.size()>0) {											
			}else {
				SwitchLog lastMock=new SwitchLog();
				lastMock.setDate(dateTo);
				lastMock.setState(resultsToReturn.get(0).getState());
				lastMock.setSwitch_(switch_);				
				resultsToReturn.add(lastMock);
			}
		}	
						
		return resultsToReturn;
	}	
	public List<SwitchGpioLog> getManagedSwitchLogsForDate(SwitchGPIO switch_, Date dateFrom, Date dateTo) {
		List<SwitchGpioLog> resultsToReturn=new ArrayList<>();
		Query query=em.createQuery("SELECT e FROM SwitchGpioLog e where e.switchGpio=:switch and e.date>:dateFrom and e.date<=:dateTo order by e.id asc");
		query.setParameter("switch", switch_);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		List<SwitchGpioLog> results=query.getResultList();
		Query firstEntry=em.createQuery("SELECT e FROM SwitchGpioLog e where e.switchGpio=:switch and e.date<:dateFrom and e.id=(SELECT max(f.id) FROM SwitchGpioLog f where f.switchGpio=:switch and f.date<:dateFrom)");
		firstEntry.setParameter("switch", switch_);
		firstEntry.setParameter("dateFrom", dateFrom);		
		
		Query lastEntry=em.createQuery("SELECT e FROM SwitchGpioLog e where e.switchGpio=:switch and e.date>:dateTo and e.id=(SELECT min(f.id) FROM SwitchGpioLog f where f.switchGpio=:switch and f.date>:dateTo)");
		lastEntry.setParameter("switch", switch_);
		lastEntry.setParameter("dateTo", dateTo);		
						
		List<SwitchGpioLog> firstResults=(List<SwitchGpioLog>) firstEntry.getResultList();
		List<SwitchGpioLog> lastResults=(List<SwitchGpioLog>) lastEntry.getResultList();
				
		
		if(results.size()==0 && firstResults.size()==0 && lastResults.size()==0) {
			return resultsToReturn;
		}
		
		if(firstResults!=null && firstResults.size()>0) {
			SwitchGpioLog firstResult=firstResults.get(0);
			firstResult.setDate(dateFrom);
			resultsToReturn.add(firstResult);
		}else {
			SwitchGpioLog firstMock=new SwitchGpioLog();			
			firstMock.setDate(dateFrom);
			firstMock.setState(SwitchState.OFF);
			firstMock.setSwitchGpio(switch_);	
			resultsToReturn.add(firstMock);
		}
		resultsToReturn.addAll(results);
		
		
		
		if(lastResults!=null && lastResults.size()>0) {
			LOGGER.debug("Last result for "+switch_.getDescription()+" = "+lastResults.get(0));
			SwitchGpioLog lastResult=lastResults.get(0);
			lastResult.setDate(dateTo);
			resultsToReturn.add((SwitchGpioLog) lastResults.get(0));
		}else {
			if(results.size()>0) {											
			}else {
				SwitchGpioLog lastMock=new SwitchGpioLog();
				lastMock.setDate(dateTo);
				lastMock.setState(resultsToReturn.get(0).getState());
				lastMock.setSwitchGpio(switch_);				
				resultsToReturn.add(lastMock);
			}
		}	
						
		return resultsToReturn;
	}			
}
