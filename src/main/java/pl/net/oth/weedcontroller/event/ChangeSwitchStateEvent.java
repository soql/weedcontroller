package pl.net.oth.weedcontroller.event;

import org.springframework.context.ApplicationEvent;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;

public class ChangeSwitchStateEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Switch switch_;
	private SwitchState state;
	private User user;	
	private String ruleUser;
	
	public ChangeSwitchStateEvent(Object source, Switch switch_, SwitchState state, User user, String ruleUser) {
		super(source);
		this.switch_ = switch_;
		this.state = state;
		this.user = user;
		this.ruleUser = ruleUser;
	}
	
	public Switch getSwitch() {
		return switch_;
	}

	public void setSwitch(Switch switch_) {
		this.switch_ = switch_;
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
