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
import pl.net.oth.weedcontroller.model.Camera;
import pl.net.oth.weedcontroller.model.SensorResultLog;
import pl.net.oth.weedcontroller.model.dto.FotoDTO;
import pl.net.oth.weedcontroller.service.CameraService;
import pl.net.oth.weedcontroller.service.ConfigurationService;
import pl.net.oth.weedcontroller.service.SensorResultService;
import pl.net.oth.weedcontroller.service.SwitchService;

@Controller
public class FotoController {
	private final static Log LOGGER = LogFactory.getLog(FotoController.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CameraService cameraService;

	@RequestMapping(value = "/getLastFoto", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody FotoDTO[] getLastFoto() {
		List<Camera> allCameras = cameraService.getAllCameras();
		FotoDTO[] result = new FotoDTO[allCameras.size()];

		int i = 0;
		for (Camera camera : allCameras) {
			String fileName = "/opt/camera/" + camera.getName() + "-" + camera.getLastFoto() + ".jpg";
			LOGGER.debug("Rządanie pobrania pliku " + fileName + " z kamery " + camera.getName());
			BufferedImage image;
			try {
				result[i]=new FotoDTO();
				image = ImageIO.read(new File(fileName));
				result[i].setFoto(convertToBase64(getFullPhoto(image)));
				long fotoTime=Long.parseLong(camera.getLastFoto());
				result[i].setTime((int)(new Date().getTime()-fotoTime)/1000);
				i++;
			} catch (IOException e) {
				LOGGER.error(Helper.STACK_TRACE, e);
			}
		}
		return result;
	}

	private byte[] getFullPhoto(BufferedImage image) {
		ByteArrayOutputStream imagebuffer = null;
		try {
			imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", imagebuffer);
			return imagebuffer.toByteArray();
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		return null;
	}

	private byte[] getPartPhoto(BufferedImage image, String x, String y, String w, String h) {
		ByteArrayOutputStream imagebuffer = null;
		try {

			imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image.getSubimage(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(w),
					Integer.parseInt(h)), "jpg", imagebuffer);
			return imagebuffer.toByteArray();
		} catch (IOException e) {
			LOGGER.error(Helper.STACK_TRACE, e);
		}
		return null;
	}

	private String convertToBase64(byte[] image) {
		return "data:image/png;base64," + DatatypeConverter.printBase64Binary(image);
	}
}
