package pl.net.oth.weedcontroller.helpers;

import java.text.SimpleDateFormat;

import groovy.lang.GroovyShell;

public class Helper {
	public static final String STACK_TRACE="StackTrace:";
	public static final SimpleDateFormat START_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd");
	
	public static Double milisecondsToHours(long l) {		
		return new Double( (l/(double)1000/60/60));
	}
}
