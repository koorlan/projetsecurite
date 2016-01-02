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
			    	ecrivain.println("xterm -fg green -bg black -e 'java -jar user.jar " + login + "' &");
			    else
			    	ecrivain.println("xterm -iconic -fg green -bg black -e 'java -jar user.jar " + login + "' &");
			}
			rs = stmt.executeQuery(initialisation_BD.DatabaseMain.QUERIES[63]); // Recupere les frontales
			String frontale =null;
			while (rs.next()==true ) {
				frontale = rs.getString("Frontale");
			    if (rs.getInt("Icone") == 0) 
					ecrivain.println("xterm -fg blue -bg black -e 'java -jar frontale.jar " + frontale + "' &");
				else
					ecrivain.println("xterm -iconic -fg blue -bg black -e 'java -jar frontale.jar " + frontale + "' &");
			}
			rs = stmt.executeQuery(initialisation_BD.DatabaseMain.QUERIES[62]); // Recupere les NoeudsTOR
			String noeud =null;
			while (rs.next()==true ) {
				noeud = rs.getString("NoeudTOR");
			    if (rs.getInt("Icone") == 0) 
					ecrivain.println("xterm -fg yellow -bg black -e 'java -jar noeud.jar " + noeud + "' &");
				else
					ecrivain.println("xterm -iconic -fg yellow -bg black -e 'java -jar noeud.jar " + noeud + "' &");
			}
		    ecrivain.println("xterm -fg red -bg black -e 'java -jar server.jar Server' &");
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