package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.SwitchGPIO;
import pl.net.oth.weedcontroller.model.SwitchGpioLog;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.PowerUsageDTO;

@Component
public class PowerService {
	private final static Log LOGGER = LogFactory.getLog(PowerService.class);

	private static final String ONE_WAT_COST = "ONE_WAT_COST";

	@Autowired
	private SwitchService switchService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	public List<PowerUsageDTO> calculatePowerUsage(final Long dateFrom, final Long dateTo ) {
		List<PowerUsageDTO> resultsDTO=new ArrayList<PowerUsageDTO>();
		List<SwitchGPIO> allSwitches=switchService.getAllManagedSwitches();
		Double oneWatCost=getOneWatCost();		
		for (SwitchGPIO switch_ : allSwitches) {
			PowerUsageDTO powerUsageDTO=new PowerUsageDTO();
			powerUsageDTO.setSwitchName(switch_.getParent().getName()+(switch_.getParent().getGpios().size()>1?"("+switch_.getDescription()+")":""));
			powerUsageDTO.setMaxTime(Helper.milisecondsToHours(dateTo-dateFrom));
			powerUsageDTO.setPowerUsage(switch_.getPowerUsage());
			LOGGER.debug("Przelacznik "+powerUsageDTO.getSwitchName());
			List<SwitchLog> results=switchService.getLogsForDate(switch_.getParent(), new Date(dateFrom), new Date(dateTo));
			long milisecoundsOn=0;
			SwitchLog lastState=null;
			SwitchLog prevState=null;
			for (SwitchLog switchLog : results) {	
				prevState=lastState;
				if(lastState!=null) {
					LOGGER.debug("Resultat: "+lastState.getState()+" od "+lastState.getDate()+" do "+switchLog.getDate());					
					if(lastState.getState().equals(SwitchState.ON)) {
						long timeInMilisecounds=(switchLog.getDate().getTime()-lastState.getDate().getTime());
						milisecoundsOn+=timeInMilisecounds;
						LOGGER.debug("Dodaję do "+powerUsageDTO.getSwitchName()+" czas "+timeInMilisecounds+"("+Helper.milisecondsToHours(timeInMilisecounds)+ "h) za okres "+lastState.getDate()+" do "+switchLog.getDate());
						
						if(switch_.getParent().getGpios().size()>1) {
							List<SwitchGpioLog> gpioResults=switchService.getManagedSwitchLogsForDate(switch_, lastState.getDate(), switchLog.getDate());
							SwitchGpioLog lastGpioState=null;
							SwitchGpioLog prevGpioState=null;
							for (SwitchGpioLog switchGpioLog : gpioResults) {
								prevGpioState=lastGpioState;	
								if(lastGpioState!=null) {
									if(lastGpioState.getState().equals(SwitchState.OFF)) {
										timeInMilisecounds=(switchGpioLog.getDate().getTime()-lastGpioState.getDate().getTime());
										milisecoundsOn-=timeInMilisecounds;
										LOGGER.debug("Odejmuję od "+powerUsageDTO.getSwitchName()+" czas "+timeInMilisecounds+"("+Helper.milisecondsToHours(timeInMilisecounds)+ "h) za okres "+lastGpioState.getDate()+" do "+switchGpioLog.getDate());
									}
								}
								lastGpioState=switchGpioLog;
							}							
						}
					}
				}				
				lastState=switchLog;
			}	
			if(lastState.getState().equals(SwitchState.ON) && lastState.getDate().before(new Date(dateTo))) {
				long timeInMilisecounds=(new Date(dateTo).getTime()-lastState.getDate().getTime());
				milisecoundsOn+=timeInMilisecounds;
				LOGGER.debug("Dodaję do "+powerUsageDTO.getSwitchName()+" czas "+timeInMilisecounds+"("+Helper.milisecondsToHours(timeInMilisecounds)+ "h) za okres "+new Date(dateTo)+" do "+lastState.getDate());
			}
			powerUsageDTO.setPowerOnTime(Helper.milisecondsToHours(milisecoundsOn));
			powerUsageDTO.setCost(powerUsageDTO.getPowerOnTime()/1000*oneWatCost*powerUsageDTO.getPowerUsage());
			resultsDTO.add(powerUsageDTO);
		}
		
		return resultsDTO;
	}

	private Double getOneWatCost() {
		Configuration oneWatCost=configurationService.getByKey(ONE_WAT_COST);
		if(oneWatCost!=null) {			
			try {
				return Double.parseDouble(oneWatCost.getValue());
			} catch (NumberFormatException e) {
				return new Double(0);
			}
		
		}
		return new Double(0);
	}
}
