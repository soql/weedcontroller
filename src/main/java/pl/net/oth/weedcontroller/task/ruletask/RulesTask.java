package pl.net.oth.weedcontroller.task.ruletask;

import java.text.SimpleDateFormat;
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
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Configuration
@EnableScheduling
public class RulesTask {
	private final static Log LOGGER=LogFactory.getLog(RulesTask.class);
	
	private GroovyShell gs;
		
	@Autowired
	private SensorTask sensorTask;
		
	@Autowired
	private RuleService ruleService;	
	
	@Autowired
	private Command command;
	
	private Date lastRuleTime=null;
	
	private Date nowRuleTime=null;
	
	private Integer actualRuleId=null;
		
	private static final SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	@PostConstruct
	public void init() {
		gs=new GroovyShell();
		gs.setVariable("r", command);
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
				e.printStackTrace();
				continue;
			}
			LOGGER.info("Wynik reguły "+rule.getId()+" = "+condition);
		}
		lastRuleTime=nowRuleTime;
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
}
