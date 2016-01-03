package process_to_complete;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import initialisation_BD.DBManager;

public class frontale {
	public static void main (String[]  args) throws InterruptedException{
		try
		{
			// ON TEST SI LA BD CORRESPONDANTE A CE PROCESSUS EXISTE
			String Ma_BD = initialisation_BD.DatabaseMain.PREFIX + "/" + args[0] + ".sqlite";
			File f1 = new File(Ma_BD); 
			if(f1.exists())
			{
				// LE FICHIER BD EXISTE, ON L'OUVRE
				Connection con=DBManager.OpenCreateDB(Ma_BD);
				Statement stmt=DBManager.CreateStatement(con);
				ResultSet rs = stmt.executeQuery("Select * from Frontales");
				String IP = rs.getString("IP");
				int Port = rs.getInt("InternalPort"); 
				
				// ICI DEBUTE L'APPLICATION USER (INTERFACE)
				for (int j = 0; j < 10; j++) {
					System.out.println("Je suis la frontale " + args[0] + "  IP : " + IP + "  Internal Port : " + Port);
					Thread.sleep(10000);
				}

			}
			// SI PAS DE BD
			System.exit(0);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}
}