package pl.net.oth.weedcontroller.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.service.ConfigurationService;

@Configuration
@EnableScheduling
public class PingTask {
	private final static Log LOGGER = LogFactory.getLog(PingTask.class);
	
	private final static String ADDRESS_TO_PING="8.8.8.8";

	public static final String LAST_INTERNET_CONNECTION = "LAST_INTERNET_CONNECTION";
	
	@Autowired
	private ConfigurationService configurationService;

	@Scheduled(fixedDelay = 60000)
	public void checkPing() {		
		InetAddress inet;
		try {
			inet = InetAddress.getByName(ADDRESS_TO_PING);			
			if(inet.isReachable(5000)){
				LOGGER.debug("Ping ok - połączenie internetowe aktywne.");
				updateConfig();
			}else{
				LOGGER.error("Ping nie ok - brak internetu.");
			}
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}	
	}

	private void updateConfig() {
		pl.net.oth.weedcontroller.model.Configuration conf=new pl.net.oth.weedcontroller.model.Configuration();
		conf.setKey(LAST_INTERNET_CONNECTION);
		conf.setValue(String.valueOf(new Date().getTime()));
		configurationService.update(conf);
		
	}
}
