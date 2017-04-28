package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pi4j.io.gpio.GpioController;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.external.GpioExternalController;
import pl.net.oth.weedcontroller.external.impl.GpioMockExternalController;
import pl.net.oth.weedcontroller.external.impl.GpioPiExternalController;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchDTO;

@Component
public class SwitchService {
	
	private final static Log LOGGER = LogFactory.getLog(SwitchService.class);
	
	@Autowired
	private SwitchDAO switchDAO;

	
	@Autowired
	private GpioExternalController gpioExternalController;
	
	@Transactional
	public void add(Switch s) {
		switchDAO.persist(s);
	}
	
	public List<SwitchDTO> getAllSwitchesWithStates(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			SwitchDTO switchDTO=new SwitchDTO();
			switchDTO.setGpioNumber(switch_.getGpioNumber());
			switchDTO.setName(switch_.getName());
			switchDTO.setState(gpioExternalController.getState(switch_.getGpioNumber()));
			result.add(switchDTO);
		}
		return result;
	}
	
	public List<Integer> getSwitchesConfiguration(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<Integer> result=new ArrayList<Integer>();
		for (Switch switch_ : switches) {				
			result.add(switch_.getGpioNumber());
		}
		return result;
	}
	
	public Boolean setSwitchState(Integer switchNumber, SwitchState state){
		LOGGER.info("Rzadanie zmiany przel. nr "+switchNumber+" na "+state);
		return gpioExternalController.setState(switchNumber.intValue(), state);
	}
}
