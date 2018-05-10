package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.Camera;
import pl.net.oth.weedcontroller.model.CameraFoto;

@Component
public class CameraFotoDAO {
	@PersistenceContext
	private EntityManager em;		
	
	public void persist(CameraFoto cameraFoto) {		
		em.merge(cameraFoto);
	}
	public void remove(CameraFoto cameraFoto) {		
		em.remove(cameraFoto);
	}
	public CameraFoto getByFileName(String fileName) {
		Query q=em.createQuery("SELECT e FROM CameraFoto e where e.fileName=:fileName");
		q.setParameter("fileName", fileName);
		List<CameraFoto> cameraFotos=q.getResultList();
		if(cameraFotos!=null && cameraFotos.size()>0) {
			return cameraFotos.get(0);
		}
		return null;		
	}
}
