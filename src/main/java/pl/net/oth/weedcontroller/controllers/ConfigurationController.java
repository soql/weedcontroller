package pl.net.oth.weedcontroller.controllers;

import java.text.ParseException;
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

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.Switch;
import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.SwitchLogDTO;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class ConfigurationController {
	private final static Log LOGGER = LogFactory.getLog(ConfigurationController.class);
	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/getConfigurationAsDate", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody long getConfigurationAsDate(@RequestParam("key") final String key) {
		String value = configurationService.getByKey(key).getValue();
		Date startDate;
		try {
			startDate = Helper.START_DATE_FORMAT.parse(value);
			return startDate.getTime();
		} catch (ParseException e) {
			LOGGER.error(e);
		}
		return new Date().getTime();
		
	}
}
