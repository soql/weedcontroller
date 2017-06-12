package pl.net.oth.weedcontroller.controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class FotoController {
	private final static Log LOGGER = LogFactory.getLog(FotoController.class);
	
	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/getLastFoto", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getLastFoto() {
		ByteArrayOutputStream imagebuffer=null;
		try {
			String fileName="/opt/camera/image-"+configurationService.getByKey(ConfigurationService.LAST_FOTO_KEY).getValue()+".jpg";
			LOGGER.debug("Rządanie pobrania pliku "+fileName);
			BufferedImage image = ImageIO
					.read(new File(fileName));
			imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", imagebuffer);
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		return "data:image/png;base64," + DatatypeConverter.printBase64Binary(imagebuffer.toByteArray());
	}
}
