package pl.net.oth.weedcontroller.task;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Camera;
import pl.net.oth.weedcontroller.model.CameraFoto;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.service.CameraFotoService;
import pl.net.oth.weedcontroller.service.CameraService;
import pl.net.oth.weedcontroller.service.ConfigurationService;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class CameraTask {	
	private final static Log LOGGER=LogFactory.getLog(CameraTask.class);
	public static final String IMAGE_FOLDER="/opt/camera/";
	/*In hours*/
	public static final long IMAGE_TIME_TO_KEEP=10;
	
	public static final String CAMERA_ON="CAMERA_ON";
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private CameraService cameraService;
	
	@Autowired
	private CameraFotoService cameraFotoService;
	
	@Scheduled(fixedDelay = 5000)		
	public void takeFotos(){
		Configuration cameraOn=configurationService.getByKey(CAMERA_ON);
		if(cameraOn==null || cameraOn.getValue().equals("OFF")){
			LOGGER.debug("Kamery wyłączone konfiguracyjnie");
			return;
		}
		
		List<Camera> cameras=cameraService.getAllCameras();
		for (Camera camera : cameras) {
			takeFoto(camera);
		}
		
	}
	
		
	private void takeFoto(Camera camera) {
		if(camera.getActive()==null || !camera.getActive().booleanValue())
			return;
		Process process;
		try {		
			Long time=new Date().getTime();
			LOGGER.debug("Robię zdjęcie z kamery "+camera.getName()+" o id "+time);
			process = new ProcessBuilder(camera.getTakeFotoCommand(), String.valueOf(time)).start();
			int result=process.waitFor();
			LOGGER.debug("Kamera "+camera.getName()+" = "+result);
			if(result!=0) {
				LOGGER.error("Nieudane wykonanie zdjęca z kamery: "+camera.getName()+" = "+result);
				return;
			}
			camera.setLastFoto(String.valueOf(time));			
			cameraService.save(camera);
			CameraFoto cameraFoto=new CameraFoto();
			cameraFoto.setCamera(camera);
			cameraFoto.setTime(time);
			cameraFoto.setFileName(camera.getName()+"-"+String.valueOf(time));
			cameraFotoService.save(cameraFoto);
			LOGGER.debug("Zdjęcie zrobione pomyślnie");
			removeOldFiles(true);
					
		} catch (IOException e) {
			LOGGER.error("Nieudane wykonanie zdjęca z kamery: "+camera.getName());
			LOGGER.error(Helper.STACK_TRACE, e);
		} catch (InterruptedException e) {
			LOGGER.error("Nieudane wykonanie zdjęca z kamery: "+camera.getName());
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		
	}



	private void removeOldFiles(boolean realDelete) {
		List<Camera> cameras=cameraService.getAllCameras();
			for (Camera camera : cameras) {
			String prefix=camera.getName();
			
			Map<Long, File> filesMap=new HashMap<>();
			for (final File fileEntry : new File(IMAGE_FOLDER).listFiles()) {
				String name=fileEntry.getName();
				if(!name.contains(prefix+"-"))
					continue;
				name=name.replaceAll(prefix+"-", "").replaceAll(".jpg", "");
				Long time=null;
				try{
					time=Long.parseLong(name);
					filesMap.put(time, fileEntry);
				}catch(Exception e){		
					LOGGER.error(Helper.STACK_TRACE, e);
				}			
			}
			
			long now=new Date().getTime();
			for (Entry<Long, File> entry : filesMap.entrySet()) {
				if(entry.getKey().longValue()+IMAGE_TIME_TO_KEEP*60*60*1000<now){
					LOGGER.debug("Usuwam plik: "+entry.getValue().getName());
					if(realDelete) {
						entry.getValue().delete();
						cameraFotoService.remove(entry.getValue().getName());
					}
				}
			}
		}
	}
}
