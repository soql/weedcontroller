package pl.net.oth.weedcontroller.task.ruletask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.SMSMessage;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;

/**
 * @author psokolowski
 *
 */
@Configuration
@EnableScheduling
public class RulesTask {
	private final static Log LOGGER=LogFactory.getLog(RulesTask.class);
	
	@Autowired
	private SwitchService switchService;	
		
	@Autowired
	private SensorTask sensorTask;
		
	@Autowired
	private RuleService ruleService;	
	
	@Autowired
	private Command command;
	
	private Date lastRuleTime=null;
	
	private Date nowRuleTime=null;
	
	private Integer actualRuleId=null;	
	
	private Map<String, SwitchState> nowSwitchStates=null;
	
	private Map<String, SwitchState> lastSwitchStates=null;
		
	private static final SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	@PostConstruct
	public void init() {
					
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
		GroovyShell gs=new GroovyShell();
		fillGroovyShell(gs);
		
		nowSwitchStates=buildSwitchStateMap();		
		
		LOGGER.info("Weryfikacja taskow. Poprzedni czas: "+sdf.format(lastRuleTime)+" Aktualny czas: "+sdf.format(nowRuleTime));
				
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
				LOGGER.error(Helper.STACK_TRACE,e);				
				continue;
			}
			LOGGER.info("Wynik reguły "+rule.getId()+" = "+condition);
		}
		lastRuleTime=nowRuleTime;
		lastSwitchStates=nowSwitchStates;
	}	
	
	private void fillGroovyShell(GroovyShell gs) {
		gs.setVariable("r", command);
		gs.setVariable("ON", SwitchState.ON);
		gs.setVariable("OFF", SwitchState.OFF);
		gs.setVariable("TEMP", sensorTask.getLastSuccesfullSensorResult().getTemperature());
		gs.setVariable("HUMI", sensorTask.getLastSuccesfullSensorResult().getHumidity());				
	}

	public void handleSMS(SMSMessage message) {
		LOGGER.info("Odpalono handleSMS dla "+message.getPhoneNumber()+" / "+message.getText());
		List<Rule> rules=ruleService.getAllActiveSMSRules();
		GroovyShell gs=new GroovyShell();
		fillGroovyShell(gs);
		gs.setVariable("PHONE", message.getPhoneNumber());
		for (Rule rule : rules) {
			Pattern p=Pattern.compile(rule.getCondition_());
			Matcher m=p.matcher(message.getText().toUpperCase());
			if(m.matches()){
				try{
					gs.evaluate(rule.getExpression_());
					return;
				}catch(Exception e){
					LOGGER.error("Błąd podczas weryfikacji/wykonania warunku reguly SMS nr "+rule.getId());
					LOGGER.error(Helper.STACK_TRACE,e);				
					continue;
				}					
			}
			command.sendSMS("Nie rozpoznano komendy.", message.getPhoneNumber());
		}
		
	}
	private Map<String, SwitchState> buildSwitchStateMap() {
		Map<String, SwitchState> result=new HashMap<String, SwitchState>();
		List<SwitchDTO> switches=switchService.getAllSwitchesWithStates();
		for (SwitchDTO switchDTO : switches) {
			result.put(switchDTO.getName(), switchDTO.getState());
		}
		return result;
	}

	public Integer getActualRuleId() {
		return actualRuleId;
	}	
	
	public Date getLastRuleTime() {
		return lastRuleTime;
	}	
	
	public Date getNowRuleTime() {
		return nowRuleTime;
	}

	public Map<String, SwitchState> getNowSwitchStates() {
		return nowSwitchStates;
	}

	public Map<String, SwitchState> getLastSwitchStates() {
		return lastSwitchStates;
	}

	
	
}
