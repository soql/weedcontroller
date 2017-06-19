package pl.net.oth.weedcontroller.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import pl.net.oth.weedcontroller.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;
    
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    
    
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
      	.antMatchers("/putSMS").permitAll()
      	.and().authorizeRequests().antMatchers("/**").access("hasRole('ADMIN')")        
        .and().formLogin().successHandler(loginSuccessHandler).and().httpBasic()
        .and().logout().logoutSuccessHandler(loginSuccessHandler).and().exceptionHandling().accessDeniedPage("/Access_Denied")
        .and().csrf().disable();
            	
    }
}