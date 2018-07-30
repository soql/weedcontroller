package pl.net.oth.weedcontroller.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.SensorDAO;
import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;

@Component
public class SensorService {
	@Autowired
	private SensorDAO sensorDAO;

	
	public List<Sensor> getAllCommandSensors(){
		return sensorDAO.getAllCommandSensors();
	}
	public List<Sensor> getAllMQTTSensors(){
		return sensorDAO.getAllMQTTSensors();
	}
	public String getNameByNumber(Integer number) {		
		return sensorDAO.getNameByNumber(number);
	}
	
	public Sensor getSensorByNumber(Integer number) {		
		return sensorDAO.getSensorByNumber(number);
	}

	public List<Sensor> getSensorsWithCheck() {
		return sensorDAO.getSensorsWithCheck();
	}
}