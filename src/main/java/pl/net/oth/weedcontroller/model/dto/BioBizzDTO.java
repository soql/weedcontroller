package pl.net.oth.weedcontroller.model.dto;

import java.util.List;
import java.util.Map;

import pl.net.oth.weedcontroller.model.BioBizz;
import pl.net.oth.weedcontroller.model.Phase;

public class BioBizzDTO {
	private List<Phase> phases;
	private Phase actualPhase;
	private Integer actualWeek;
	
	private Map<String, Integer> bioBizzData;

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	public Integer getActualWeek() {
		return actualWeek;
	}

	public void setActualWeek(Integer actualWeek) {
		this.actualWeek = actualWeek;
	}

	public Map<String, Integer> getBioBizzData() {
		return bioBizzData;
	}

	public void setBioBizzData(Map<String, Integer> bioBizzData) {
		this.bioBizzData = bioBizzData;
	}

	public Phase getActualPhase() {
		return actualPhase;
	}

	public void setActualPhase(Phase actualPhase) {
		this.actualPhase = actualPhase;
	}

	
	
	
	
}
