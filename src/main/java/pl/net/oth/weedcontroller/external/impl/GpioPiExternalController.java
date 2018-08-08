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
import pl.net.oth.weedcontroller.external.SwitchController;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchGpioDTO;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Profile("PROD")
@Configuration
public class GpioPiExternalController implements SwitchController{
	private final static Log LOGGER = LogFactory.getLog(GpioPiExternalController.class);
	
	private GpioController gpio;

	@Autowired
	private SwitchService switchService;
	
	private Map<Integer, GpioPinDigitalOutput> gpioPinDigitalOutput;
	
	@Override
	public void init() {
		LOGGER.info("Inicjalizacja GPIO");
		gpio = GpioFactory.getInstance();
		LOGGER.info("Pozytywna inicjalizacja GPIO");
		gpioPinDigitalOutput=new HashMap<Integer, GpioPinDigitalOutput>();
		List<Integer> switches=switchService.getSwitchesConfiguration();
		for (Integer integer : switches) {			
			 gpioPinDigitalOutput.put(integer, gpio.provisionDigitalOutputPin(PinHelper.getInstance().getPin(integer)));
			 LOGGER.info("Ustawienie pinu "+integer+" pomyślne.");
		}		
	}

	public SwitchState getState(int gpioNumber, boolean revert) {		
		PinState pinState=gpioPinDigitalOutput.get(new Integer(gpioNumber)).getState();
		SwitchState realState=null;
		if(pinState.isHigh()){
			realState=SwitchState.OFF;			
		}else{
			realState=SwitchState.ON;
		}
		if(revert){
			return realState.equals(SwitchState.ON)?SwitchState.OFF:SwitchState.ON;
		}
		return realState;
	}
	
	public boolean setState(int gpioNumber, SwitchState switchState, boolean revert) {
		LOGGER.info("Zmiana PINu nr "+gpioNumber+" na "+switchState);
		if(switchState==getState(gpioNumber, revert)){
			return false ;
		}
		if(switchState==SwitchState.ON){
			if(revert){
				gpioPinDigitalOutput.get(gpioNumber).high();
			}else{
				gpioPinDigitalOutput.get(gpioNumber).low();
			}
		}else{
			if(revert){
				gpioPinDigitalOutput.get(gpioNumber).low();
			}else{
				gpioPinDigitalOutput.get(gpioNumber).high();
			}
		}
		
		return true;
	}
	@Override
	public void mergeGpioStates(List<SwitchDTO> allSwitches) {
		for(SwitchDTO switch_:allSwitches) {
			for(SwitchGpioDTO switchGPIO:switch_.getGpio()) {
				SwitchState switchState=getState(switchGPIO.getGpioNumber(), switch_.getIsRevert());
				if(switchGPIO.getActive()) {
					setState(switchGPIO.getGpioNumber(), switch_.getState(), switch_.getIsRevert());
				}else {
					setState(switchGPIO.getGpioNumber(), SwitchState.OFF, switch_.getIsRevert());
				}
			}
		}
		
	}
	
	@PreDestroy
	public void destroy() {
		LOGGER.info("Shutdown GPIO");
		gpio.shutdown();
		LOGGER.info("Pozytywny shutdown GPIO");
	}
}
