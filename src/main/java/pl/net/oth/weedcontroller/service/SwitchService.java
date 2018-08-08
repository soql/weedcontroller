package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.event.ChangeSwitchGpioStateEvent;
import pl.net.oth.weedcontroller.event.ChangeSwitchStateEvent;
import pl.net.oth.weedcontroller.external.ExternalSwitchDispatcher;
import pl.net.oth.weedcontroller.external.SwitchController;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.SwitchGpioLog;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.model.dto.PowerUsageDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchConfigurationDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchGpioDTO;
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
	private SwitchLogDAO switchLogDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ExternalSwitchDispatcher externalSwitchDispatcher;
	
	@Autowired
	private ConfigurationService configurationService;
	
	
	@Transactional
	public void add(Switch s) {
		switchDAO.persist(s);
	}
	public List<SwitchGPIO> getAllManagedSwitches(){
		return switchDAO.getAllManagedSwitches();
	}
	public List<Switch> getAllSwitches(){
		return switchDAO.getAllSwitches();
	}
	
	public List<SwitchDTO> getAllSwitchesWithStates(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			SwitchDTO switchDTO=new SwitchDTO();			
			switchDTO.setName(switch_.getName());
			switchDTO.setState(getStateFromExternalController(switch_));
			result.add(switchDTO);
		}
		return result;
	}
	
	public List<SwitchDTO> getManagedSwitches(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			if(switch_.getGpios().size()>1) {
				SwitchDTO switchDTO=new SwitchDTO();			
				switchDTO.setName(switch_.getName());
				switchDTO.setState(getStateFromExternalController(switch_));
				switchDTO.setGpio(new ArrayList<SwitchGpioDTO>());
				for(SwitchGPIO switchGPIO : switch_.getGpios()) {
					SwitchGpioDTO switchGpioDTO=new SwitchGpioDTO();
					switchGpioDTO.setActive(switchGPIO.isActive());
					switchGpioDTO.setName(switchGPIO.getDescription());
					switchGpioDTO.setGpioNumber(switchGPIO.getGpioNumber());
					switchDTO.getGpio().add(switchGpioDTO);
				}
				result.add(switchDTO);	
			}
			
		}
		return result;
	}
	
	private SwitchState getStateFromExternalController(Switch switch_) {
		return externalSwitchDispatcher.getStateFromExternalController(switch_);
	}

	
	
	private SwitchState getLastState(Switch switch_) {
		return switchDAO.getLastState(switch_);
	}

	public Switch getSwitchByName(String name){
		return switchDAO.getSwitchByName(name);
	}
	
	public SwitchGPIO getManagedSwitchByName(String name){
		return switchDAO.getManagedSwitchByName(name);
	}
	
	public SwitchState getStateByName(String name){
		Switch switch1=getSwitchByName(name);
		return getStateFromExternalController(switch1);		
	}
		
	public List<Integer> getSwitchesConfiguration(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<Integer> result=new ArrayList<Integer>();
		for (Switch switch_ : switches) {	
			for(SwitchGPIO switchGPIO: switch_.getGpios()) {
				result.add(switchGPIO.getId());
			}
		}
		return result;
	}
	
	public Boolean setSwitchState(String switchName, SwitchState state){		
		LOGGER.info("Rzadanie zmiany przel. nr "+switchName+" na "+state+" przez uzytkownika "+getUser().getFullName());
		Switch switch_=switchDAO.getSwitchByName(switchName);			
		Boolean result=setStateToExternalController(switch_, state);
		if(result.booleanValue()) {
			publishSwitchStateEvent(switch_, state, getUser(), null);
		}
		return result;
	}
	
	
	public Boolean setSwitchState(Switch switch_, SwitchState state, String ruleUser){		
		LOGGER.info("Rzadanie zmiany przel. nr "+switch_.getName()+" na "+state+" przez rolę  "+ruleUser);							
		Boolean result=setStateToExternalController(switch_, state);		
		if(result.booleanValue()) {
			publishSwitchStateEvent(switch_, state, null, ruleUser);
		}
		return result;
	}
	public Boolean setStateToExternalController(Switch switch_, SwitchState state) {
		return externalSwitchDispatcher.setStateToExternalController(switch_, state);
	}
	
	private void publishSwitchStateEvent(Switch switch_, SwitchState state, User user, String ruleUser) {		
		ChangeSwitchStateEvent csse=new ChangeSwitchStateEvent(this, switch_, state, user, ruleUser);
		applicationEventPublisher.publishEvent(csse);
	}

	private void publishSwitchGpioStateEvent(SwitchGPIO switchGpio_, SwitchState state, User user, String ruleUser) {		
		ChangeSwitchGpioStateEvent csse=new ChangeSwitchGpioStateEvent(this, switchGpio_, state, user, ruleUser);
		applicationEventPublisher.publishEvent(csse);
	}
	private User getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); 
		return userDAO.findByLogin(name);		
	}
	
	public List<SwitchLogDTO> getLogs(int number, List<String> switches){
		List<SwitchLog> switchLogs=switchLogDAO.getSwitchLog(number, switches);
		List<SwitchGpioLog> switchGpioLogs=switchLogDAO.getSwitchGpioLog(number, switches);
		
		List<SwitchLogDTO> result=new ArrayList<SwitchLogDTO>();
		for (SwitchLog switchLog : switchLogs) {
			String userName=switchLog.getUser()!=null?switchLog.getUser().getFullName():switchLog.getRuleUser();
			result.add(new SwitchLogDTO(userName, switchLog.getSwitch_().getName(), switchLog.getState(), switchLog.getDate(), SwitchLogDTO.LOG_SWITCH, null));
		}
		for (SwitchGpioLog switchLog : switchGpioLogs) {
			String userName=switchLog.getUser()!=null?switchLog.getUser().getFullName():switchLog.getRuleUser();
			String descr=switchLog.getSwitchGpio().getParent().getName();
			result.add(new SwitchLogDTO(userName, descr, switchLog.getState(), switchLog.getDate(), SwitchLogDTO.LOG_SWITCH_GPIO, switchLog.getSwitchGpio().getDescription()));
		}
		Collections.sort(result,new Comparator<SwitchLogDTO>() {
			@Override
			public int compare(SwitchLogDTO o1, SwitchLogDTO o2) {				
				return (int)(o2.getRealDate().getTime()-o1.getRealDate().getTime());
			}
		});		
		return result.subList(0, result.size()>number?number:result.size());
	}
	public List<SwitchLog> getLogsForDate(Switch switch_, Date dateFrom, Date DateTo){
		return switchLogDAO.getLogsForDate(switch_, dateFrom, DateTo);
	}
	public List<SwitchGpioLog> getManagedSwitchLogsForDate(SwitchGPIO switch_, Date dateFrom, Date DateTo){
		return switchLogDAO.getManagedSwitchLogsForDate(switch_, dateFrom, DateTo);
	}
	public String getLastSwitchStateChangeUser(String switchName, SwitchState state) {
		Switch switch_=getSwitchByName(switchName);
		return switchDAO.getLastSwitchStateChangeUser(switch_, state);
	}

	public int getLastSwitchStateChangeTime(String switchName) {
		Switch switch_=getSwitchByName(switchName);
		Date lastDate=switchDAO.getLastSwitchStateChangeTime(switch_);
		int time=(int) ((new Date().getTime()-lastDate.getTime())/1000/60);
		LOGGER.debug("Czas od ost. zmiany statusu "+switchName+" = "+time);
		return time;
	}
	
	public int getLastManagedSwitchStateChangeTime(String switchName) {
		SwitchGPIO switchGpio=getManagedSwitchByName(switchName);
		Date lastDate=switchDAO.getLastManagedSwitchStateChangeTime(switchGpio);
		int time=(int) ((new Date().getTime()-lastDate.getTime())/1000/60);
		LOGGER.debug("Czas od ost. zmiany statusu (managed switch) "+switchName+" = "+time);
		return time;
	}
	
	

	public void mergeStates() {
		externalSwitchDispatcher.mergeStates();		
	}
	public Boolean setManagedSwitchState(Integer gpioNumber, Boolean active, String ruleUser){
		SwitchState switchState=active?SwitchState.ON:SwitchState.OFF;	
		SwitchGPIO switchGPIO=switchDAO.getSwitchGpioByNumber(gpioNumber);
		if(active.booleanValue()==switchGPIO.isActive()) {			
			return false;
		}
		LOGGER.info("Rzadanie zmiany przel. GPIO nr "+gpioNumber+" na "+switchState+" przez uzytkownika "+ruleUser);
		
		publishSwitchGpioStateEvent(switchGPIO, switchState, null, ruleUser);
		switchDAO.updateGpioActive(gpioNumber, active.booleanValue());		
		mergeStates();
		return true;
	}
	public Boolean setManagedSwitchState(Integer gpioNumber, Boolean active) {		
		SwitchState switchState=active?SwitchState.ON:SwitchState.OFF;
		SwitchGPIO switchGPIO=switchDAO.getSwitchGpioByNumber(gpioNumber);
		if(active.booleanValue()==switchGPIO.isActive()) {			
			return false;
		}
		LOGGER.info("Rzadanie zmiany przel. GPIO nr "+gpioNumber+" na "+switchState+" przez uzytkownika "+getUser().getFullName());
		
		publishSwitchGpioStateEvent(switchGPIO, switchState, getUser(), null);
		switchDAO.updateGpioActive(gpioNumber, active.booleanValue());		
		externalSwitchDispatcher.mergeStates();
		return true;
	}

	public List<SwitchConfigurationDTO> getSwitchesCSSConfiguration() {
		List<Switch> switches = switchDAO.getAllSwitches();
		List<SwitchConfigurationDTO> result=new ArrayList<>();
		for(Switch switch1:switches) {
			SwitchConfigurationDTO switchConfiguration=new SwitchConfigurationDTO();
			switchConfiguration.setName(switch1.getName());
			switchConfiguration.setColor(switch1.getColor());
			switchConfiguration.setActiveLog(true);;
			result.add(switchConfiguration);
		}
		return result;
	}
	public List<SwitchGPIO> getAllMqttSwitches() {
		return switchDAO.getAllMqttSwitches();
	}
	public SwitchGPIO getSwitchByMQTTTopic(String topic) {
		return switchDAO.getSwitchByMQTTTopic(topic);
	}
	

	
	
		
}
