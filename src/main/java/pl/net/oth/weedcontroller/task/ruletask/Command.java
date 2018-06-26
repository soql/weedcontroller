package pl.net.oth.weedcontroller.task.ruletask;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.impl.SMSController;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.ChangeDetection;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.service.ChangeDetectionService;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.PingTask;

@Configuration
public class Command {
	private final static Log LOGGER=LogFactory.getLog(Command.class);	
	
	@Autowired
	private SMSController smsController;
	
	@Autowired
	private RulesTask rulesTask;
	
	@Autowired
	private UserDAO userDAO;	
	
	@Autowired
	private RuleService ruleService;	
	
	@Autowired
	private SwitchService switchService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private SensorResultService sensorResultService;
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ChangeDetectionService changeDetectionService;
	
	public long gCaL(String key){
		return getConfigurationAsLong(key);
	}
	
	public long getConfigurationAsLong(String key){
		return (Long.parseLong(configurationService.getByKey(key).getValue()));	
	}
	
	public SwitchState css(String switchName){
		return checkSwitchState(switchName);
	}
		
			
	public SwitchState checkSwitchState(String switchName){
		return switchService.getStateByName(switchName);		
	}
	
	public boolean sss(String switchName, String targetState){			
		return setSwitchState(switchName, targetState, rulesTask.getActualRuleLogin());		
	}	
	
	public boolean setSwitchState(String switchName, String targetState, String userName){
		Switch s=switchService.getSwitchByName(switchName);		
		return switchService.setSwitchState(s, SwitchState.valueOf(targetState), userName);		
	}	
	
	public boolean setManagedSwitchState(String switchName, String targetState, String userName) {
		SwitchGPIO s=switchService.getManagedSwitchByName(switchName);
		return switchService.setManagedSwitchState(new Integer(s.getGpioNumber()),  targetState.equals(SwitchState.ON)?Boolean.TRUE:Boolean.FALSE);
	}
	
	public boolean smss(String switchName, String targetState, String userName) {
		return setManagedSwitchState(switchName, targetState, userName);
	}
	
	public boolean csnc(String switchName, SwitchState state){
		return checkSwitchNowChange(switchName, state);
	}
	
	public String glsscu(String switchName, SwitchState state){
		return getLastSwitchStateChangeUser(switchName, state);
	}
	
	public String getLastSwitchStateChangeUser(String switchName, SwitchState state){
		return switchService.getLastSwitchStateChangeUser(switchName, state);
	}
	public int glssct(String switchName){
		return getLastSwitchStateChangeTime(switchName);
	}
	
	public int getLastSwitchStateChangeTime(String switchName){
		return switchService.getLastSwitchStateChangeTime(switchName);
	}
	public int getLastManagedSwitchStateChangeTime(String switchName){
		return switchService.getLastManagedSwitchStateChangeTime(switchName);
	}
	
	public float gav(String type, String func, Integer minutes){
		return getAggregatedValue(type, func, minutes).floatValue();
	}
	
	public String gavAS(String type, String func, Integer minutes){
		return getAggregatedValueAsString(type, func, minutes);
	}
	
	public long getLastInternetConnection() {
		long last=getConfigurationAsLong(PingTask.LAST_INTERNET_CONNECTION);
		long timeInMinutes=(last-new Date().getTime())/1000/60;
		return timeInMinutes; 
	}
	
	public long getLIC() {		
		return getLastInternetConnection(); 
	}
	
	public Float getAggregatedValue(String type, String func, Integer minutes){
		if(type.equals("TEMP")){
			type="temperature";
		}else if(type.equals("HUMI")){
			type="humidity";
		}else{
			LOGGER.error("Brak sensora dla "+type);
			return null;
		}
		Date dateFrom=new Date(new Date().getTime()-(1000*60*minutes.intValue()));
		Date dateTo=new Date();
		Float result=sensorResultService.getAggregatedValue(type, func,dateFrom, dateTo, sensorService.getSensorByNumber(1));
		LOGGER.debug("getAggregatedValue ("+type+","+func+","+minutes.intValue()+"): "+result);
		return result;
	}
	public String getAggregatedValueAsString(String type, String func, Integer minutes){
		return String.valueOf(new DecimalFormat("0.00##").format(getAggregatedValue(type, func, minutes)));
	}
	

	
	public boolean checkSwitchNowChange(String switchName, SwitchState state){
		if(rulesTask.getLastSwitchStates()==null)
			return false;
		if(rulesTask.getNowSwitchStates().get(switchName).equals(state) && !rulesTask.getLastSwitchStates().get(switchName).equals(state)){
			return true;
		}
		return false;
	}
	
