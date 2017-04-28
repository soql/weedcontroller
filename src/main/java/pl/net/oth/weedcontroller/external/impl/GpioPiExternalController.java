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

@Profile("PROD")
@Configuration
public class GpioPiExternalController implements GpioExternalController{
	private final static Log LOGGER = LogFactory.getLog(GpioPiExternalController.class);
	
	private GpioController gpio;

	@Autowired
	private SwitchService switchService;
	
	private Map<Integer, GpioPinDigitalOutput> gpioPinDigitalOutput;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Inicjalizacja GPIO");
		gpio = GpioFactory.getInstance();
		LOGGER.info("Pozytywna inicjalizacja GPIO");
		gpioPinDigitalOutput=new HashMap<Integer, GpioPinDigitalOutput>();
		List<Integer> switches=switchService.getSwitchesConfiguration();
		for (Integer integer : switches) {			
			 gpioPinDigitalOutput.put(integer, gpio.provisionDigitalOutputPin(PinHelper.getInstance().getPin(integer), PinState.LOW));
			 LOGGER.info("Ustawienie pinu "+integer+" pomyślne.");
		}
		
	}

	public SwitchState getState(int gpioNumber) {		
		PinState pinState=gpioPinDigitalOutput.get(new Integer(gpioNumber)).getState();
		if(pinState.isHigh()){
			return SwitchState.ON;			
		}else{
			return SwitchState.OFF;
		}
	}
	
	public boolean setState(int gpioNumber, SwitchState switchState) {
		if(switchState==getState(gpioNumber)){
			return false ;
		}
		if(switchState==SwitchState.OFF){
			gpioPinDigitalOutput.get(gpioNumber).low();
		}else{
			gpioPinDigitalOutput.get(gpioNumber).high();
		}
		
		return true;
	}
	@PreDestroy
	public void destroy() {
		LOGGER.info("Shutdown GPIO");
		gpio.shutdown();
		LOGGER.info("Pozytywny shutdown GPIO");
	}
}
