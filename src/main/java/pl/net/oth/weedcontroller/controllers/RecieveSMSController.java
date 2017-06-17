package pl.net.oth.weedcontroller.controllers;

import java.lang.ref.PhantomReference;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.SMSMessage;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.service.UserService;
import pl.net.oth.weedcontroller.task.CameraTask;
import pl.net.oth.weedcontroller.task.ruletask.RulesTask;

@Controller
public class RecieveSMSController {
	private final static Log LOGGER=LogFactory.getLog(RecieveSMSController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RulesTask rulesTask;
	
	@RequestMapping(value = "/putSMS", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody boolean putSMS(@RequestBody SMSMessage jsonString) {
		LOGGER.info("Odebrano SMS od:"+jsonString.getPhoneNumber()+ " o treści: "+jsonString.getText());
		if(!userService.validatePhoneNumber(jsonString.getPhoneNumber())){
			LOGGER.error("Numer "+jsonString.getPhoneNumber()+" nie znaleziony w bazie danych.");
			return false;
		}
		rulesTask.handleSMS(jsonString);
		return true;
		
	}
}
