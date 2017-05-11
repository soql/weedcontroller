package pl.net.oth.weedcontroller.controllers;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.dto.SwitchDTO;

@Controller
public class RecieveSMSController {
	@RequestMapping(value = "/putSMS", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody boolean putSMS(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("text") String text) {
		System.out.println("Odebrano "+phoneNumber+ " "+text);
		return true;
	}
}
