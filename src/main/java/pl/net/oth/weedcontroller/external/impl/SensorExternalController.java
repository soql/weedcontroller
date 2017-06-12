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
import org.springframework.context.annotation.Configuration;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.task.SensorTask;

@Configuration
public class SensorExternalController {
	private final static Log LOGGER = LogFactory.getLog(SensorTask.class);
	private static final String ERROR_MESSAGE = "Failed to get reading. Try again!";

	public SensorResultDTO check() {
		Process process;
		try {
			process = new ProcessBuilder("/opt/externalSensor/AdafruitDHT.py", "22", "4").start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			Pattern pattern = Pattern.compile("Temp=(.*)?\\*  Humidity=(.*)?\\%");
			Matcher matcher = pattern.matcher(line);
			if (line.equals(ERROR_MESSAGE) || !matcher.matches()) {
				LOGGER.error("Nieudane pobranie wartości z DHT22 - noException");
				return null;
			}
			float temperature = Float.parseFloat(matcher.group(1));
			float humidity = Float.parseFloat(matcher.group(2));

			LOGGER.info("DHT22 read. Temperature:"+temperature + " Humidity:" + humidity);
			
			return new SensorResultDTO(new Date(), temperature, humidity);

			

		} catch (IOException e) {
			LOGGER.error("Nieudane pobranie wartości z DHT22"); 
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		return null;
	}
}
