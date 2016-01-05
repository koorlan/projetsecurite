package initialisation_BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBManager {

	
	private static Connection con = null; 
	private static Statement stmt =null;
	
	public static Connection OpenCreateDB(String db){
		System.out.println("Ouverture/création de la base de données " + db); 
		try 
		{ 
			Class.forName("org.sqlite.JDBC"); 
			con = DriverManager.getConnection("jdbc:sqlite:" + db); 
			System.out.println("Ouverture de '"+ db +"' OK");
		} 
		catch (Exception ex) 
		{ 
			System.out.println("Problème d'ouverture de la BD."); 
			ex.printStackTrace();
			System.exit(1); 
		}
		return con;
	}
	
	public static Statement CreateStatement(Connection con){
		try 
		{ 
			stmt = con.createStatement(); 
			System.out.println("Creation du statement OK");
		} 
		catch (Exception ex) 
		{ 
			System.out.println("Problème de création du statement"); 
			ex.printStackTrace();
			System.exit(1); 
		}
		return stmt;
	}
}
