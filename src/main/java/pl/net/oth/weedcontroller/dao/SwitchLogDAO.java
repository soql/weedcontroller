package pl.net.oth.weedcontroller.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;

@Component
@EnableTransactionManagement
public class SwitchLogDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void persist(SwitchLog switchLog){
		em.persist(switchLog);
	}
	
	public List<SwitchLog> getSwitchLog(int number){
		Query query=em.createQuery("SELECT e FROM SwitchLog e order by id desc");
		query.setMaxResults(number);
		return (List<SwitchLog>)query.getResultList();
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
		
		Query lastEntry=em.createQuery("SELECT e FROM SwitchLog e where e.switch_=:switch and e.date>:dateTo and e.id=(SELECT min(f.id) FROM SwitchLog f where f.switch_=:switch and e.date>:dateTo)");
		lastEntry.setParameter("switch", switch_);
		lastEntry.setParameter("dateTo", dateTo);		
		
		List<SwitchLog> firstResults=(List<SwitchLog>) firstEntry.getResultList();
		if(firstResults!=null && firstResults.size()>0) {
			resultsToReturn.add(firstResults.get(0) );
		}
		resultsToReturn.addAll(results);
		List<SwitchLog> lastResults=(List<SwitchLog>) lastEntry.getResultList();
		if(lastResults!=null && lastResults.size()>0) {
			resultsToReturn.add((SwitchLog) lastResults.get(0));
		}
		
		
		return resultsToReturn;
	}			
}
