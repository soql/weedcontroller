package pl.net.oth.weedcontroller.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorData;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SensorResultDataDTO;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;

@Configuration
@EnableScheduling
public class HistoryTask {
	private final static Log LOGGER = LogFactory.getLog(HistoryTask.class);
	
	@Autowired
	private SensorTask sensorTask;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Autowired
	private SensorService sensorService;
	
	@Scheduled(fixedDelay = 15000)
	public void putSensorDataToDatabase(){
		LOGGER.debug("Próba zapisu wyników sensorów do bazy.");
		List<Sensor> allSensors=new ArrayList<>(sensorService.getAllCommandSensors());
		allSensors.addAll(sensorService.getAllMQTTSensors());
		for(Sensor sensor:allSensors){
			LOGGER.debug("Sensor "+sensor.getName());
			SensorResultDTO sensorResultDTO=sensorTask.getLastSuccesfullSensorResult().get(sensor.getNumber());
			if(sensorResultDTO==null)
				continue;
			for(SensorData sensorData: sensor.getSensorDatas()) {
				LOGGER.debug("	Czujnik "+sensorData.getName());
				SensorResultLog sensorResult=new SensorResultLog();
				sensorResult.setSensorData(sensorData);
				sensorResult.setDate(sensorResultDTO.getLastSuccesfullRead());
				boolean writeLog=false;			
				if(sensorResultDTO.getResults().get(sensorData.getName())!=null) {					
					sensorResult.setValue(sensorResultDTO.getResults().get(sensorData.getName()).getResult());
					writeLog=true;
					LOGGER.debug("Zapis wyników sensora "+sensorData.getName());
				}
				if(writeLog) {
					sensorResultService.add(sensorResult);
				}
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
