package pl.net.oth.weedcontroller.controllers;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.model.dto.SensorResultDTO;
import pl.net.oth.weedcontroller.model.dto.SensorResultJson;
import pl.net.oth.weedcontroller.service.SensorService;
import pl.net.oth.weedcontroller.task.SensorTask;

@Controller
public class SensorsController {
	private final static Log LOGGER = LogFactory.getLog(SensorsController.class);
	
	@Autowired
	private SensorTask sensorTask;
	
	@Autowired
	private SensorService sensorService;
	
	@RequestMapping(value = "/sensors", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<SensorResultJson> getSensors() {
		List<SensorResultJson> result=new ArrayList<>();
		Map<Integer, SensorResultDTO> sensorResultDTO=sensorTask.getLastSuccesfullSensorResult();
		if(sensorResultDTO==null){
			LOGGER.error("Brak udanego odczytu");
			return null;
		}		
		
		List<Entry<Integer, SensorResultDTO>> keySet=sensorResultDTO.entrySet().stream().sorted((e1, e2) -> {
			return Integer.compare(e1.getValue().getSortOrder(), e2.getValue().getSortOrder());
			}).collect(Collectors.toList());
		for (Entry<Integer, SensorResultDTO>  entry : keySet) {
			if(Boolean.TRUE.equals(sensorResultDTO.get(entry.getKey()).getVisibleOnGui())) {
				SensorResultDTO sensorResultDTO2=sensorResultDTO.get(entry.getKey());
				result.add(new SensorResultJson(sensorResultDTO2, sensorService.getNameByNumber(entry.getKey())));
			}
		}
		return result;		
	}
}
