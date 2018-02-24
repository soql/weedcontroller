package pl.net.oth.weedcontroller.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.model.Role;
import pl.net.oth.weedcontroller.model.User;

@EnableTransactionManagement
@Component
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)	
	public User findByLogin(String login) {		
		return userDAO.findByLogin(login);
	}
	
	public List<User> getAllSMSUsers(){
		return userDAO.getAllSMSUsers();
	}

	public boolean validatePhoneNumber(String phoneNumber) {		
		return userDAO.isExistPhoneNumber(phoneNumber);
	}
	
	public String getLoginByPhoneNumber(String phoneNumber) {		
		return userDAO.getLoginByPhoneNumber(phoneNumber);
	}

	@Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
	public List<String> getUserRoles() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); 
		return findByLogin(name).getRoles().stream().map(Role::getName).collect(Collectors.toList());
	}

}
