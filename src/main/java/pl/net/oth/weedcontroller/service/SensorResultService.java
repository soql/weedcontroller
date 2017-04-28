package pl.net.oth.weedcontroller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.model.SensorResult;
import pl.net.oth.weedcontroller.model.Switch;

@Component
public class SensorResultService {
	@Autowired
	private SensorResultDAO sensorResultDAO;

	@Transactional
	public void add(SensorResult s) {
		sensorResultDAO.persist(s);
	}
}
