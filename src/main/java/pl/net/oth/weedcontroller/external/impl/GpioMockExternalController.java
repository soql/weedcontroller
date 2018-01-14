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
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchGpioDTO;
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
			 gpioPinDigitalOutput.put(integer.intValue(),SwitchState.OFF);
			 LOGGER.info("Ustawienie pinu "+integer+" pomyślne.");
		}
		LOGGER.info("Odtworzenie stanów PINów.");
		List<SwitchDTO> switchesWithLastState=switchService.getAllSwitchesWithLastStates();
		for (SwitchDTO switchDTO : switchesWithLastState) {
			for(SwitchGpioDTO switchGpioDTO: switchDTO.getGpio()) {
				if(switchGpioDTO.getActive().booleanValue()) {
					LOGGER.info("AKTYWNY PIN nr "+ switchGpioDTO.getGpioNumber()+ " ("+switchDTO.getName()+") ustawiamy na "+switchDTO.getState());
					setState( switchGpioDTO.getGpioNumber(), switchDTO.getState(), switchService.getSwitchByName(switchDTO.getName()).getRevert());
				}else {
					LOGGER.info("NIEAKTYWNY PIN nr "+ switchGpioDTO.getGpioNumber()+ " ("+switchDTO.getName()+") ustawiamy na "+SwitchState.OFF);
					setState( switchGpioDTO.getGpioNumber(), SwitchState.OFF, switchService.getSwitchByName(switchDTO.getName()).getRevert());
				}
			}
		}
	}

	public SwitchState getState(int gpioNumber, boolean revert) {		
		return gpioPinDigitalOutput.get(gpioNumber);		
	}
	
	public boolean setState(int gpioNumber, SwitchState switchState, boolean revert) {
		if(switchState.equals(getState(gpioNumber, revert))){
			return false ;
		}
		if(switchState.equals(SwitchState.ON)){
			gpioPinDigitalOutput.put(gpioNumber, SwitchState.ON);
		}else{
			gpioPinDigitalOutput.put(gpioNumber, SwitchState.OFF);
		}
		
		return true;
	}
	@PreDestroy
	public void destroy() {
		LOGGER.info("Shutdown GPIO");		
		LOGGER.info("Pozytywny shutdown GPIO");
	}

	@Override
	public void mergeGpioStates(List<SwitchDTO> allSwitches) {		
		for(SwitchDTO switch_:allSwitches) {
			for(SwitchGpioDTO switchGPIO:switch_.getGpio()) {
				SwitchState realSwitchState=getState(switchGPIO.getGpioNumber(), switch_.getIsRevert());
				if(switchGPIO.getActive()) {
					if(!realSwitchState.equals(switch_.getState())) {
						setState(switchGPIO.getGpioNumber(), switch_.getState(), switch_.getIsRevert());
					}
				}else {
					setState(switchGPIO.getGpioNumber(), SwitchState.OFF, switch_.getIsRevert());
				}
			}
		}
		printMockStates();
	}

	private void printMockStates() {
		for(Integer pin: gpioPinDigitalOutput.keySet()) {
			LOGGER.debug("Stan pinu "+pin+" = "+gpioPinDigitalOutput.get(pin));
		}
		
	}
}
