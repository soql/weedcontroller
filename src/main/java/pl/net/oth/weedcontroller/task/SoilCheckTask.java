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
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
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
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Autowired
	private SensorService sensorService;

	@Scheduled(fixedDelay = 300000)
	public void soilCheck() {		
		Date startDate=getStartDate();
		Date actualDate=new Date();
		List<Sensor> sensors=sensorService.getSensorsWithCheck();
		for (Sensor sensor : sensors) {
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
							confirmSoilChange(list, besti, bestj, worsei, worsej);						
						}
						active=false;
					}
					i++;
				}
			}
		}
		updateConfig(actualDate);
	}

	

	private void confirmSoilChange(SensorResultLog[] list, int besti, int bestj, int worsei, int worsej) {		
		LOGGER.info("Wykryto podlanie BEST: " + list[bestj].getHumidity() + " -> " + list[besti].getHumidity()
				+ "(" + (list[besti].getHumidity() - list[bestj].getHumidity()) + ") ("+formatDate(list[besti].getDate())+" do "+formatDate(list[bestj].getDate())+")");	
		LOGGER.info("Wykryto podlanie WORSE: " + list[worsej].getHumidity() + " -> " + list[worsei].getHumidity()
				+ "(" + (list[worsei].getHumidity() - list[worsej].getHumidity()) + ") ("+formatDate(list[worsei].getDate())+" do "+formatDate(list[worsej].getDate())+")");
	}



	private Date getStartDate() {
		long timeFromConfig=Integer.parseInt(configurationService.getByKey(LAST_SOIL_CHECK).getValue());
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
