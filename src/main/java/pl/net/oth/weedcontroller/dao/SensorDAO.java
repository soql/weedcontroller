package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Sensor;

@Component
public class SensorDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Sensor> getAllSensors(){
		Query query=em.createQuery("SELECT e FROM Sensor e");		
		return (List<Sensor>)query.getResultList();
	}

	public String getNameByNumber(Integer number) {
			
		return em.find(Sensor.class, number).getName();
	}
}
