package pl.net.oth.weedcontroller.task;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import groovy.lang.GroovyShell;
import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.impl.SMSController;
import pl.net.oth.weedcontroller.helpers.GroovyHelper;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Configuration
@EnableScheduling
public class RulesTask {
	private final static Log LOGGER=LogFactory.getLog(RulesTask.class);
	
	private GroovyShell gs;
	
	@Autowired
	private SwitchService switchService;
	
	@Autowired
	private SensorTask sensorTask;
		
	@Autowired
	private RuleService ruleService;
	
	@Autowired
	private SMSController smsController;
	
	@Autowired
	private UserDAO userDAO;
	
	private Date lastRuleTime=null;
	
	private Date nowRuleTime=null;
	
	private Integer actualRuleId=null;
		
	
	@PostConstruct
	public void init() {
		gs=new GroovyShell();
		gs.setVariable("r", this);
		gs.setVariable("ON", SwitchState.ON);
		gs.setVariable("OFF", SwitchState.OFF);			
	}
	
	@Scheduled(fixedDelay = 15000)
	private void checkAndExecuteRules() {
		nowRuleTime=new Date();
		if(sensorTask.getLastSuccesfullSensorResult()==null){
			LOGGER.info("Brak rezultatów z sensora - przetwarznie reguł wstrzymane");
			return;
		}
		if(lastRuleTime==null){
			LOGGER.info("Brak czasu poprzedniego wykonania - przetwarznie reguł wstrzymane");
			lastRuleTime=nowRuleTime;
		}
		gs.setVariable("TEMP", sensorTask.getLastSuccesfullSensorResult().getTemperature());
		gs.setVariable("HUMI", sensorTask.getLastSuccesfullSensorResult().getHumidity());
		LOGGER.info("Weryfikacja taskow "+lastRuleTime);
				
		List<Rule> rules=ruleService.getAllActiveRules();
		for (Rule rule : rules) {
			LOGGER.info("Weryfikacja warunków reguły nr "+rule.getId());
			Boolean condition=null;
			try{
				actualRuleId=rule.getId();
				condition=(Boolean) gs.evaluate(rule.getCondition_());
				if(condition.booleanValue()){
					LOGGER.info("Reguła "+rule.getId()+" spełniona - wykonuję rządanie.");
					gs.evaluate(rule.getExpression_());
				}
			}catch(Exception e){
				LOGGER.error("Błąd podczas weryfikacji/wykonania warunku reguly nr "+rule.getId());
				LOGGER.error(e);
				e.printStackTrace();
				continue;
			}
			LOGGER.info("Wynik reguły "+rule.getId()+" = "+condition);
		}
		lastRuleTime=nowRuleTime;
	}	
	
	public SwitchState css(String switchName){
		return checkSwitchState(switchName);
	}
			
	public SwitchState checkSwitchState(String switchName){
		return switchService.getStateByName(switchName);		
	}
	public boolean sss(String switchName, SwitchState targetState){			
		return setSwitchState(switchName, targetState, "REG:"+actualRuleId);		
	}	
	public boolean setSwitchState(String switchName, SwitchState targetState, String userName){
		Switch s=switchService.getSwitchByName(switchName);				
		return switchService.setSwitchState(s.getGpioNumber(), targetState, userName);		
	}	
	
	public boolean cron(String secounds, String minutes, String hours, String dayOfMonth, String month, String dayOfWeek){		
		Calendar prevCalendar=GregorianCalendar.getInstance();
		prevCalendar.setTime(lastRuleTime);
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
		prevCalendar.setTime(nowRuleTime);
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
			LOGGER.info("pr przeskoku:"+previousRead[i]+"   "+patern[i]+"     "+nowRead[i]);
			/*Obsługa przeskoków np sekundy z 56 na 4 (nowa minuta)*/
			if(nowRead[i]<previousRead[i]){
				nowRead[i]+=previousRead[i];
				if(patern[i]<previousRead[i])
					patern[i]+=previousRead[i];
			}
			LOGGER.info("po przeskoku:"+previousRead[i]+"   "+patern[i]+"     "+nowRead[i]);
			if(patern[i]>=previousRead[i] && patern[i]<=nowRead[i]){
				
			}else{
				return false;
			}
			
		}
		return true;
	}
	public void delayRule(int minutes){
		ruleService.setNextTimeExecution(actualRuleId, minutes);
	}
	public void sendSMS(String text){
		List<User> users=userDAO.getAllSMSUsers();
		for (User user : users) {
			smsController.sendSMS(user.getPhoneNumber(), text);
		}
	}
}
