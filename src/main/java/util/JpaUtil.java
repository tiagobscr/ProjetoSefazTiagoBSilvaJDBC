package util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class JpaUtil {
	
	//private static PersistenceProperties properties;
	
	private static EntityManagerFactory factory;
	private static String driver = "org.postgresql.Driver";
	private static Object overDrive = driver;
	
	


    static {
    	
    	Map<String, String> env = System.getenv();
    	Map<String, Object> configOverrides = new HashMap<String, Object>();
    	for (String envName : env.keySet()) {
    	  if (envName.contains("JDBC_DATABASE_URL")) {
    	       
    	    configOverrides.put("javax.persistence.jdbc.url", env.get(envName));
    	    configOverrides.put("javax.persistence.jdbc.driver", overDrive);
    	  }
    	  if (envName.contains("JDBC_DATABASE_USERNAME")) {
    		  configOverrides.put("javax.persistence.jdbc.user",env.get(envName));
    	  }
    	  if (envName.contains("JDBC_DATABASE_PASSWORD")) {
    		  configOverrides.put("javax.persistence.jdbc.password",env.get(envName));
    		  }
    	  
    	}
    	
    	
        factory = Persistence.createEntityManagerFactory("Sefaz",configOverrides);
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        factory.close();
    }

}
