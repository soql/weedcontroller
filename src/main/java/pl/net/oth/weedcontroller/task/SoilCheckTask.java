package pl.net.oth.weedcontroller.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.ChangeDetection;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.service.ChangeDetectionService;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;

@Configuration
@EnableScheduling
public class SoilCheckTask {
	private final static Log LOGGER = LogFactory.getLog(SoilCheckTask.class);
	
	public static final String LAST_SOIL_CHECK = "LAST_SOIL_CHECK";
	
	private final static int SEC = 1000;
	private final static int HUMIDITY_LEVEL=600;
	private final static int MEASURMENT_TIME=300;
	private final static int MIN_TIME_DIFFERENCE=30;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ChangeDetectionService changeDetectionService;

	@Scheduled(fixedDelay = 300000)
	public void soilCheck() {		
		LOGGER.info("Task od weryfikacji zmian wilgotności gleby - start");
		Date startDate=getStartDate();
		if(startDate==null) {
			LOGGER.info("Funkcjonalność weryfikacji zmiany wilgotności wyłączona - brak konfiguracji ");
		}
		Date actualDate=new Date();
		List<Sensor> sensors=sensorService.getSensorsWithCheck();
		for (Sensor sensor : sensors) {
			LOGGER.info("Weryfikacja zmian na sensorze "+sensor.getName());
			List<SensorResultLog> sersorLogs = sensorResultService
					.getResultsForDate(startDate, actualDate, sensor);
			SensorResultLog[] list = new SensorResultLog[sersorLogs.size()];
			sersorLogs.toArray(list);
			boolean active=false;
			int besti=0;
			int bestj=0;
			int worsei=0;
			int worsej=0;
			for (int i = 0; i < list.length;) {
				for (int j = 0; j < list.length;) {
					if (list[j].getDate().getTime() - list[i].getDate().getTime() < MEASURMENT_TIME * SEC) {
						j++;
						continue;
					}				
					if (list[i].getHumidity() - list[j].getHumidity() > HUMIDITY_LEVEL) {
						if(!active) {
							active=true;
							besti=i;
							bestj=j;
							worsei=i;
							worsej=j;
						}else {
							if((list[i].getHumidity() - list[j].getHumidity())
									>
								(list[besti].getHumidity() - list[bestj].getHumidity())){
								besti=i;
								bestj=j;
							}
							if((list[i].getHumidity() - list[j].getHumidity())
									<
								(list[worsei].getHumidity() - list[worsej].getHumidity())){
								worsei=i;
								worsej=j;
							}
						}
					}else {
						if(active) {
							confirmSoilChange(sensor, actualDate, list, besti, bestj, worsei, worsej);						
						}
						active=false;
					}
					i++;
				}
				i++;
			}
		}
		updateConfig(actualDate);
		LOGGER.info("Task od weryfikacji zmian wilgotności gleby - koniec");
	}

	

	private void confirmSoilChange(Sensor sensor,  Date actualDate, SensorResultLog[] list, int besti, int bestj, int worsei, int worsej) {
		ChangeDetection lastChangeDetection=changeDetectionService.getLast(sensor);
		if(lastChangeDetection==null || actualDate.getTime()-lastChangeDetection.getDate().getTime()>MIN_TIME_DIFFERENCE*SEC*60) {
			log(sensor, list,  besti,  bestj,  worsei,  worsej, true);
			ChangeDetection changeDetection=new ChangeDetection();
			changeDetection.setDate(list[worsej].getDate());
			changeDetection.setSensor(sensor);
			changeDetection.setBest((int)(list[besti].getHumidity() - list[bestj].getHumidity()));
			changeDetection.setWorse((int)(list[worsei].getHumidity() - list[worsej].getHumidity()));
			changeDetectionService.save(changeDetection);
			LOGGER.debug("Zapisano wynik");
		}else {
			log(sensor, list,  besti,  bestj,  worsei,  worsej, false);
			
		}
	}

	public void log(Sensor sensor, SensorResultLog[] list, int besti, int bestj, int worsei, int worsej, boolean ok){
		if(!ok) {
			LOGGER.error("Wykryto podlanie ale za mały odstęp czasowy !!");	
		}
		LOGGER.info("Wykryto podlanie "+sensor.getName());
		LOGGER.info("BEST: " + list[bestj].getHumidity() + " -> " + list[besti].getHumidity()
				+ "(" + (list[besti].getHumidity() - list[bestj].getHumidity()) + ") ("+formatDate(list[besti].getDate())+" do "+formatDate(list[bestj].getDate())+")");	
		LOGGER.info("WORSE: " + list[worsej].getHumidity() + " -> " + list[worsei].getHumidity()
				+ "(" + (list[worsei].getHumidity() - list[worsej].getHumidity()) + ") ("+formatDate(list[worsei].getDate())+" do "+formatDate(list[worsej].getDate())+")");		
	}

	private Date getStartDate() {
		pl.net.oth.weedcontroller.model.Configuration conf=configurationService.getByKey(LAST_SOIL_CHECK);
		if(conf==null)
			return null;
		long timeFromConfig=Long.parseLong(conf.getValue());
		return new Date(timeFromConfig-150*SEC);
	}



	private void updateConfig(Date actualDate) {
		pl.net.oth.weedcontroller.model.Configuration conf=new pl.net.oth.weedcontroller.model.Configuration();
		conf.setKey(LAST_SOIL_CHECK);
		conf.setValue(String.valueOf(actualDate.getTime()));
		configurationService.update(conf);	
	}
	
	public String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
