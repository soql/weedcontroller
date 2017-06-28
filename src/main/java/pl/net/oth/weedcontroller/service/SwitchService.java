package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.event.ChangeSwitchStateEvent;
import pl.net.oth.weedcontroller.external.GpioExternalController;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchLogDTO;

@Component
@EnableTransactionManagement
public class SwitchService {	
	private final static Log LOGGER = LogFactory.getLog(SwitchService.class);
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	
	@Autowired
	private SwitchDAO switchDAO;

	@Autowired
	private SwitchLogDAO SwitchLogDAO;
	
	@Autowired
	private UserDAO userDAO;
	
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
			switchDTO.setState(gpioExternalController.getState(switch_.getGpioNumber(), switch_.getRevert().booleanValue()));
			result.add(switchDTO);
		}
		return result;
	}
	
	public List<SwitchDTO> getAllSwitchesWithLastStates(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			SwitchDTO switchDTO=new SwitchDTO();
			switchDTO.setGpioNumber(switch_.getGpioNumber());
			switchDTO.setName(switch_.getName());
			switchDTO.setState(getLastState(switch_.getGpioNumber()));
			result.add(switchDTO);
		}
		return result;
	}
	
	private SwitchState getLastState(int gpioNumber) {
		return switchDAO.getLastState(gpioNumber);
	}

	public Switch getSwitchByName(String name){
		return switchDAO.getSwitchByName(name);
	}
		
	public SwitchState getStateByName(String name){
		Switch switch1=getSwitchByName(name);
		return gpioExternalController.getState(switch1.getGpioNumber(), switch1.getRevert().booleanValue());		
	}
	
	public Switch getSwitchByNumber(Integer id){
		return switchDAO.getSwitchByNumber(id);
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
		LOGGER.info("Rzadanie zmiany przel. nr "+switchNumber+" na "+state+" przez uzytkownika "+getUser().getFullName());
		Switch switch_=switchDAO.getSwitchByNumber(switchNumber);
		publishEvent(switch_, state, getUser(), null);
		/*logSwitchChange(switchNumber,state);*/
		return gpioExternalController.setState(switchNumber.intValue(), state, switch_.getRevert().booleanValue());		
	}
	
	
	public Boolean setSwitchState(Integer switchNumber, SwitchState state, String ruleUser){		
		LOGGER.info("Rzadanie zmiany przel. nr "+switchNumber+" na "+state+" przez rolę  "+ruleUser);		
		Switch switch_=switchDAO.getSwitchByNumber(switchNumber);
		publishEvent(switch_, state, null, ruleUser);
		/*logSwitchChange(switchNumber,state);*/
		return gpioExternalController.setState(switchNumber.intValue(), state, switch_.getRevert().booleanValue());		
	}
	
	
	private void publishEvent(Switch switch_, SwitchState state, User user, String ruleUser) {		
		ChangeSwitchStateEvent csse=new ChangeSwitchStateEvent(this, switch_, state, user, ruleUser);
		applicationEventPublisher.publishEvent(csse);
	}


	private User getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); 
		return userDAO.findByLogin(name);		
	}
	
	public List<SwitchLogDTO> getLogs(int number){
		List<SwitchLog> switchLogs=SwitchLogDAO.getSwitchLog(number);
		List<SwitchLogDTO> result=new ArrayList<SwitchLogDTO>();
		for (SwitchLog switchLog : switchLogs) {
			String userName=switchLog.getUser()!=null?switchLog.getUser().getFullName():switchLog.getRuleUser();
			result.add(new SwitchLogDTO(userName, switchLog.getSwitch_().getName(), switchLog.getState(), switchLog.getDate()));
		}
		return result;
	}

	public String getLastSwitchStateChangeUser(String switchName, SwitchState state) {
		Switch switch_=getSwitchByName(switchName);
		return switchDAO.getLastSwitchStateChangeUser(switch_, state);
	}

	public int getLastSwitchStateChangeTime(String switchName) {
		Switch switch_=getSwitchByName(switchName);
		Date lastDate=switchDAO.getLastSwitchStateChangeTime(switch_.getGpioNumber());
		int time=(int) ((new Date().getTime()-lastDate.getTime())/1000/60);
		LOGGER.debug("Czas od ost. zmiany statusu "+switchName+" = "+time);
		return time;
	}
	
}
