package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.SwitchLog;
import pl.net.oth.weedcontroller.model.User;

@Component
@EnableTransactionManagement
public class UserDAO {
	@PersistenceContext
	private EntityManager em;

	@Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
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

	public String getLoginByPhoneNumber(String phoneNumber) {
		Query query=em.createQuery("SELECT e FROM User e where e.phoneNumber=:phoneNumber");
		query.setParameter("phoneNumber", phoneNumber);
		return ((User)query.getSingleResult()).getFullName();
	}	
}
