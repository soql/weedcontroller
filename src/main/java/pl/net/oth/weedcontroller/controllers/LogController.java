package pl.net.oth.weedcontroller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchLogDTO;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class LogController {
	@Autowired
	private SwitchService switchService;
	@RequestMapping(value = "/getLogs", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SwitchLogDTO> getSwitches() {
		return switchService.getLogs(0);
	}
}
