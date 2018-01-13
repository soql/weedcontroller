package pl.net.oth.weedcontroller.event;

import org.springframework.context.ApplicationEvent;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.User;

public class ChangeSwitchGpioStateEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SwitchGPIO switchGpio;
	private SwitchState state;
	private User user;	
	private String ruleUser;
	
	public ChangeSwitchGpioStateEvent(Object source, SwitchGPIO switchGpio, SwitchState state, User user, String ruleUser) {
		super(source);
		this.switchGpio = switchGpio;
		this.state = state;
		this.user = user;
		this.ruleUser = ruleUser;
	}
	

	public SwitchGPIO getSwitchGpio() {
		return switchGpio;
	}


	public void setSwitchGpio(SwitchGPIO switchGpio) {
		this.switchGpio = switchGpio;
	}


	public SwitchState getState() {
		return state;
	}
	public void setState(SwitchState state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getRuleUser() {
		return ruleUser;
	}

	public void setRuleUser(String ruleUser) {
		this.ruleUser = ruleUser;
	}
	
	

}
