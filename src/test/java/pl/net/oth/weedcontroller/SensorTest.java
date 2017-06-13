package pl.net.oth.weedcontroller;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import pl.net.oth.weedcontroller.configuration.WeedControllerConfiguration;
import pl.net.oth.weedcontroller.external.impl.SensorExternalController;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.task.SensorTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SensorTask.class, SensorExternalController.class})
@ActiveProfiles(profiles = "DEV")

public class SensorTest {

	@Autowired
	private SensorTask sensorTask;
	
	@Test
	public void test() {
		SensorResultDTO s=new SensorResultDTO(new Date(), 10, 80);
		sensorTask.setLastSensorResult(s);
		sensorTask.check();
		s=new SensorResultDTO(new Date(), 10, 80);
		sensorTask.setLastSensorResult(s);
		sensorTask.check();
		s=new SensorResultDTO(new Date(), 3, 80);
		sensorTask.setLastSensorResult(s);
		sensorTask.check();

	}
}
