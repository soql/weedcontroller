package pl.net.oth.weedcontroller.external;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import pl.net.oth.weedcontroller.SwitchState;

public interface GpioExternalController {
	public SwitchState getState(int gpioNumber, boolean revert) ;	
	public boolean setState(int gpioNumber, SwitchState switchState, boolean revert);	
}
