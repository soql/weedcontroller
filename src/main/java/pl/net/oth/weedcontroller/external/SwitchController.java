package pl.net.oth.weedcontroller.external;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;

public interface SwitchController {
	public SwitchState getState(int gpioNumber, boolean revert) ;	
	public boolean setState(int gpioNumber, SwitchState switchState, boolean revert);
	public void mergeGpioStates(List<SwitchDTO> list);	
}
