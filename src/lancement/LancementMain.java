package lancement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import initialisation_BD.DBManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;


public class LancementMain {

	public static void main(String[] args) 
	{
		try
		{
			// 1 - ON OUVRE LA BD LANCEMENT ET ON LANCE LES PROCESSUS "LOCAUX"
			//--------------------------------------------------------------------------------
			
			// Chaque processus doit au lancement verifier si sa BD est presente. Dans le cas 
			// contraire, il quitte simplement --> On repartit les processus en repartissant leurs BD

		    PrintWriter ecrivain;
		    ecrivain =  new PrintWriter(new BufferedWriter (new FileWriter("lancement.sh")));
		   
			Connection con=DBManager.OpenCreateDB(initialisation_BD.DatabaseMain.PREFIX + "/Lancement.sqlite");
			Statement stmt=DBManager.CreateStatement(con);
			ResultSet rs = stmt.executeQuery(initialisation_BD.DatabaseMain.QUERIES[33]); // Recupere les logins
			String login =null;
			while (rs.next()==true ) {
				login = rs.getString("Login");
			    if (rs.getInt("Icone") == 0) 
			    	ecrivain.println("xterm -fg green -bg black -e 'java -jar App.jar user " + login +"_SEC.sqlite "  + login +".sqlite' &");
			    else
			    	ecrivain.println("xterm -iconic -fg green -bg black -e 'java -jar App.jar user " + login +"_SEC.sqlite "  + login +".sqlite' &");
			}
			rs = stmt.executeQuery(initialisation_BD.DatabaseMain.QUERIES[39]); // Recupere les frontales
			String frontale =null;
			while (rs.next()==true ) {
				frontale = rs.getString("Frontale");
			    if (rs.getInt("Icone") == 0) 
					ecrivain.println("xterm -fg white -bg black -e 'java -jar App.jar frontal " + frontale +".sqlite' &");
				else
					ecrivain.println("xterm -iconic -fg white -bg black -e 'java -jar App.jar frontal " + frontale +".sqlite' &");
			}
		    ecrivain.println("xterm -fg red -bg black -e 'java -jar App.jar server server.sqlite' &");
			ecrivain.close();
			Runtime.getRuntime().exec("sh lancement.sh");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

}