package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionBase1 {
	
	public static Connection getConncetion() {
		
		try {
			
			String sqlDriver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "tiago";
			String senha = "senha";
			
			Class.forName(sqlDriver);
			Connection conn = DriverManager.getConnection(url,user,senha);
			return conn;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
