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
import pl.net.oth.weedcontroller.helpers.GroovyHelper;
import pl.net.oth.weedcontroller.helpers.PinHelper;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Switch;
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
	private SensorResultService sensorResultService;
	
	@Autowired
	private RuleService ruleService;
	
	private Date lastRuleTime=null;
	
	private Date nowRuleTime=null;
	
	@PostConstruct
	public void init() {
		gs=new GroovyShell();
		gs.setVariable("r", this);
		gs.setVariable("ON", SwitchState.ON);
		gs.setVariable("OFF", SwitchState.OFF);		
	}
	
	@Scheduled(fixedDelay = 30000)
	private void checkAndExecuteRules() {
		LOGGER.info("Weryfikacja taskow "+lastRuleTime);
		nowRuleTime=new Date();		
		List<Rule> rules=ruleService.getAllActiveRules();
		for (Rule rule : rules) {
			LOGGER.info("Weryfikacja warunków reguły nr "+rule.getId());
			Boolean condition=null;
			try{
				gs.setVariable("un", "REG:"+rule.getId());
				condition=(Boolean) gs.evaluate(rule.getCondition_());
				if(condition.booleanValue()){
					LOGGER.info("Reguła "+rule.getId()+" spełniona - wykonuję rządanie.");
					gs.evaluate(rule.getExpression_());
				}
			}catch(Exception e){
				LOGGER.error("Błąd podczas weryfikacji/wykonania warunku reguly nr "+rule.getId());
				e.printStackTrace();
				continue;
			}
			LOGGER.info("Wynik reguły "+rule.getId()+" = "+condition);
		}
		lastRuleTime=nowRuleTime;
	}
	
	private void groovy(){
		
		Object c=gs.evaluate("gv.cos(a)");
	}
	
	public SwitchState css(String switchName){
		return checkSwitchState(switchName);
	}
			
	public SwitchState checkSwitchState(String switchName){
		return switchService.getStateByName(switchName);		
	}
	public boolean sss(String switchName, SwitchState targetState, String userName){			
		return setSwitchState(switchName, targetState, userName);		
	}	
	public boolean setSwitchState(String switchName, SwitchState targetState, String userName){
		Switch s=switchService.getSwitchByName(switchName);		
		System.out.println("TUUUU: "+userName);
		return switchService.setSwitchState(s.getGpioNumber(), targetState, userName);		
	}	
	public boolean cron(String secounds, String minutes, String hours, String dayOfMonth, String month, String dayOfWeek){
		Calendar prevCalendar=GregorianCalendar.getInstance();
		prevCalendar.setTime(lastRuleTime);
		int[] previousRead=new int[]{	prevCalendar.get(Calendar.SECOND),
										prevCalendar.get(Calendar.MINUTE),
										prevCalendar.get(Calendar.HOUR_OF_DAY),
										prevCalendar.get(Calendar.DAY_OF_MONTH),
										prevCalendar.get(Calendar.MONTH)+1,
										prevCalendar.get(Calendar.DAY_OF_WEEK),
										};
		for (int i : previousRead) {
			System.out.println(i);
		}
		return true;
	}
	public static void main(String[] args) {
		new RulesTask();
	}
	public RulesTask() {
		lastRuleTime=new Date();
		cron("*","*","*","*","*","*");
	}
	
}
