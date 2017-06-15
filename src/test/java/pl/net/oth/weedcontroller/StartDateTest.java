package pl.net.oth.weedcontroller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.net.oth.weedcontroller.dao.ConfigurationDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.impl.GpioMockExternalController;
import pl.net.oth.weedcontroller.external.impl.SMSController;
import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.SensorTask;
import pl.net.oth.weedcontroller.task.ruletask.Command;
import pl.net.oth.weedcontroller.task.ruletask.RulesTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, Command.class, ConfigurationService.class, ConfigurationDAO.class, SMSController.class, RulesTask.class, SwitchService.class, SwitchDAO.class, SwitchLogDAO.class, UserDAO.class, GpioMockExternalController.class, SensorTask.class, SensorExternalController.class, RuleService.class, RuleDAO.class })
@ActiveProfiles(profiles = "DEV")
public class StartDateTest {
	
	@Autowired
	private Command command;
	
	@Test
	public void test() {
		/*System.out.println(command.getNumberOfDays());*/
		System.out.println("ELO "+command.getLastSwitchStateChangeUser("LAMPA", SwitchState.ON));

	}
}
