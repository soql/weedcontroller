package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.User;

@Component
public class UserDAO {
	@PersistenceContext
	private EntityManager em;

	public User findByLogin(String login) {
		 return em.find(User.class, login);				
	}

	public List<User> getAllSMSUsers() {
		Query query=em.createQuery("SELECT e FROM User e where e.sendSMS=true");
		return (List<User>)query.getResultList();
	}

	public boolean isExistPhoneNumber(String phoneNumber) {
		Query query=em.createQuery("SELECT e FROM User e where e.phoneNumber=:phoneNumber");
		query.setParameter("phoneNumber", phoneNumber);
		return query.getResultList().size()>0;
	}	
}
