package pl.net.oth.weedcontroller.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.SwitchLogDTO;
import pl.net.oth.weedcontroller.service.SwitchService;


@Controller
public class PowerUsageController {	
	private final static Log LOGGER=LogFactory.getLog(PowerUsageController.class);
	@Autowired
	private SwitchService switchService;
	@RequestMapping(value = "/calculatePowerUsage", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SwitchLogDTO> calculatePowerUsage(@RequestParam("dateFrom") final Long dateFrom,@RequestParam("dateTo") final Long dateTo ) {
		List<Switch> allSwitches=switchService.getAllSwitches();
		for (Switch switch_ : allSwitches) {
			LOGGER.debug("Przelacznik "+switch_.getName());
			List<SwitchLog> results=switchService.getLogsForDate(switch_, new Date(dateFrom), new Date(dateTo));
			for (SwitchLog switchLog : results) {
				LOGGER.debug(switchLog.getDate()+" "+switchLog.getState());
			}
		}
		return null;
	}
}
