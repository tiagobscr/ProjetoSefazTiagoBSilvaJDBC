package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Factory {
	
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;


	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("persistence_escola1");
			entityManager = entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}
	
	 public void closeEntityManagerFactory() {
		    
		    if (entityManager != null) {
		      entityManager.close();
		      entityManager = null;
		      
		    }
		  }

}
