package pl.net.oth.weedcontroller.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.SMSMessage;
import pl.net.oth.weedcontroller.task.ruletask.Command;
import pl.net.oth.weedcontroller.task.ruletask.RulesTask;

@Controller
public class SendSMSController {
	private final static Log LOGGER=LogFactory.getLog(SendSMSController.class);
	

	@Autowired
	private Command command;
	
	@RequestMapping(value = "/sendSMS", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean sendSMS(@RequestBody SMSMessage jsonString) {
		LOGGER.info("SMS do wysyłki na numer:"+jsonString.getPhoneNumber()+ " o treści: "+jsonString.getText());
		command.sendSMS(jsonString.getText(), jsonString.getPhoneNumber());		
		return Boolean.TRUE;
		
	}
}
