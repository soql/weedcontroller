package pl.net.oth.weedcontroller.controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.net.oth.weedcontroller.helpers.Helper;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;
import pl.net.oth.weedcontroller.task.CameraTask;

@Controller
public class ChartController {
	private final static Log LOGGER=LogFactory.getLog(ChartController.class);
	public static final int TEMPERATURE=0;
	public static final int HUMIDITY=1;
	
	@Autowired
	private SwitchService switchService;

	@Autowired
	private SensorResultService sensorResultService;

	
	@RequestMapping(value = "/generateChart", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody  String generateChart(@RequestParam("dateFrom") final Long dateFrom,@RequestParam("dateTo") final Long dateTo ) {
		System.out.println("DateFrom "+dateFrom);
		System.out.println("DateTo "+dateTo);
		List<SensorResultLog> sensorResultLogs=sensorResultService.getResultsForDate(new Date(dateFrom), new Date(dateTo));
		
		float[] min={Float.MIN_VALUE, Float.MIN_VALUE};
		float[] max={Float.MIN_VALUE, Float.MIN_VALUE};
		for (SensorResultLog sensorResultLog : sensorResultLogs) {			
			if(min[TEMPERATURE]==Float.MIN_VALUE || sensorResultLog.getTemperature()<min[TEMPERATURE]){
				min[TEMPERATURE]=sensorResultLog.getTemperature();
			}
			if(max[TEMPERATURE]==Float.MAX_VALUE || sensorResultLog.getTemperature()>max[TEMPERATURE]){
				max[TEMPERATURE]=sensorResultLog.getTemperature();
			}
			
			if(min[HUMIDITY]==Float.MIN_VALUE || sensorResultLog.getHumidity()<min[HUMIDITY]){
				min[HUMIDITY]=sensorResultLog.getHumidity();
			}
			if(max[HUMIDITY]==Float.MAX_VALUE || sensorResultLog.getHumidity()>max[HUMIDITY]){
				max[HUMIDITY]=sensorResultLog.getHumidity();
			}
		}				
		
		 JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(
		         "WCR",		         
		         "Time" ,
		         "Value",
		         createDataset(sensorResultLogs, min, max) ,		         
		         true , true , false);
		 xylineChart.setBackgroundPaint(Color.lightGray);
		 XYPlot plot=xylineChart.getXYPlot();
		 plot.setBackgroundPaint(Color.white);
	     plot.setDomainGridlinePaint(Color.black);
	     plot.setRangeGridlinePaint(Color.black);
	     
	     	     
		 DateAxis axis = (DateAxis) plot.getDomainAxis();
		 axis.setDateFormatOverride(new SimpleDateFormat("MM-dd hh:mm:ss"));
		 
		 NumberAxis angeAxis = (NumberAxis) plot.getRangeAxis();
		 angeAxis.setRange(min[TEMPERATURE], max[TEMPERATURE]+1);
		 angeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		 angeAxis.setNumberFormatOverride(new DecimalFormat("##.#"));
		 		 		 
		 BufferedImage image=xylineChart.createBufferedImage(1920,1080);
		 ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
		 try {
			ImageIO.write(image, "png", imagebuffer);
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}		 
		return "data:image/png;base64,"+DatatypeConverter.printBase64Binary(imagebuffer.toByteArray());		
	}
	private XYDataset createDataset(List<SensorResultLog> sensorResultLogs, float[] min, float[] max ) {
		TimeSeriesCollection timeSeriesCollection=new TimeSeriesCollection();		
		  final TimeSeries temperature = new TimeSeries( "Temperatura" );  
	      for (SensorResultLog sensorResultLog : sensorResultLogs) {
	    	  temperature.addOrUpdate(new Second(sensorResultLog.getDate()), sensorResultLog.getTemperature());	    	  
	      }	    
	      timeSeriesCollection.addSeries(temperature);
	      final TimeSeries humidity = new TimeSeries( "Wilgotność" );  
	      for (SensorResultLog sensorResultLog : sensorResultLogs) {
	    	  System.out.println(sensorResultLog.getHumidity());
	    	  humidity.addOrUpdate(new Second(sensorResultLog.getDate()), (((sensorResultLog.getHumidity()-min[HUMIDITY])/(max[HUMIDITY]-min[HUMIDITY])*(max[TEMPERATURE]-min[TEMPERATURE]))+min[TEMPERATURE]));	    	  
	      }		      
	    		  
	      timeSeriesCollection.addSeries(humidity);
	      return timeSeriesCollection; 
	}
}
