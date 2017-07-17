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

	
	public List<Sensor> getAllSensors(){
		return sensorDAO.getAllSensors();
	}


	public String getNameByNumber(Integer number) {		
		return sensorDAO.getNameByNumber(number);
	}
}