package pl.net.oth.weedcontroller.model.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SensorResultDTO {
	public static final String TEMPERATURE="TEMPERATURE";
	public static final String HUMIDITY="HUMIDITY";
	public static final Object POWER = "POWER";
	public Date lastSuccesfullRead;
	public SortedMap<String, SensorResultDataDTO> results;		
	private Boolean visibleOnGui;
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
			results=new TreeMap<>();		
		return results;
	}

	
	
	public void setResults(TreeMap<String, SensorResultDataDTO> results) {
		this.results = results;
	}

	public Boolean getVisibleOnGui() {
		return visibleOnGui;
	}

	public void setVisibleOnGui(Boolean visibleOnGui) {
		this.visibleOnGui = visibleOnGui;
	}
	
}
