package pl.net.oth.weedcontroller.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.model.User;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	private final static Log LOGGER = LogFactory.getLog(CustomUserDetailsService.class);
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userService.findByLogin(login);
		LOGGER.debug("User : " + user);
		if (user == null) {
			LOGGER.error("User "+login+" not found");
			throw new UsernameNotFoundException("Username not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
				user.getEnabled().equals(Boolean.TRUE), true, true, true, getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		user.getRoles().stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		
		return authorities;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
