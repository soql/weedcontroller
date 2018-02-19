package pl.net.oth.weedcontroller.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.net.oth.weedcontroller.model.Camera;

@Component
public class CameraDAO {
	@PersistenceContext
	private EntityManager em;		

	public List<Camera> getAllCameras(){
		Query query=em.createQuery("SELECT e FROM Camera e");		
		return (List<Camera>)query.getResultList();
	}

	public void persist(Camera camera) {		
		em.merge(camera);
	}
}
