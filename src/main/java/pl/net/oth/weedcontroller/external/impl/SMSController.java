package pl.net.oth.weedcontroller.external.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.task.SensorTask;

@Component
public class SMSController {
	private final static Log LOGGER = LogFactory.getLog(SensorTask.class);
		
	public boolean sendSMS(String phoneNumber, String text){
		Process process;
		try {
			process = new ProcessBuilder("echo \""+text+"\" | gammu-smsd-inject text "+phoneNumber).start();				
			if(process.exitValue()!=0){
				LOGGER.error("Błąd przy wysyłce SMS");
				return false;
			}

		} catch (IOException e) {
			LOGGER.error("Błąd krytyczny przy wysyłce SMS"); 
			e.printStackTrace();			
		}
		return true;
	}
}
