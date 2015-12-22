package Initialisation_BD;

import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;
import java.math.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;
import java.math.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseMain {

	public static void main(String[] args) 
	{
		try
		{
			System.out.println("Mise en place des repertoires");
			Runtime.getRuntime().exec("rm -r " + PREFIX);
			Runtime.getRuntime().exec("mkdir " + PREFIX);		
			Runtime.getRuntime().exec("cp " + PREFIX + "/../" + INIT_NAME + " " + PREFIX); 

			// On commence par completer la BD originale (avec les cles)
			Statement stmt1=DBManager.OpenCreateDB(INIT_FILE);
			RunQuery(stmt1, QUERIES[0]); //Supprime la table Cles
			RunQuery(stmt1, QUERIES[1]); //Cree la table Cles avec Kpub et Kpriv
			RunQuery(stmt1, QUERIES[2]); //Rempli Cles (ID de Groupes/Statuts/Affectations)
			RunQuery(stmt1, QUERIES[36]); //On cree la colonne Liste_groupes dans la table Utilisateurs
			RunQuery(stmt1, QUERIES[51]); //On rajoute une colonne refBD à Donnees
			RunQuery(stmt1, QUERIES[52]); //On rajoute une refBD aleatoire à Donnees
			RunQuery(stmt1, QUERIES[53]); //On rajoute un colonne ID_Cred à Cred_Autorise
			RunQuery(stmt1, QUERIES[54]); //On rajoute un ID_Cred aleatoire à Cred_Autorise
			RunQuery(stmt1, QUERIES[3]); //Rajoute le champ Ksec à la table Types_Utili

			// On copie les tables contenant les noeuds, users et frontales dans une BD TEMP
			// Pour pouvoir la parcourir (et en même temps attacher la BD originale) car sinon
			// on a des problemes de verrouillage de la BD pendant le parcours
			Statement stmt4=DBManager.OpenCreateDB(PREFIX + "/TEMP.sqlite");
			RunQuery(stmt4, QUERIES[5]); //attache initDB
			RunQuery(stmt4, QUERIES[6]); //Copie de la table utilisateurs
			RunQuery(stmt4, QUERIES[7]); //Copie de la table frontales
			RunQuery(stmt4, QUERIES[19]); //Copie de la table des NoeudsTOR
			RunQuery(stmt4, QUERIES[23]); //Copie de la table des Types_Utilisateurs
			RunQuery(stmt4, QUERIES[9]); //Copie de la table des cles
			RunQuery(stmt4, QUERIES[8]); //Detache initDB
			
			
			
			
			ResultSet rs3 = stmt4.executeQuery(QUERIES[4]); //On recupere les Logins 
			String login2 =null;
			while (rs3.next()==true ) {
				login2 = rs3.getString("Login");
				System.out.println("On traite " + login2);
				ResultSet rs4 = stmt1.executeQuery(QUERIES[37] + login2 + "'"); //On recupere les groupes
				String ListG = "";
				while (rs4.next() == true){
					ListG = ListG + "//" + rs4.getString("Groupe");
				}				
				rs4.close();
				RunQuery(stmt1, QUERIES[39] + ListG + QUERIES[40] + login2 + "'"); 
			}
			rs3.close();


			// TODO INSERER ICI LE CALCUL DES CLES PUB/PRIV
			// Il faut iterer sur cles (comme on itere sur les logins)
			
			
			ResultSet rss = stmt4.executeQuery(QUERIES[57]);
			String ID_Cle2=null;
			while (rss.next()==true ) {
				ID_Cle2 = rss.getString("ID_Cle");
				System.out.println("On traite " + ID_Cle2);
				MyRSA rsa = new MyRSA();
				rsa.generateKeyPair();
				byte[] publicKey = rsa.getPublicKeyInBytes();
				byte[] privateKey = rsa.getPrivateKeyInBytes();
				RunQuery(stmt1, QUERIES[58] + publicKey + QUERIES[59] + ID_Cle2 + "'"); 
				RunQuery(stmt1, QUERIES[60] + privateKey + QUERIES[59] + ID_Cle2 + "'");
			} 
			rss.close();
			
			

			// TODO INSERER ICI LE CALCUL DES CLES SECRETES DE TYPE
			// Il faut iterer sur Types_utili (comme on itere sur les logins)
			
			
			ResultSet rss2 = stmt4.executeQuery(QUERIES[61]); //On recupere les rowid 
			String ID =null;
			while (rss2.next()==true){
				ID = rss2.getString("rowid");
				KeyGenerator keyGen = KeyGenerator.getInstance("AES");
				keyGen.init(256); // for example
				SecretKey secretKey = keyGen.generateKey();
				byte[] secretKeyInBytes = secretKey.getEncoded();
				RunQuery(stmt1, QUERIES[63] + secretKeyInBytes + QUERIES[62] + ID + "'");
			}
			rss2.close();
			
			
			// TODO INSERER ICI LE CALCUL DU HASH DU PASSWORD (remplacer la valeur par son hash)

			// On cree les BD des utilisateurs
			ResultSet rs = stmt4.executeQuery(QUERIES[4]); // Recupere les logins
			String login =null;
			System.out.println("On y est");
			while (rs.next()==true ) {
				login = rs.getString("Login");
				// On cree la BD "Securisee". Normalement cette BD devrait être deportee dans un token securise
				// Car elle contient les donnees en clair et les cles secretes et privees....
				Statement stmt2=DBManager.OpenCreateDB(PREFIX + "/" + login + "_SEC.sqlite");
				RunQuery(stmt2, QUERIES[5]); //attache initDB
				RunQuery(stmt2, QUERIES[9]); //Copie de la tables des cles
				RunQuery(stmt2, QUERIES[20] + login +"' " + QUERIES[21] + login +"' " + QUERIES[22] + login +"') "); //Suppression des cles privees qui ne sont pas associees à l'utilisateur
				RunQuery(stmt2, QUERIES[10]); //Copie de la tables des groupes
				RunQuery(stmt2, QUERIES[11]); //Copie de la tables des statuts
				RunQuery(stmt2, QUERIES[12]); //Copie de la tables des affectations
				RunQuery(stmt2, QUERIES[13] + login +"'"); //Copie des donnees de l'utilisateur
				RunQuery(stmt2, QUERIES[14] + login +"'"); //Copie des politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[15] + login +"'"); //Copie des types et cles, politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[16] + login +"'"); //Copie des credentials autorises de l'utilisateur
				RunQuery(stmt2, QUERIES[17] + login +"'"); //Copie du login/passwd de l'utilisateur
				RunQuery(stmt2, QUERIES[18] + login +"'"); //Copie de la frontale de l'utilisateur
				RunQuery(stmt2, QUERIES[19]); //Copie de la tables des NoeudsTOR

				// Creation de la BD non securisee pour l'utilisateur
				Statement stmt3=DBManager.OpenCreateDB(PREFIX + "/" + login+ ".sqlite");
				RunQuery(stmt3, QUERIES[5]); //attache initDB
				RunQuery(stmt3, QUERIES[24] + login +"'"); // Types
				RunQuery(stmt3, QUERIES[25] + login +"'"); //Donnees
			
				//Chiffrement donnees 
				
				
				
				/*RunQuery(stmt3, QUERIES[26]); //Chiffrement donnees (en faux pour l'instant)*/
				RunQuery(stmt3, QUERIES[27] + login +"'"); //U_Cles_Types
				RunQuery(stmt3, QUERIES[28]); //Creation colonne chiffrement credendials
				RunQuery(stmt3, QUERIES[29]); //Chiffrement credentials (en faux pour l'instant)
			}
			rs.close();

			// On cree maintenant les Frontales des clusters
			ResultSet rs2 = stmt4.executeQuery(QUERIES[30]); // Recupere les Frontales
			String frontale =null;
			while (rs2.next()==true){
				frontale = rs2.getString("Frontale");
				//C'est parti !
				Statement stmt5=DBManager.OpenCreateDB(PREFIX + "/" + frontale + ".sqlite");
				RunQuery(stmt5, QUERIES[5]); //attache initDB
				RunQuery(stmt5, QUERIES[31]); //server
				RunQuery(stmt5, QUERIES[32] + frontale +"'"); //table utilisateurs (Statut, Affectation generalises ? Ou desanonymisation ?)
				RunQuery(stmt5, QUERIES[33] + frontale +"'"); //Donnees de base pour Donnees_Chiffrees, Liens et Cles_Types--> provisoire
				RunQuery(stmt5, QUERIES[41]); //Creation colonne Metadonnees
				RunQuery(stmt5, QUERIES[42]); //Remplissage colonne Metadonnees
				RunQuery(stmt5, QUERIES[43]+ frontale +"'"); //Creation Reelle table Donnees_Chiffrees
				RunQuery(stmt5, QUERIES[46] + frontale +"'"); //Table Cles_Types
				RunQuery(stmt5, QUERIES[55]+ frontale +"'"); //Creation table Liens
				RunQuery(stmt5, QUERIES[44]); //Supression table provisoire
				 RunQuery(stmt5, QUERIES[26]); //Chiffrement donnees (en faux pour l'instant)
				//Chiffrement donnees

				
				
				RunQuery(stmt5, QUERIES[45]); //Chiffrement Metadonnees (en faux pour l'instant)
				RunQuery(stmt5, QUERIES[48]); //Remplissage E_Cred_Ksec
				RunQuery(stmt5, QUERIES[49]); //Table Logcom
				RunQuery(stmt5, QUERIES[50]); //Table Routage
			}
			rs2.close();

			// On cree maintenant le Serveur Central
			Statement stmt6 = DBManager.OpenCreateDB(PREFIX + "/Server.sqlite");
			RunQuery(stmt6, QUERIES[5]); //attache initDB
			RunQuery(stmt6, QUERIES[35]); //Copie de la tables des Frontales
			RunQuery(stmt6, QUERIES[49]); //Table LogCOM

			// On cree maintenant les BD Noeud TOR
			// TODO il faut en faire n (ou n est le nombre de lignes dans la table NoeudsTOR
			// TODO il faut aussi ajouter un nom aux noeuds.... (pour que chacun sache qui il est)
			ResultSet rs4 = stmt4.executeQuery(QUERIES[56]); // Recupere les noeuds TOR
			String NoeudTOR =null;
			while (rs4.next()==true){
				NoeudTOR = rs4.getString("NoeudTOR");
				Statement stmt7=DBManager.OpenCreateDB(PREFIX + "/" + NoeudTOR + ".sqlite");
				RunQuery(stmt7, QUERIES[5]); //attache initDB
				RunQuery(stmt7, QUERIES[49]); //Table Logcom
				RunQuery(stmt7, QUERIES[50]); //Table Routage
				RunQuery(stmt7, QUERIES[19]); //Table NoeudsTOR
			}
			rs4.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

	public static void RunQuery ( Statement stmt, String sql)
	{
		System.out.println("Execution de : " + sql);
		try 
		{ 
			stmt.executeUpdate(sql);
		} 
		catch (Exception ex) 
		{ 
			System.out.println("Error: " + ex); 
			ex.printStackTrace();
			System.exit(1);

		}   

	}

	public final static String PREFIX = "Initialisation/datas";
	public final static String INIT_NAME = "InitDB.sqlite";
	public final static String INIT_FILE = PREFIX + "/" + INIT_NAME;
	public static final String[] QUERIES = {
			"DROP TABLE Cles",
			"CREATE TABLE Cles (ID_Cle TEXT(500) PRIMARY KEY  NOT NULL, Kpub BLOB,Kpriv BLOB)",
			"insert into Cles Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle from Statuts UNION Select ID_Cle,'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Groupes UNION Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Affectations",
			"ALTER TABLE Types_Utili ADD COLUMN Ksec BLOB",
			"SELECT Login from Utilisateurs",
			/*5*/
			"ATTACH '"+ INIT_FILE + "' as 'BASE'",
			"create table Utilisateurs as select * from BASE.Utilisateurs",
			"create table Frontales as select * from BASE.Frontales",
			"DETACH 'BASE';",
			"create table Cles as select * from BASE.cles",
			/*10*/
			"create table Groupes as select * from BASE.Groupes",
			"create table Statuts as select * from BASE.Statuts",
			"create table Affectations as select * from BASE.Affectations",
			"create table Donnees_Clair as select Type, Valeur from BASE.Donnees WHERE Login = '",
			"create table Politiques as Select P.Politique, Expression from BASE.Types_Utili T , BASE.Politiques P where P.Politique = T.Politique and Login = '",
			/*15*/
			"create table Types as select Type, Ksec, Dispo, Politique from BASE.Types_Utili WHERE Login = '",
			"create table Cred_Autorise as Select C.Politique, Cred_Auto_Ref from BASE.Types_Utili T , BASE.Cred_Autorise C where C.Politique = T.Politique and Login = '",
			"create table Utilisateurs as Select Login, Password from BASE.Utilisateurs where Login = '",
			"create table Frontales as Select F.IP, F.Port from BASE.Utilisateurs U , BASE.Frontales F where U.Frontale = F.Frontale and Login = '",
			"create table NoeudsTOR as select * from BASE.NoeudsTOR",
			/*20*/
			"Update Cles set Kpriv = NULL where ID_Cle not in (SELECT ID_Cle from BASE.Statuts S, BASE.Utilisateurs U where U.ID_Statut = S.ID_Statut and Login = '",
			"UNION SELECT ID_Cle from BASE.Affectations A, BASE.Utilisateurs U where U.ID_Affectation = A.Affectation and Login = '",
			"UNION SELECT ID_Cle from BASE.Groupes G, BASE.Inscription I where I.ID_Groupe = G.ID_Groupe and Login = '",
			"create table Types_Utili as select * from BASE.Types_Utili",
			"create table Types as select Type from BASE.Types_Utili WHERE Login = '",
			/*25*/
			"create table Donnees_Chiffrees as select Type, Valeur from BASE.Donnees WHERE Login = '",
			"Update Donnees_Chiffrees set Valeur = Valeur || ' + CHIFFREMENT' ",
			"create table U_Cles_Types as select T.Type, Cred_Auto_Ref from BASE.Types_Utili T, BASE.Cred_Autorise C where C.Politique = T.Politique and Login = '",
			"ALTER TABLE U_Cles_Types ADD COLUMN E_Cred_Ksec TEXT",
			"Update U_Cles_Types set E_Cred_Ksec = Cred_Auto_ref || ' + CHIFFREMENT' ",
			/*30*/
			"SELECT Frontale from Frontales",
			"create table Server as select IP, Port from BASE.Server",
			"create table Utilisateurs as select U.IP, U.Port, S.Statut_Gen, A.Affect_Gen from BASE.Utilisateurs U, BASE.Statuts S, BASE.Affectations A where U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and Frontale = '",
			"create table Provisoire as select U.Frontale, D.RefBD, C.ID_Cred, T.Ksec, T.Type, Cred_Auto_Ref, S.Statut_Gen, A.Affect_Gen, D.Valeur, D.Login, A.Affectation, S.Statut, U.Liste_groupes from BASE.Types_Utili T, BASE.Cred_Autorise C, BASE.Utilisateurs U, BASE.Donnees D, BASE.Statuts S, BASE.Affectations A where C.Politique = T.Politique and T.Login = U.Login and U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and U.Login = D.Login and D.Type = T.Type and T.Dispo = 'Toujours' and Frontale = '",
			"SELECT Ksec from Types_utili",
			/*35*/
			"create table Frontales as select * from BASE.Frontales",
			"ALTER TABLE Utilisateurs ADD COLUMN Liste_groupes TEXT(500)",
			"SELECT G.Groupe FROM Groupes G, Utilisateurs U, Inscription I where U.Login = I.Login and I.ID_Groupe = G.ID_Groupe and U.Login = '",
			"UPDATE Types_Utili set Ksec = 'KSEC_' || Login ||'_' || Type",
			"UPDATE Utilisateurs set Liste_groupes = '",
			/*40*/
			"' WHERE Login = '",
			"ALTER TABLE Provisoire ADD COLUMN Metadonnees TEXT(500)",
			"UPDATE Provisoire SET Metadonnees = Login || '//' || Affectation || '//' || Statut || Liste_groupes",
			"create table Donnees_Chiffrees as select distinct RefBD, Type, Statut_Gen, Affect_Gen, Valeur, Metadonnees from Provisoire where Frontale = '",
			"DROP table Provisoire",
			/*45*/
			"Update Donnees_Chiffrees set Metadonnees = Metadonnees || ' + CHIFFREMENT' ",
			"create table Cles_Types as select distinct ID_Cred, Cred_Auto_Ref, Ksec as E_Cred_Ksec from Provisoire where Frontale = '",
			"UPDATE Donnees_Chiffrees set RefBD = ",
			"Update Cles_Types set E_Cred_Ksec = Cred_Auto_ref || '//' || E_Cred_Ksec || ' + CHIFFREMENT' ",
			"CREATE TABLE LogCOM (Date DATETIME, IP_Emmeteur TEXT, Port_Emetteur TEXT, IP_Recepteur TEXT, Port_Recepteur TEXT, MSG TEXT)",
			/*50*/
			"CREATE TABLE Routage (ID_Req TEXT PRIMARY KEY  NOT NULL , IP_Emmeteur TEXT, Port_Emetteur TEXT, MSG TEXT(2000))",
			"ALTER TABLE Donnees ADD COLUMN RefBD INTEGER",
			"UPDATE Donnees set RefBD = random()",
			"ALTER TABLE Cred_Autorise ADD COLUMN ID_Cred INTEGER",
			"UPDATE Cred_Autorise set ID_Cred = random()",
			/*55*/
			"CREATE TABLE Liens as select distinct RefBD, ID_Cred from Provisoire where Frontale = '",
			"SELECT NoeudTOR from NoeudsTOR",
			"SELECT ID_Cle from Cles",/*57*/
			"UPDATE Cles set Kpub = '",/*58*/
			"' WHERE ID_Cle = '",/*59*/
			/*60*/
			"UPDATE Cles set Kpriv = '",
			"SELECT rowid from Types_Utili",/*61*/
			"' WHERE rowid = '",/*62*/
			"UPDATE Types_Utili set Ksec = '",/*63*/
			"",/*64*/
			/*65*/
			"",
	""};

}
