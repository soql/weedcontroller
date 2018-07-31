package weedcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import pl.net.oth.weedcontroller.configuration.WeedControllerConfiguration;
import pl.net.oth.weedcontroller.task.ruletask.Command;

@Component
@WebAppConfiguration 
@ComponentScan(basePackages = "pl.net.oth.weedcontroller")
public class AggregateTest {
	@Autowired
	private Command command;
	public static void main(String[] args) {
		new AggregateTest();		
	}
	public AggregateTest() {
		ApplicationContext context
        = new AnnotationConfigApplicationContext(WeedControllerConfiguration.class);
		 
	System.out.println(command.gavAS("TEMPERATURE", "avg", 10));
	}
}
