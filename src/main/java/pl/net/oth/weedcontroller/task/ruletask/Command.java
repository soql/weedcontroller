package pl.net.oth.weedcontroller.task.ruletask;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.impl.SMSController;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.User;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SwitchService;

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
	
	public SwitchState css(String switchName){
		return checkSwitchState(switchName);
	}
			
	public SwitchState checkSwitchState(String switchName){
		return switchService.getStateByName(switchName);		
	}
	public boolean sss(String switchName, SwitchState targetState){			
		return setSwitchState(switchName, targetState, "REG:"+rulesTask.getActualRuleId());		
	}	
	public boolean setSwitchState(String switchName, SwitchState targetState, String userName){
		Switch s=switchService.getSwitchByName(switchName);				
		return switchService.setSwitchState(s.getGpioNumber(), targetState, userName);		
	}	
	
	public boolean csnc(String switchName, SwitchState state){
		return checkSwitchNowChange(switchName, state);
	}
	
	public boolean checkSwitchNowChange(String switchName, SwitchState state){
		if(rulesTask.getLastSwitchStates()==null)
			return false;
		if(rulesTask.getNowSwitchStates().get(switchName).equals(state) && !rulesTask.getLastSwitchStates().get(switchName).equals(state)){
			return true;
		}
		return false;
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
		ruleService.setNextTimeExecution(rulesTask.getActualRuleId(), minutes);
	}
	public void sendSMS(String text){
		List<User> users=userDAO.getAllSMSUsers();
		for (User user : users) {
			smsController.sendSMS(user.getPhoneNumber(), text);
		}
	}
}
