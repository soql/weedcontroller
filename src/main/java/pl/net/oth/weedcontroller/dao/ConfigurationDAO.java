package pl.net.oth.weedcontroller.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.Configuration;
import pl.net.oth.weedcontroller.model.Rule;
import pl.net.oth.weedcontroller.model.Switch;

@Component
public class ConfigurationDAO {

	@PersistenceContext
	private EntityManager em;		

	public Configuration getByKey(String key) {
		return em.find(Configuration.class, key);		
	}

	@Transactional
	public void save(Configuration r) {
		em.merge(r);		
	}
	@Transactional
	public void update(Configuration r) {
		em.merge(r);		
	}
}


