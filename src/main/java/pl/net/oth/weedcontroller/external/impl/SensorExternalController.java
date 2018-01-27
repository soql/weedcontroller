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

	public String check(String command) {
		Process process;
		try {
			process = new ProcessBuilder(command.split(" ")).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			if (line.equals(ERROR_MESSAGE)) {
				LOGGER.error("Nieudane pobranie wartości z DHT22 - noException");
				return null;
			}
			return line;
												
		} catch (IOException e) {
			LOGGER.error("Nieudane pobranie wartości z DHT22"); 
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		return null;
	}
}
