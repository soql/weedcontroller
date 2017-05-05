package pl.net.oth.weedcontroller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.SensorResultDTO;
import pl.net.oth.weedcontroller.service.SensorResultService;

@Configuration
@EnableScheduling
public class HistoryTask {
	@Autowired
	private SensorTask sensorTask;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Scheduled(fixedDelay = 10000)
	public void putSensorDataToDatabase(){
		SensorResultDTO sensorResultDTO=sensorTask.getLastSensorResult();
		if(sensorResultDTO==null)
			return;
		SensorResultLog sensorResult=new SensorResultLog();
		sensorResult.setDate(sensorResultDTO.getLastSuccesfullRead());
		sensorResult.setTemperature(sensorResultDTO.getTemperature());
		sensorResult.setHumidity(sensorResultDTO.getHumidity());
		sensorResultService.add(sensorResult);
	}

	public SensorTask getSensorTask() {
		return sensorTask;
	}

	public void setSensorTask(SensorTask sensorTask) {
		this.sensorTask = sensorTask;
	}
	
}
