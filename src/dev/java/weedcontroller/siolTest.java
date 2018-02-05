package weedcontroller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import pl.net.oth.weedcontroller.configuration.WeedControllerConfiguration;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;

@Component
@Configuration
@ComponentScan(basePackages = "pl.net.oth.weedcontroller")
public class siolTest {
	@Autowired
	private SensorService sensorService;
	@Autowired
	private SensorResultService sensorResultService;
	public siolTest() {
		 ApplicationContext context
         = new AnnotationConfigApplicationContext(WeedControllerConfiguration.class);
		 Sensor s=sensorService.getSensorByNumber(3);
		 System.out.println(s.getName());
		sensorResultService.getResultsForDate(new Date(new Date().getTime()-1000*60*24*5), new Date(), s);
		System.exit(0);
	}
	public static void main(String[] args) {
		new siolTest();
	}
}
