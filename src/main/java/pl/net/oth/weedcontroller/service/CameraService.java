package pl.net.oth.weedcontroller.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.CameraDAO;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Camera;

@Component
public class CameraService {
	@Autowired
	private CameraDAO cameraDAO;

	public List<Camera> getAllCameras() {
		return cameraDAO.getAllCameras();
	}

	@Transactional
	public void save(Camera camera) {
		cameraDAO.persist(camera);
	}

	public Camera getCameraByName(String cameraName) {
		return cameraDAO.getCameraByName(cameraName);		
	}

}
