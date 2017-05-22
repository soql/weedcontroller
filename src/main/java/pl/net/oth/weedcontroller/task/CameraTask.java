package pl.net.oth.weedcontroller.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorResultService;

@Configuration
@EnableScheduling
public class CameraTask {	
	private final static Log LOGGER=LogFactory.getLog(RulesTask.class);
	@Autowired
	private ConfigurationService configurationService;
	@Scheduled(fixedDelay = 30000)
	public void takeFoto(){
		Process process;
		try {		
			String time=String.valueOf(new Date().getTime());
			LOGGER.debug("Robię zdjęcie o id "+time);
			process = new ProcessBuilder("/opt/camera/takeFoto.sh", time).start();
			pl.net.oth.weedcontroller.model.Configuration configuration=new pl.net.oth.weedcontroller.model.Configuration();
			configuration.setKey(ConfigurationService.LAST_FOTO_KEY);
			configuration.setValue(time);
			configurationService.save(configuration);
			LOGGER.debug("Zdjęcie zrobione pomyślnie");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
