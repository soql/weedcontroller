package pl.net.oth.weedcontroller.task.ruletask;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioFormat.Encoding;

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
import pl.net.oth.weedcontroller.model.Phase;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.SMSMessage;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.service.ChangeDetectionService;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.PhaseService;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.service.UserService;
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
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private PhaseService phaseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChangeDetectionService changeDetectionService;
	
	private Date lastRuleTime=null;
	
	private Date nowRuleTime=null;
	
	private Integer actualRuleId=null;	
	
	private String actualRuleLogin=null;
	
	private Map<String, SwitchState> nowSwitchStates=null;
	
	private Map<String, SwitchState> lastSwitchStates=null;
	
	private Map<Integer, SensorResultDTO> nowSensorStates=null;
	
	private Map<Integer, SensorResultDTO> lastSensorStates=null;
		
	private Integer lastPhase=null;
	
	private Integer nowPhase=null;
	
	private static final SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	@PostConstruct
	public void init() {
					
	}
	
	@Scheduled(fixedDelay = 3000)
	private void checkAndExecuteRules() {
		switchService.mergeGpioStates();
		nowRuleTime=new Date();
		pl.net.oth.weedcontroller.model.Configuration nowPhaseConf=configurationService.getByKey(ConfigurationService.ACTUAL_PHASE);
		if(nowPhaseConf==null) {
			LOGGER.error("Brak zdefiniowanej fazy - przetwarznie reguł wstrzymane");			
			return;
		}
		nowPhase=Integer.parseInt(nowPhaseConf.getValue());
		nowSensorStates=buildSensorStateMap();
		
		if(sensorTask.getLastSuccesfullSensorResult()==null){
			LOGGER.info("Brak rezultatów z sensora - przetwarznie reguł wstrzymane");
			return;
		}
		
		if(lastRuleTime==null){
			LOGGER.info("Brak czasu poprzedniego wykonania - przetwarznie reguł wstrzymane");
			lastRuleTime=nowRuleTime;
			return;
		}
		if(lastPhase==null) {
			LOGGER.info("Brak poprzedniej fazy - przetwarznie reguł wstrzymane");
			lastPhase=nowPhase;
			return;			
		}			
		
		if(lastSensorStates==null) {
			LOGGER.info("Brak poprzednich stanów sensora - przetwarznie reguł wstrzymane");
			lastSensorStates=nowSensorStates;
			return;
		}
		GroovyShell gs=new GroovyShell();
		
		nowSwitchStates=buildSwitchStateMap();		
		
		
		fillGroovyShell(gs);
		
		
		
		LOGGER.info("Weryfikacja taskow. Poprzedni czas: "+sdf.format(lastRuleTime)+" Aktualny czas: "+sdf.format(nowRuleTime));
				
		List<Rule> rules=ruleService.getAllActiveRulesByPhase(nowPhase);
		for (Rule rule : rules) {
			LOGGER.info("Weryfikacja warunków reguły nr "+rule.getId());
			Boolean condition=null;
			try{
				actualRuleId=rule.getId();
				actualRuleLogin="REG:"+rule.getId();
				
				condition=(Boolean) gs.evaluate(new StringReader(rule.getCondition_()));
				if(condition.booleanValue()){
					LOGGER.info("Reguła "+rule.getId()+" spełniona - wykonuję rządanie.");
					gs.evaluate(new StringReader(rule.getExpression_()));
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
		lastPhase=nowPhase;
		lastSensorStates=nowSensorStates;
	}	
	
	private void fillGroovyShell(GroovyShell gs) {
		gs.setVariable("r", command);
		gs.setVariable("ON", SwitchState.ON);
		gs.setVariable("OFF", SwitchState.OFF);
		gs.setVariable("TEMP", sensorTask.getLastSuccesfullSensorResult().get(1).getResults().get(SensorResultDTO.TEMPERATURE).getResult());
		gs.setVariable("HUMI", sensorTask.getLastSuccesfullSensorResult().get(1).getResults().get(SensorResultDTO.HUMIDITY).getResult());
		/*gs.setVariable("POWER_STATE", sensorTask.getLastSuccesfullSensorResult().get(5).getResults().get(SensorResultDTO.POWER).getResult());*/
		if(sensorTask.getLastSuccesfullSensorResult().size()>1) {
			gs.setVariable("TEMP_Z", sensorTask.getLastSuccesfullSensorResult().get(2).getResults().get(SensorResultDTO.TEMPERATURE).getResult());
			gs.setVariable("HUMI_Z", sensorTask.getLastSuccesfullSensorResult().get(2).getResults().get(SensorResultDTO.HUMIDITY).getResult());
		}
		gs.setVariable("NOW_SENSORS_MAP", nowSensorStates);
		gs.setVariable("PREV_SENSORS_MAP", lastSensorStates);
		gs.setVariable("LAST_PHASE", phaseService.getPhaseById(lastPhase).getName());
		gs.setVariable("ACTUAL_PHASE", phaseService.getPhaseById(nowPhase).getName());
		gs.setVariable("SOIL_DET_TO_SEND", changeDetectionService.getChangeDetectionToSend());
	}

	public void handleSMS(SMSMessage message) {
		LOGGER.info("Odpalono handleSMS dla "+message.getPhoneNumber()+" / "+message.getText());
		List<Rule> rules=ruleService.getAllActiveSMSRules();
		GroovyShell gs=new GroovyShell();
		fillGroovyShell(gs);
		gs.setVariable("PHONE", message.getPhoneNumber());
				
		for (Rule rule : rules) {
			actualRuleId=rule.getId();
			actualRuleLogin="TEL:"+userService.getLoginByPhoneNumber(message.getPhoneNumber());
			
			Pattern p=Pattern.compile(rule.getCondition_());
			Matcher m=p.matcher(message.getText().toUpperCase().trim());
			if(m.matches()){
				try{
					for(int i=0; i<m.groupCount()+1; i++){
						gs.setVariable("G"+i, m.group(i));
						LOGGER.debug("Podstawiam pod zmienną G"+i+" = "+m.group(i));
					}
					gs.evaluate(new StringReader(rule.getExpression_()));
					return;
				}catch(Exception e){
					LOGGER.error("Błąd podczas weryfikacji/wykonania warunku reguly SMS nr "+rule.getId());
					LOGGER.error(Helper.STACK_TRACE,e);				
					continue;
				}					
			}			
		}
		command.sendSMS("Nie rozpoznano komendy.", message.getPhoneNumber());
		
	}
	private Map<String, SwitchState> buildSwitchStateMap() {
		Map<String, SwitchState> result=new HashMap<String, SwitchState>();
		List<SwitchDTO> switches=switchService.getAllSwitchesWithStates();
		for (SwitchDTO switchDTO : switches) {
			result.put(switchDTO.getName(), switchDTO.getState());
		}
		return result;
	}
	private Map<Integer, SensorResultDTO> buildSensorStateMap() {
		return new HashMap<Integer, SensorResultDTO>(sensorTask.getLastSuccesfullSensorResult());		
	}
	public Integer getActualRuleId() {
		return actualRuleId;
	}		
	
	public String getActualRuleLogin() {
		return actualRuleLogin;
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
