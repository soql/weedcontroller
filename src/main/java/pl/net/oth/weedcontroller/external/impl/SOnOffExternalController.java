package pl.net.oth.weedcontroller.external.impl;

import java.util.List;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.external.SwitchController;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;

public class SOnOffExternalController implements SwitchController{

	@Override
	public SwitchState getState(int gpioNumber, boolean revert) {		
		return null;
	}

	@Override
	public boolean setState(int gpioNumber, SwitchState switchState, boolean revert) { 
		return false;
	}

	@Override
	public void mergeGpioStates(List<SwitchDTO> list) {	
		
	}

}
