package pl.net.oth.weedcontroller.event;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.service.SwitchService;

@Component
public class ChangeSwitchStateListener implements ApplicationListener<ChangeSwitchStateEvent>{
	@Autowired
	private SwitchLogDAO switchLogDAO;
	
	private final static Log LOGGER = LogFactory.getLog(ChangeSwitchStateListener.class);
	public void onApplicationEvent(ChangeSwitchStateEvent event) {
		
		LOGGER.info("Odebrano rządanie asynchroniczne");		
		logSwitchChange(event);
		
	}
	@Transactional
	private void logSwitchChange(ChangeSwitchStateEvent event) {
		SwitchLog switchLog=new SwitchLog();
		switchLog.setSwitch_(event.getSwitch());		
		switchLog.setUser(event.getUser());
		switchLog.setRuleUser(event.getRuleUser());
		switchLog.setState(event.getState());
		switchLog.setDate(new Date());		
		switchLogDAO.persist(switchLog);		
	}

}
