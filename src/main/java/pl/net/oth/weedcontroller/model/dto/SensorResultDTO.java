package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SensorResultDTO {
	public static final String TEMPERATURE="TEMPERATURE";
	public static final String HUMIDITY="HUMIDITY";
	public Date lastSuccesfullRead;
	public Map<String, SensorResultDataDTO> results;		
	
	public SensorResultDTO() {
		super();		
		
	}

	public Date getLastSuccesfullRead() {
		return lastSuccesfullRead;
	}

	public void setLastSuccesfullRead(Date lastSuccesfullRead) {
		this.lastSuccesfullRead = lastSuccesfullRead;
	}

	public Map<String, SensorResultDataDTO> getResults() {
		if(results==null) 
			results=new HashMap<>();		
		return results;
	}

	
	
	public void setResults(Map<String, SensorResultDataDTO> results) {
		this.results = results;
	}	
}