	public int getNumberOfDays(){
		pl.net.oth.weedcontroller.model.Configuration configuration=configurationService.getByKey("START_DATE");
		if(configuration==null){
			return 0;
		}
		try {
			Date startDate=Helper.START_DATE_FORMAT.parse(configuration.getValue());
			return (int) ((new Date().getTime()-startDate.getTime())/1000/60/60/24);
		} catch (ParseException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
			return 0;
		}			
	}
	
	public boolean cron(String secounds, String minutes, String hours, String dayOfMonth, String month, String dayOfWeek){		
		Calendar prevCalendar=GregorianCalendar.getInstance();
		prevCalendar.setTime(rulesTask.getLastRuleTime());
		int[] patern=new int[]{
				secounds.equals("*")?-1:Integer.parseInt(secounds),
				minutes.equals("*")?-1:Integer.parseInt(minutes),
				hours.equals("*")?-1:Integer.parseInt(hours), 
				dayOfMonth.equals("*")?-1:Integer.parseInt(dayOfMonth),
				month.equals("*")?-1:Integer.parseInt(month),
				dayOfWeek.equals("*")?-1:Integer.parseInt(dayOfWeek)	
		};
		int[] previousRead=new int[]{	prevCalendar.get(Calendar.SECOND),
										prevCalendar.get(Calendar.MINUTE),
										prevCalendar.get(Calendar.HOUR_OF_DAY),
										prevCalendar.get(Calendar.DAY_OF_MONTH),
										prevCalendar.get(Calendar.MONTH)+1,
										prevCalendar.get(Calendar.DAY_OF_WEEK),
										};
		prevCalendar.setTime(rulesTask.getNowRuleTime());
		int[] nowRead=new int[]{		prevCalendar.get(Calendar.SECOND),
										prevCalendar.get(Calendar.MINUTE),
										prevCalendar.get(Calendar.HOUR_OF_DAY),
										prevCalendar.get(Calendar.DAY_OF_MONTH),
										prevCalendar.get(Calendar.MONTH)+1,
										prevCalendar.get(Calendar.DAY_OF_WEEK),
										};
		
		for (int i=0; i < patern.length; i++) {
			if(patern[i]==-1)
				continue;
			LOGGER.debug("pr przeskoku:	"+previousRead[i]+"   "+patern[i]+"     "+nowRead[i]);
			/*Obsługa przeskoków np sekundy z 56 na 4 (nowa minuta)*/
			if(nowRead[i]<previousRead[i]){
				nowRead[i]+=previousRead[i];
				if(patern[i]<previousRead[i])
					patern[i]+=previousRead[i];
			}
			LOGGER.debug("po przeskoku:	"+previousRead[i]+"   "+patern[i]+"     "+nowRead[i]);
			if((patern[i]>=previousRead[i] && patern[i]<nowRead[i]) || (patern[i]==nowRead[i] && patern[i]==previousRead[i])){
				
			}else{
				return false;
			}
			
		}
		return true;
	}
	
	public void delayRule(int minutes){
		ruleService.setNextTimeExecution(rulesTask.getActualRuleId(), minutes);
	}
	
	public void sendSMS(String text){
		List<User> users=userDAO.getAllSMSUsers();
		for (User user : users) {
			smsController.sendSMS(user.getPhoneNumber(), text);
		}
	}
	public void sendSMS(String text, String ...phoneNumbers){		
		for (String phoneNumber : phoneNumbers) {
			smsController.sendSMS(phoneNumber, text);
		}
	}
	public void setChangeDetectionNotif(ChangeDetection changeDetection) {
		changeDetectionService.updateNotification(changeDetection);
	}
	public void sCDN(ChangeDetection changeDetection) {
		setChangeDetectionNotif(changeDetection);
	}
}
