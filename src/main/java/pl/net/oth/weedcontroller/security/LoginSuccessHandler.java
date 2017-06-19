package pl.net.oth.weedcontroller.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.AuditOperation;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.service.AuditLogService;
import pl.net.oth.weedcontroller.service.UserService;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler{
	private final static Log LOGGER = LogFactory.getLog(LoginSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private AuditLogService auditLogService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {	
		User user=(User)authentication.getPrincipal();
		LOGGER.debug("Zalogowano "+user);		
		logToDB(request, user, AuditOperation.LOGIN);
		redirectStrategy.sendRedirect(request, response, "/");
	}
	private void logToDB(HttpServletRequest request, User user, AuditOperation auditOperation) {
		AuditLog auditLog=new AuditLog();
		auditLog.setOperaion(auditOperation);
		auditLog.setUser(userService.findByLogin(user.getUsername()));
		auditLog.setIp(request.getRemoteAddr());
		auditLog.setTime(new Date());
		auditLogService.save(auditLog);
		
	}
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		User user=(User)authentication.getPrincipal();
		LOGGER.debug("Wylogowano "+user);		
		logToDB(request, user, AuditOperation.LOGOUT);
		redirectStrategy.sendRedirect(request, response, "/");
		
	}	
}
