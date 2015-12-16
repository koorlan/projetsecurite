package Initialisation_BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBManager {

	private static Statement stmt =null;
	
	static Connection con = null; 
	
	public static Statement OpenCreateDB(String db){
		System.out.println("Ouverture/création de la base de données " + db); 
		try 
		{ 
			Class.forName("org.sqlite.JDBC"); 
			con = DriverManager.getConnection("jdbc:sqlite:" + db); 
			stmt = con.createStatement(); 
			System.out.println("Ouverture de '"+ db +"' OK");
		} 
		catch (Exception ex) 
		{ 
			System.out.println("Problème d'ouverture de la BD."); 
			ex.printStackTrace();
			System.exit(1); 
		}
		return stmt;
	}
}
