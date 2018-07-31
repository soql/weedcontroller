package pl.net.oth.weedcontroller.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.Switch;

@Component
public class SensorResultService {
	@Autowired
	private SensorResultDAO sensorResultDAO;

	@Transactional
	public void add(SensorResultLog s) {
		sensorResultDAO.persist(s);
	}
	
	public List<SensorResultLog> getResultsForDate(Date dateFrom, Date dateTo, Sensor sensor, String sensorDataName){
		return sensorResultDAO.getResultsForDate(dateFrom, dateTo, sensor, sensorDataName);
	}

	public float getAggregatedValue(String type, String func, Date dateFrom, Date dateTo, Sensor sensor){
		return sensorResultDAO.getAggregatedValue(type, func, dateFrom, dateTo, sensor);
	}
	
	
}
