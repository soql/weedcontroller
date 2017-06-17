package pl.net.oth.weedcontroller.task;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.service.ConfigurationService;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class CameraTask {	
	private final static Log LOGGER=LogFactory.getLog(CameraTask.class);
	public static final String IMAGE_FOLDER="/opt/camera/";
	/*In hours*/
	public static final long IMAGE_TIME_TO_KEEP=24;
	
	public static final String CAMERA_ON="CAMERA_ON";
	@Autowired
	private ConfigurationService configurationService;
	@Scheduled(fixedDelay = 30000)		
	public void takeFoto(){
		Configuration cameraOn=configurationService.getByKey(CAMERA_ON);
		if(cameraOn==null || cameraOn.getValue().equals("OFF")){
			LOGGER.debug("Kamera wyłączona konfiguracyjnie");
			return;
		}
		Process process;
		try {		
			String time=String.valueOf(new Date().getTime());
			LOGGER.debug("Robię zdjęcie o id "+time);
			process = new ProcessBuilder(IMAGE_FOLDER+"takeFoto.sh", time).start();
			Configuration configuration=new pl.net.oth.weedcontroller.model.Configuration();
			configuration.setKey(ConfigurationService.LAST_FOTO_KEY);
			configuration.setValue(time);
			configurationService.save(configuration);
			LOGGER.debug("Zdjęcie zrobione pomyślnie");
			removeOldFiles(true);
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
	}
	private void removeOldFiles(boolean realDelete) {
		Map<Long, File> filesMap=new HashMap<>();
		for (final File fileEntry : new File(IMAGE_FOLDER).listFiles()) {
			String name=fileEntry.getName();
			name=name.replaceAll("image-", "").replaceAll(".jpg", "");
			Long time=null;
			try{
				time=Long.parseLong(name);
				filesMap.put(time, fileEntry);
			}catch(Exception e){			
			}			
		}
		long now=new Date().getTime();
		for (Entry<Long, File> entry : filesMap.entrySet()) {
			if(entry.getKey().longValue()+IMAGE_TIME_TO_KEEP*60*60*1000<now){
				LOGGER.debug("Usuwam plik: "+entry.getValue().getName());
				if(realDelete)
					entry.getValue().delete();
			}
		}
		
	}
}
