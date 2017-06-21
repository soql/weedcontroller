package pl.net.oth.weedcontroller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.net.oth.weedcontroller.dao.UserDAO;
import pl.net.oth.weedcontroller.model.User;

@Service("userService")
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
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

}
