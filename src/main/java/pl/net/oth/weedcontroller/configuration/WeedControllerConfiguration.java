package pl.net.oth.weedcontroller.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan(basePackages = "pl.net.oth.weedcontroller")
@ImportResource("classpath:applicationContext.xml")
public class WeedControllerConfiguration extends WebMvcConfigurerAdapter implements SchedulingConfigurer{
	
	@Bean(name="HelloWorld")
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);

		return viewResolver;
	}

	/*
     * Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
     *
     */
    @Override    
    public void addResourceHandlers(ResourceHandlerRegistry registry) {    	
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.setOrder(-1);
        
        
    }

	@Override
	public void configureTasks(ScheduledTaskRegistrar arg0) {		
		 ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	        taskScheduler.setPoolSize(5);
	        taskScheduler.initialize();
	        arg0.setTaskScheduler(taskScheduler);
		
	}
}
