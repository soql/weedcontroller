package pl.net.oth.weedcontroller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.dto.AuditLogDTO;
import pl.net.oth.weedcontroller.model.dto.SwitchLogDTO;
import pl.net.oth.weedcontroller.service.AuditLogService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class AuditLogController {
	@Autowired
	private AuditLogService auditLogService;
	@RequestMapping(value = "/getAuditLogs", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<AuditLogDTO> getSwitches(@RequestParam("number") final Integer number) {
		return auditLogService.getAuditLog(number);
	}
}
