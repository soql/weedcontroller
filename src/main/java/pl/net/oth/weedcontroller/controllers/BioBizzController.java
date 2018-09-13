package pl.net.oth.weedcontroller.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.BioBizz;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.dto.BioBizzDTO;
import pl.net.oth.weedcontroller.model.dto.ChangeDetectionDTO;
import pl.net.oth.weedcontroller.service.BioBizzService;
import pl.net.oth.weedcontroller.service.ChangeDetectionService;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.PhaseService;

@Controller
public class BioBizzController {
	private final static Log LOGGER = LogFactory.getLog(FotoController.class);
	
	@Autowired
	private BioBizzService bioBizzService;
	
	@Autowired
	private PhaseService phaseService;	
	
	@RequestMapping(value = "/getBioBizz", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<BioBizz> getNowBioBizz() {
		LOGGER.debug("/getBioBizz phaseId="+phaseService.getActualPhase().getId()+" days="+phaseService.getNumberOfDays());
		return bioBizzService.getData(phaseService.getActualPhase(), phaseService.getNumberOfDays());
	}
}
