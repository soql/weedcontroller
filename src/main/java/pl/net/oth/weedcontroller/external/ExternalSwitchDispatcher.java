package pl.net.oth.weedcontroller.external;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.SwitchType;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.external.impl.GpioPiExternalController;
import pl.net.oth.weedcontroller.external.impl.SOnOffExternalController;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchGpioDTO;
import pl.net.oth.weedcontroller.service.SwitchService;

@Component
public class ExternalSwitchDispatcher {
	private final static Log LOGGER = LogFactory.getLog(ExternalSwitchDispatcher.class);
	
	@Autowired
	private SwitchController gpioExternalController;

	@Autowired
	private SOnOffExternalController sonoffExternalController;
	
	@Autowired
	private SwitchDAO switchDAO;
	
	@PostConstruct
	public void init(){
		gpioExternalController.init();
		LOGGER.info("Odtworzenie stanów PINów.");
		mergeStates();
		sonoffExternalController.init();
	}
	
	public Boolean setStateToExternalController(Switch switch_, SwitchState state) {			
		boolean result=false;
		for(SwitchGPIO switchGPIO: switch_.getGpios()) {
			if(switchGPIO.isActive()) {
				setState(switchGPIO, state);
				result=true;
			}else {
				setState(switchGPIO, SwitchState.OFF);							
			}
		}
		return result;
	}
	
	private void setState(SwitchGPIO switchGPIO, SwitchState state) {
		switch(switchGPIO.getSwitchType()) {
			case GPIO: 
				gpioExternalController.setState(switchGPIO.getGpioNumber(), state, switchGPIO.getParent().getRevert().booleanValue());
			break;
			case MQTT:
				sonoffExternalController.setState(switchGPIO, state);
			break;
		}
		
	}

	public SwitchState getStateFromExternalController(Switch switch_) {
		for(SwitchGPIO switchGPIO: switch_.getGpios()){
			if(switchGPIO.isActive()) {
				return getState(switchGPIO);
			}
		}
		LOGGER.error("Brak aktywnego GPIO dla "+switch_.getName());
		return SwitchState.OFF;
	}

	private SwitchState getState(SwitchGPIO switchGPIO) {	
		switch(switchGPIO.getSwitchType()) {
		case GPIO:
			return gpioExternalController.getState(switchGPIO.getGpioNumber(), switchGPIO.getParent().getRevert().booleanValue());		
		case MQTT:
			return sonoffExternalController.getState(switchGPIO);	
		default:
			return null;				
		}
	}

	public void mergeStates() {
		gpioExternalController.mergeGpioStates(getAllGpioSwitchesWithLastStates());
		
	}
	
	public List<SwitchDTO> getAllGpioSwitchesWithLastStates(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			SwitchDTO switchDTO=new SwitchDTO();	
			switchDTO.setName(switch_.getName());
			switchDTO.setIsRevert(switch_.getRevert());
			switchDTO.setState(switchDAO.getLastState(switch_));
			switchDTO.setGpio(new ArrayList<SwitchGpioDTO>());
			for(SwitchGPIO switchGPIO : switch_.getGpios()) {
				if(switchGPIO.getGpioNumber()==null)
					continue;
				SwitchGpioDTO switchGpioDTO=new SwitchGpioDTO();
				switchGpioDTO.setActive(switchGPIO.isActive());
				switchGpioDTO.setGpioNumber(switchGPIO.getGpioNumber());
				switchGpioDTO.setName(switchGPIO.getDescription());
				switchDTO.getGpio().add(switchGpioDTO);
			}
			result.add(switchDTO);
		}
		return result;
	}
}
