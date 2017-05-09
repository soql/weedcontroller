package pl.net.oth.weedcontroller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.SwitchState;
import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchDTO;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class SwitchController {
	@Autowired
	private SwitchService switchService;
	
	@RequestMapping(value = "/getSwitches", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SwitchDTO> getSwitches() {
		return switchService.getAllSwitchesWithStates();		
	}
	
	@RequestMapping(value = "/setState", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Boolean setSwitch(@RequestParam("switchNumber") final Integer switchNumber,@RequestParam("switchState") final SwitchState switchState) {
		return switchService.setSwitchState(switchNumber,switchState);
	}
}

