package pl.net.oth.weedcontroller.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.ConfigurationDAO;
import pl.net.oth.weedcontroller.dao.RuleDAO;
import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Rule;

@Component
public class ConfigurationService {
	public static final String LAST_FOTO_KEY = "LAST_FOTO";
	public static final String HUMIDITY_POSITION="HUMIDITY_POSITION";
	public static final String ACTUAL_PHASE="ACTUAL_PHASE";
	@Autowired
	private ConfigurationDAO configurationDAO;
	
	public Configuration getByKey(String key) {
		return configurationDAO.getByKey(key);
	}

	@Transactional
	public void save(Configuration r) {
		configurationDAO.save(r);		
	}
	
	@Transactional
	public void update(Configuration r) {
		configurationDAO.update(r);		
	}
	
	public boolean isConfigurationExist(String...keys){
		for (String string : keys) {
			if(getByKey(string)==null)
				return false;
		}
		return true;
	}
}
