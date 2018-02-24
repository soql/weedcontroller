package pl.net.oth.weedcontroller.controllers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.Role;
import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;
import pl.net.oth.weedcontroller.model.dto.ChangeDetectionDTO;
import pl.net.oth.weedcontroller.service.ChangeDetectionService;
import pl.net.oth.weedcontroller.service.UserService;

@Controller
public class ChangeDetectionController {
	@Autowired
	private ChangeDetectionService changeDetectionService;
	
	@RequestMapping(value = "/getChangeDetection", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ChangeDetectionDTO> getChangeDetection(@RequestParam("number") final Integer number) {
		return changeDetectionService.getChangeDetectionLog(number);
	}
}
