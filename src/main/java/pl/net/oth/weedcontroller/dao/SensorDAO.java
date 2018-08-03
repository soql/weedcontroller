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
import pl.net.oth.weedcontroller.model.SensorData;

@Component
public class SensorDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Sensor> getAllCommandSensors(){
		Query query=em.createQuery("SELECT e FROM Sensor e where e.command is not null and e.mqttTopic is null and e.active=true");		
		return (List<Sensor>)query.getResultList();
	}
	
	public List<Sensor> getAllMQTTSensors(){
		Query query=em.createQuery("SELECT e FROM Sensor e where e.mqttTopic is not null and e.command is null and e.active=true");		
		return (List<Sensor>)query.getResultList();
	}
	public String getNameByNumber(Integer number) {			
		return em.find(Sensor.class, number).getName();
	}
	public Sensor getSensorByNumber(Integer number){
		return em.find(Sensor.class, number);
	}

	public List<Sensor> getSensorsWithCheck() {		
		Query query=em.createQuery("SELECT e FROM Sensor e where e.checkChanges=true");		
		return (List<Sensor>)query.getResultList();
	}

	public Sensor getSensorByMQTTTopic(String topic) {
		Query query=em.createQuery("SELECT e FROM Sensor e where e.mqttTopic=:topic");
		query.setParameter("topic", topic);
		List<Sensor> result=query.getResultList();
		if(result.size()>0)
			return result.get(0);
		return null;
	}

	public List<SensorData> getAllSensorDataWithAlias() {
		Query query=em.createQuery("SELECT e FROM SensorData e where e.ruleAlias is not null");		
		return (List<SensorData>)query.getResultList();
	}
}
