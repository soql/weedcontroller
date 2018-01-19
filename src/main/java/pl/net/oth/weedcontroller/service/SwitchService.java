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
import pl.net.oth.weedcontroller.external.GpioExternalController;
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

	private static final String ONE_WAT_COST = "ONE_WAT_COST";
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	
	@Autowired
	private SwitchDAO switchDAO;

	@Autowired
	private SwitchLogDAO switchLogDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private GpioExternalController gpioExternalController;
	
	@Autowired
	private ConfigurationService configurationService;
	
	
	@Transactional
	public void add(Switch s) {
		switchDAO.persist(s);
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
		for(SwitchGPIO switchGPIO: switch_.getGpios()){
			if(switchGPIO.isActive()) {
				return gpioExternalController.getState(switchGPIO.getGpioNumber(), switch_.getRevert().booleanValue());
			}
		}
		LOGGER.error("Brak aktywnego GPIO dla "+switch_.getName());
		return SwitchState.OFF;
	}

	public List<SwitchDTO> getAllSwitchesWithLastStates(){
		List<Switch> switches=switchDAO.getAllSwitches();
		List<SwitchDTO> result=new ArrayList<SwitchDTO>();
		for (Switch switch_ : switches) {
			SwitchDTO switchDTO=new SwitchDTO();	
			switchDTO.setName(switch_.getName());
			switchDTO.setIsRevert(switch_.getRevert());
			switchDTO.setState(getLastState(switch_));
			switchDTO.setGpio(new ArrayList<SwitchGpioDTO>());
			for(SwitchGPIO switchGPIO : switch_.getGpios()) {
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
	
	private SwitchState getLastState(Switch switch_) {
		return switchDAO.getLastState(switch_);
	}

	public Switch getSwitchByName(String name){
		return switchDAO.getSwitchByName(name);
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
				result.add(switchGPIO.getGpioNumber());
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
		boolean result=false;
		for(SwitchGPIO switchGPIO: switch_.getGpios()) {
			if(switchGPIO.isActive()) {
				result=gpioExternalController.setState(switchGPIO.getGpioNumber(), state, switch_.getRevert().booleanValue());
			}else {
				result=gpioExternalController.setState(switchGPIO.getGpioNumber(), SwitchState.OFF, switch_.getRevert().booleanValue());
			}
		}
		return result;
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
	
	public List<PowerUsageDTO> calculatePowerUsage(final Long dateFrom, final Long dateTo ) {
		List<PowerUsageDTO> resultsDTO=new ArrayList<PowerUsageDTO>();
		List<Switch> allSwitches=getAllSwitches();
		Double oneWatCost=getOneWatCost();		
		for (Switch switch_ : allSwitches) {
			PowerUsageDTO powerUsageDTO=new PowerUsageDTO();
			powerUsageDTO.setSwitchName(switch_.getName());
			powerUsageDTO.setMaxTime(Helper.milisecondsToHours(dateTo-dateFrom));
			powerUsageDTO.setPowerUsage(switch_.getPowerUsage());
			LOGGER.debug("Przelacznik "+switch_.getName());
			List<SwitchLog> results=getLogsForDate(switch_, new Date(dateFrom), new Date(dateTo));
			long milisecoundsOn=0;
			SwitchLog lastState=null;
			SwitchLog prevState=null;
			for (SwitchLog switchLog : results) {	
				prevState=lastState;
				if(lastState!=null) {
					LOGGER.debug("Resultat: "+lastState.getState()+" od "+lastState.getDate()+" do "+switchLog.getDate());
					if(lastState.getState().equals(SwitchState.ON)) {
						long timeInMilisecounds=(switchLog.getDate().getTime()-lastState.getDate().getTime());
						milisecoundsOn+=timeInMilisecounds;
						LOGGER.debug("Dodaję do "+switch_.getName()+" czas "+timeInMilisecounds+"("+Helper.milisecondsToHours(timeInMilisecounds)+ "h) za okres "+lastState.getDate()+" do "+switchLog.getDate());
					}
				}				
				lastState=switchLog;
			}	
			if(lastState.getState().equals(SwitchState.ON) && lastState.getDate().before(new Date(dateTo))) {
				long timeInMilisecounds=(new Date(dateTo).getTime()-lastState.getDate().getTime());
				milisecoundsOn+=timeInMilisecounds;
				LOGGER.debug("Dodaję do "+switch_.getName()+" czas "+timeInMilisecounds+"("+Helper.milisecondsToHours(timeInMilisecounds)+ "h) za okres "+new Date(dateTo)+" do "+lastState.getDate());
			}
			powerUsageDTO.setPowerOnTime(Helper.milisecondsToHours(milisecoundsOn));
			powerUsageDTO.setCost(powerUsageDTO.getPowerOnTime()/1000*oneWatCost*powerUsageDTO.getPowerUsage());
			resultsDTO.add(powerUsageDTO);
		}
		
		return resultsDTO;
	}

	private Double getOneWatCost() {
		Configuration oneWatCost=configurationService.getByKey(ONE_WAT_COST);
		if(oneWatCost!=null) {			
			try {
				return Double.parseDouble(oneWatCost.getValue());
			} catch (NumberFormatException e) {
				return new Double(0);
			}
		
		}
		return new Double(0);
	}

	public void mergeGpioStates() {
		gpioExternalController.mergeGpioStates(getAllSwitchesWithLastStates());
	}
	public Boolean setManagedSwitchState(Integer gpioNumber, Boolean active) {		
		SwitchState switchState=active?SwitchState.ON:SwitchState.OFF;
	
		LOGGER.info("Rzadanie zmiany przel. GPIO nr "+gpioNumber+" na "+switchState+" przez uzytkownika "+getUser().getFullName());
		SwitchGPIO switchGPIO=switchDAO.getSwitchGpioByNumber(gpioNumber);
		publishSwitchGpioStateEvent(switchGPIO, switchState, getUser(), null);
		switchDAO.updateGpioActive(gpioNumber, active.booleanValue());		
		mergeGpioStates();
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
	
		
}
