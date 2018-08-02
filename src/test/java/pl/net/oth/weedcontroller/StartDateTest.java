package pl.net.oth.weedcontroller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.net.oth.weedcontroller.dao.ConfigurationDAO;
import pl.net.oth.weedcontroller.dao.PhaseDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.dao.SensorDAO;
import pl.net.oth.weedcontroller.dao.SensorResultDAO;
import pl.net.oth.weedcontroller.dao.SwitchDAO;
import pl.net.oth.weedcontroller.dao.SwitchLogDAO;
import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.external.GpioExternalController;
import pl.net.oth.weedcontroller.external.impl.GpioMockExternalController;
import pl.net.oth.weedcontroller.external.impl.SMSController;
import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.model.Sensor;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.PhaseService;
import pl.net.oth.weedcontroller.service.RuleService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.service.UserService;
import pl.net.oth.weedcontroller.task.SensorTask;
import pl.net.oth.weedcontroller.task.ruletask.Command;
import pl.net.oth.weedcontroller.task.ruletask.RulesTask;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@ContextConfiguration(classes = { UserService.class, PhaseDAO.class, PhaseService.class, SensorDAO.class,
		SensorService.class, RulesTask.class, TestConfig.class, RulesTask.class, Command.class,
		GpioMockExternalController.class, ConfigurationService.class, ConfigurationDAO.class, SMSController.class,
		RulesTask.class, SwitchService.class, SwitchDAO.class, SwitchLogDAO.class, UserDAO.class,
		GpioMockExternalController.class, SensorTask.class, SensorExternalController.class, RuleService.class,
		RuleDAO.class, SensorResultService.class, SensorResultDAO.class })
@ActiveProfiles(profiles = "DEV")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class StartDateTest {
	@Autowired
	private SensorService sensorService;
	@Autowired
	private SensorResultService sensorResultService;
	@Autowired
	private Command command;

	@Test
	public void test() {
		/*Sensor s = sensorService.getSensorByNumber(3);			
		
		String dateStart="2018-02-20 18:00:00";
		System.out.println(parseDate(dateStart).getTime());
		List<SensorResultLog> ser = sensorResultService
				.getResultsForDate(parseDate(dateStart), new Date(), s);		
		System.out.println(ser.size());
		SensorResultLog[] list = new SensorResultLog[ser.size()];
		ser.toArray(list);
		System.out.println(list.length);
		int SEC = 1000;
		int HUMIDITY_LEVEL=600;
		int MEASURMENT_TIME=300;
		boolean active=false;
		int besti=0;
		int bestj=0;
		int worsei=0;
		int worsej=0;
		
		for (int i = 0; i < list.length;) {
			for (int j = 0; j < list.length;) {
				if (list[j].getDate().getTime() - list[i].getDate().getTime() < MEASURMENT_TIME * SEC) {
					j++;
					continue;
				}				
				if (list[i].getHumidity() - list[j].getHumidity() > HUMIDITY_LEVEL) {
					if(!active) {
						System.out.println("JEST " + formatDate(list[i].getDate()) + " do " + formatDate(list[j].getDate())
								+ " = " + (list[j].getDate().getTime() - list[i].getDate().getTime()) / SEC);										
						active=true;
						besti=i;
						bestj=j;
						worsei=i;
						worsej=j;
					}else {
						if((list[i].getHumidity() - list[j].getHumidity())>(list[besti].getHumidity() - list[bestj].getHumidity())){
							besti=i;
							bestj=j;
						}
						if((list[i].getHumidity() - list[j].getHumidity())<(list[worsei].getHumidity() - list[worsej].getHumidity())){
							worsei=i;
							worsej=j;
						}
					}
				}else {
					if(active) {
						System.out.println("Wykryto podlanie BEST: " + list[bestj].getHumidity() + " -> " + list[besti].getHumidity()
								+ "(" + (list[besti].getHumidity() - list[bestj].getHumidity()) + ") ("+formatDate(list[besti].getDate())+" do "+formatDate(list[bestj].getDate())+")");	
						System.out.println("Wykryto podlanie WORSE: " + list[worsej].getHumidity() + " -> " + list[worsei].getHumidity()
								+ "(" + (list[worsei].getHumidity() - list[worsej].getHumidity()) + ") ("+formatDate(list[worsei].getDate())+" do "+formatDate(list[worsej].getDate())+")");	
					}
					active=false;
				}
				i++;
			}
		}*/
		System.exit(0);
	}

	private Date parseDate(String dateStart) {		 
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStart);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
