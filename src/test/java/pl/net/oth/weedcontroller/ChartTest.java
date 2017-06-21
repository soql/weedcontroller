package pl.net.oth.weedcontroller;

import java.util.Date;

import javax.xml.ws.WebServiceProvider;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import pl.net.oth.weedcontroller.configuration.WeedControllerConfiguration;
import pl.net.oth.weedcontroller.controllers.ChartController;
import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.impl.GpioMockExternalController;
import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class, ChartController.class, SwitchService.class, SensorResultService.class, SwitchDAO.class, SwitchLogDAO.class, UserDAO.class, SensorExternalController.class, GpioMockExternalController.class, SensorResultDAO.class})
@ActiveProfiles(profiles = "DEV")
@WebAppConfiguration
public class ChartTest{

	@Autowired
	private ChartController chartController;
		
	@Test
	@Ignore
	public void test(){
		
		String data=chartController.generateChart(new Date().getTime()-1000*60*60*480, new Date().getTime());
		System.out.println(data);
	}
}
