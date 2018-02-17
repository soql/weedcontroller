package pl.net.oth.weedcontroller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SensorResultDataDTO;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;

@Configuration
@EnableScheduling
public class HistoryTask {
	@Autowired
	private SensorTask sensorTask;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Autowired
	private SensorService sensorService;
	
	@Scheduled(fixedDelay = 15000)
	public void putSensorDataToDatabase(){
		for(Sensor sensor:sensorService.getAllSensors()){
			SensorResultDTO sensorResultDTO=sensorTask.getLastSuccesfullSensorResult().get(sensor.getNumber());
			if(sensorResultDTO==null)
				return;
			SensorResultLog sensorResult=new SensorResultLog();
			sensorResult.setSensor(sensor);
			sensorResult.setDate(sensorResultDTO.getLastSuccesfullRead());
			boolean writeLog=false;			
			if(sensorResultDTO.getResults().get(SensorResultDTO.TEMPERATURE)!=null) {
				sensorResult.setTemperature(sensorResultDTO.getResults().get(SensorResultDTO.TEMPERATURE).getResult());
				writeLog=true;
			}
			if(sensorResultDTO.getResults().get(SensorResultDTO.HUMIDITY)!=null) {
				sensorResult.setHumidity(sensorResultDTO.getResults().get(SensorResultDTO.HUMIDITY).getResult());
				writeLog=true;
			}
			if(writeLog) {
				sensorResultService.add(sensorResult);
			}
		}
	}

	public SensorTask getSensorTask() {
		return sensorTask;
	}

	public void setSensorTask(SensorTask sensorTask) {
		this.sensorTask = sensorTask;
	}
	
}
