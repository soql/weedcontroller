package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.task.HistoryTask;
import pl.net.oth.weedcontroller.task.ruletask.Command;

@Component
public class SensorResultDAO {
	private final static Log LOGGER=LogFactory.getLog(SensorResultDAO.class);
	
	@PersistenceContext
	private EntityManager em;
	
	public void persist(SensorResultLog sensorResult) {
		em.persist(sensorResult);
	}

	public List<SensorResultLog> getResultsForDate(Date dateFrom, Date dateTo, Sensor sensor) {
		Query q=em.createQuery("SELECT e FROM SensorResultLog e where e.date>:dateFrom and e.date<=:dateTo and e.sensor=:sensor");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		q.setParameter("sensor", sensor);
		return q.getResultList();
	}

	public float getAggregatedValue(String type, String func, Date dateFrom, Date dateTo, Sensor sensor) {
		
		Query q=em.createQuery("SELECT "+func+"(e."+type+") FROM SensorResultLog e where e.date>:dateFrom and e.date<=:dateTo and e.sensor=:sensor");
		System.out.println(dateFrom+" "+dateTo);
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		q.setParameter("sensor", sensor);
		Object result=q.getSingleResult();
		if(result instanceof Double){
			return ((Double)result).floatValue();
		}
		if(result instanceof Float){
			return ((Float)result).floatValue();
		}
		LOGGER.error("Nie można rozpoznać typu wyniku dla "+result);
		return 0;
	}

}
