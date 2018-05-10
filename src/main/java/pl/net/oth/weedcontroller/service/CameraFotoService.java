package pl.net.oth.weedcontroller.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.net.oth.weedcontroller.dao.CameraDAO;
import pl.net.oth.weedcontroller.dao.CameraFotoDAO;
import pl.net.oth.weedcontroller.model.AuditLog;
import pl.net.oth.weedcontroller.model.Camera;
import pl.net.oth.weedcontroller.model.CameraFoto;

@Component
public class CameraFotoService {
	@Autowired
	private CameraFotoDAO cameraFotoDAO;


	@Transactional
	public void save(CameraFoto cameraFoto) {
		cameraFotoDAO.persist(cameraFoto);
	}
	
	@Transactional
	public void remove(String fileName) {
		CameraFoto cameraFoto=cameraFotoDAO.getByFileName(fileName);
		cameraFotoDAO.remove(cameraFoto);
	}

}
