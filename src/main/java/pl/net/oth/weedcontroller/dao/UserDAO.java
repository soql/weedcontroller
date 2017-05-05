package pl.net.oth.weedcontroller.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.User;

@Component
public class UserDAO {
	@PersistenceContext
	private EntityManager em;

	public User findByLogin(String login) {
		 return em.find(User.class, login);				
	}
	
}
