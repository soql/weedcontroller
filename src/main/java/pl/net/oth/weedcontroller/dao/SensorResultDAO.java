package pl.net.oth.weedcontroller.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
