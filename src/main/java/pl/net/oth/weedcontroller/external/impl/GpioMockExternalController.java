package pl.net.oth.weedcontroller.external.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.external.GpioExternalController;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Profile("DEV")
@Configuration
public class GpioMockExternalController implements GpioExternalController{
	private final static Log LOGGER = LogFactory.getLog(GpioMockExternalController.class);
	
	private Map<Integer, SwitchState> gpio;

	@Autowired
	private SwitchService switchService;
	
	private Map<Integer, SwitchState> gpioPinDigitalOutput;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Inicjalizacja GPIO");
		gpio = new HashMap<Integer, SwitchState>();
		LOGGER.info("Pozytywna inicjalizacja GPIO");
		gpioPinDigitalOutput=new HashMap<Integer, SwitchState>();
		List<Integer> switches=switchService.getSwitchesConfiguration();
		for (Integer integer : switches) {			
			 gpioPinDigitalOutput.put(integer,SwitchState.OFF);
			 LOGGER.info("Ustawienie pinu "+integer+" pomyślne.");
		}
		
	}

	public SwitchState getState(int gpioNumber) {		
		return gpioPinDigitalOutput.get(new Integer(gpioNumber));		
	}
	
	public boolean setState(int gpioNumber, SwitchState switchState) {
		if(switchState==getState(gpioNumber)){
			return false ;
		}
		if(switchState==SwitchState.OFF){
			gpioPinDigitalOutput.put(new Integer(gpioNumber), SwitchState.ON);
		}else{
			gpioPinDigitalOutput.put(new Integer(gpioNumber), SwitchState.OFF);
		}
		
		return true;
	}
	@PreDestroy
	public void destroy() {
		LOGGER.info("Shutdown GPIO");		
		LOGGER.info("Pozytywny shutdown GPIO");
	}
}
