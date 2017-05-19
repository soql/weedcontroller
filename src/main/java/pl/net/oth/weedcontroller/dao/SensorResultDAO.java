package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.task.HistoryTask;

@Component
public class SensorResultDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void persist(SensorResultLog sensorResult) {
		em.persist(sensorResult);
	}

	public List<SensorResultLog> getResultsForDate(Date dateFrom, Date dateTo) {
		Query q=em.createQuery("SELECT e FROM SensorResultLog e where e.date>:dateFrom and e.date<=:dateTo");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		return q.getResultList();
	}
}
