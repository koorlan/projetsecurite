package Initialisation_BD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import javax.crypto.*;

import crypto.AES;
import crypto.MyRSA;

import java.util.StringTokenizer;


public class DatabaseMain {

	public static void main(String[] args) 
	{
		try
		{
			System.out.println("Mise en place des repertoires");
			Runtime.getRuntime().exec("rm -r " + PREFIX);
			Runtime.getRuntime().exec("mkdir " + PREFIX);		
			//Runtime.getRuntime().exec(new String[] {"mkdir ",PREFIX});	
			Runtime.getRuntime().exec("cp " + PREFIX + "/../" + INIT_NAME + " " + PREFIX + "/" + BD_COMPLETE); 

			// 1 - ON COMPLETE LA BD INITIALE AVEC TOUTES LES INFORMATIONS DEDUITES/CALCULEES
			//--------------------------------------------------------------------------------
			Connection con1=DBManager.OpenCreateDB(INIT_FILE);
			Statement stmt1=DBManager.CreateStatement(con1);

			// Création et remplissage de la table Cles
			RunQuery(stmt1, QUERIES[0]); //Cree la table Cles avec ID_Cle, Kpub et Kpriv
			RunQuery(stmt1, QUERIES[1]); //Rempli la table Cles (ID de Groupes/Statuts/Affectations)

			// Les deux ID aleatoires ci-dessous sont utiles pour associer les donnees chiffrees aux Cles_Types sur la BD frontale
			RunQuery(stmt1, QUERIES[2]); //On rajoute une colonne refBD a Donnees
			RunQuery(stmt1, QUERIES[3]); //On rempli refBD avec une valeur aleatoire dans la table Donnees
			RunQuery(stmt1, QUERIES[4]); //On rajoute une colonne ID_Cred a Cred_Autorise
			RunQuery(stmt1, QUERIES[5]); //On rempli ID_Cred avec une valeur aleatoire dans Cred_Autorise

			//Rajout des colonnes (en rouge dans le PPT)
			RunQuery(stmt1, QUERIES[6]); //Rajoute le champ Ksec à la table Types_Utili
			RunQuery(stmt1, QUERIES[7]); //Rajoute le champ E_Cred_Ksec à la table Types_Utili
			RunQuery(stmt1, QUERIES[8]); //On cree la colonne Metadonnees dans la table Utilisateurs
			RunQuery(stmt1, QUERIES[9]); //On cree la colonne Meta_Chiffrees dans la table Types_Utili
			RunQuery(stmt1, QUERIES[10]); //On cree la colonne Valeur_Chiffree dans la table Donnees
			// TODO INSERER ICI LE CALCUL DU HASH DU PASSWORD (remplacer la valeur par son hash)

			// 2 - DEDUCTION DE DONNEES DE BASE (GSA utilisateurs)
			//--------------------------------------------------------------------------------
			// TODO rajouter les GSA comme donnees-> Fait via SQLiteManager

			// 3 - GENERATION DES CLES PUBLIQUES/PRIVEES ET STOCKAGE DANS LA TABLE Cles
			//--------------------------------------------------------------------------------
			ResultSet rss = stmt1.executeQuery(QUERIES[11]);
			String ID_Cle2=null;
			while (rss.next()==true ) {
				ID_Cle2 = rss.getString("ID_Cle");
				MyRSA rsa = new MyRSA();
				rsa.generateKeyPair();
				String encodedPublicKey = Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded());
				String encodedPrivateKey = Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded());
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[12]);
				pstmt.setString(1, encodedPublicKey);
				pstmt.setString(2, encodedPrivateKey);
				pstmt.setString(3, ID_Cle2);
				pstmt.execute();
			} 
			rss.close();

			// 4 - GENERATION DES CLES SECRETES DE TYPE
			//--------------------------------------------------------------------------------
			rss = stmt1.executeQuery(QUERIES[13]); //On recupere les rowid 
			String ID =null;
			while (rss.next()==true){
				ID = rss.getString("rowid");
				KeyGenerator keyGen = KeyGenerator.getInstance("AES");
				keyGen.init(256); // for example
				SecretKey secretKey = keyGen.generateKey();
				String encodedSecreteKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[14]);
				pstmt.setString(1, encodedSecreteKey);
				pstmt.setString(2, ID);
				pstmt.execute();
			}
			rss.close();

			// 5 - CHIFFREMENT DES DONNEES AVEC KSEC
			//--------------------------------------------------------------------------------
			rss = stmt1.executeQuery(QUERIES[15]);
			while (rss.next()==true){
				String aesKeyString = rss.getString("Ksec");
				int ID_Donnee = rss.getInt("ID_Donnee");
				System.out.println("On chiffre donnée n° " + ID_Donnee);
				byte[] valeur = rss.getString("Valeur").getBytes();
				byte[] aesKey = Base64.getDecoder().decode(aesKeyString);
				AES aes;
				aes = new AES(aesKey);
				String encodedEncryptedValue = Base64.getEncoder().encodeToString(aes.chiffrerMess(valeur));
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[16]);
				pstmt.setString(1, encodedEncryptedValue);
				pstmt.setInt(2, ID_Donnee);
				pstmt.execute();
			}
			rss.close();


			// 6 - CALCUL DES METADONNEES ET STOCKAGE DANS LA TABLE Utilisateurs
			//--------------------------------------------------------------------------------
			//  Calcul de la list des groupes puis des metadonnees de chaque utilisateur
			Statement stmt9=DBManager.CreateStatement(con1);
			rss = stmt9.executeQuery(QUERIES[17]); //On recupere les Logins, statut, affectation
			String login2 =null;
			while (rss.next()==true ) {
				login2 = rss.getString("Login");
				String statut  = rss.getString("Statut"); 
				String affectation = rss.getString("Affectation");
				ResultSet rss1 = stmt1.executeQuery(QUERIES[18] + login2 + "'"); //On recupere les groupes 
				String Meta = "";
				while (rss1.next() == true){
					Meta += rss1.getString("Groupe")+ "::";
				}		
				Meta=login2 + "::" + Meta + statut + "::" + affectation;
				rss1.close();
				RunQuery(stmt1, QUERIES[19] + Meta + QUERIES[20] + login2 + "'"); 
			}
			rss.close();


			// 7 - CHIFFREMENT DES METADONNEES AVEC LES KSec DANS LA TABLE Type_Utili
			//--------------------------------------------------------------------------------
			rss = stmt1.executeQuery(QUERIES[21]);
			while (rss.next()==true){
				String aesKeyString = rss.getString("Ksec");
				byte[] Metadonnees = rss.getString("Metadonnees").getBytes();
				System.out.println("On chiffre métadonnée KSEC " + aesKeyString);
				byte[] aesKey = Base64.getDecoder().decode(aesKeyString);
				AES aes;
				aes = new AES(aesKey);
				String encodedEncryptedValue = Base64.getEncoder().encodeToString(aes.chiffrerMess(Metadonnees));
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[22]);
				pstmt.setString(1, encodedEncryptedValue);
				pstmt.setString(2, aesKeyString); 
				pstmt.execute();
			}
			rss.close();


			// 8 - CALCUL DE E_Cred_KSec: CHIFFREMENT DE KSec avec les Kpub (CREDENTIALS) DANS LA TABLE Types_Utili
			//-------------------------------------------------------------------------------------------------------

			rss = stmt1.executeQuery(QUERIES[23]);// recupere KSec et Cred_Auto_Ref
			while (rss.next()==true){
				String KsecString= rss.getString("Ksec");
				byte[] Ksec = Base64.getDecoder().decode(KsecString);
				String CAR = rss.getString("Cred_Auto_Ref");
				byte[] cleSecChiffree = Ksec;

				StringTokenizer creds = new StringTokenizer(CAR, ",");
				while (creds.hasMoreTokens()) {
					String cred = creds.nextToken();
					ResultSet rss5= stmt9.executeQuery(QUERIES[24]+ cred +"'");//recupere les Kpub de la table Cle ou ID_Cle = Cred_Auto_Ref
					String ss = rss5.getString("Kpub");
					byte[] Kpub = Base64.getDecoder().decode(ss);
					MyRSA rsa = new MyRSA();
					rsa.setPublicKey(Kpub);
					cleSecChiffree = rsa.crypt(cleSecChiffree); 
					rss5.close();
				}
				String encodedEncryptedKsec = Base64.getEncoder().encodeToString(cleSecChiffree);
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[25]);
				pstmt.setString(1, encodedEncryptedKsec);
				pstmt.setString(2, KsecString); 
				pstmt.execute();
			}

			// 9 - CREATION D'UNE BD TEMPORAIRE
			//-------------------------------------------------------------------------------------------------------
			// On copie les tables contenant les noeuds, users et frontales dans une BD TEMP
			// Pour pouvoir la parcourir (et en même temps attacher la BD originale) car sinon
			// on a des problemes de verrouillage de la BD pendant le parcours
			Connection con4=DBManager.OpenCreateDB(PREFIX + "/TEMP.sqlite");
			Statement stmt4=DBManager.CreateStatement(con4);
			RunQuery(stmt4, QUERIES[26]); //attache initDB
			RunQuery(stmt4, QUERIES[27]); //Copie de la table utilisateurs
			RunQuery(stmt4, QUERIES[28]); //Copie de la table frontales
			RunQuery(stmt4, QUERIES[29]); //Copie de la table des NoeudsTOR
			RunQuery(stmt4, QUERIES[30]); //Copie de la table des Types_Utilisateurs
			RunQuery(stmt4, QUERIES[31]); //Copie de la table des cles
			RunQuery(stmt4, QUERIES[32]); //Detache initDB


			// 10 - CREATION DES BD DES UTILISATEURS
			//-------------------------------------------------------------------------------------------------------
			ResultSet rs = stmt4.executeQuery(QUERIES[33]); // Recupere les logins
			String login =null;
			while (rs.next()==true ) {
				login = rs.getString("Login");
				// On cree la BD "Protegee". Normalement cette BD devrait être deportee dans un token securise
				// Car elle contient les donnees en clair et les cles secretes et privees....
				Connection con2=DBManager.OpenCreateDB(PREFIX + "/" + login + "_SEC.sqlite");
				Statement stmt2=DBManager.CreateStatement(con2);
				RunQuery(stmt2, QUERIES[26]); //attache initDB
				RunQuery(stmt2, QUERIES[31]); //Copie de la tables des cles
				//Suppression des cles privees qui ne sont pas associees à l'utilisateur en gardant celles du chemin hierarchique
				RunQuery(stmt2, QUERIES[34] + login +"' " + QUERIES[35] + login +"' " + QUERIES[36] + login +"' " + QUERIES[37] + login +"' " + QUERIES[38] + login +"' " + QUERIES[39] + login +"') "); 
				RunQuery(stmt2, QUERIES[40]); //Copie de la tables des groupes
				RunQuery(stmt2, QUERIES[41]); //Copie de la tables des statuts
				RunQuery(stmt2, QUERIES[42]); //Copie de la tables des affectations
				RunQuery(stmt2, QUERIES[43] + login +"'"); //Copie des donnees de l'utilisateur
				RunQuery(stmt2, QUERIES[44] + login +"'"); //Copie des politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[45] + login +"'"); //Copie des types et cles, politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[46] + login +"'"); //Copie des credentials autorises de l'utilisateur
				RunQuery(stmt2, QUERIES[47] + login +"'"); //Copie du login/passwd/ip/port de l'utilisateur
				RunQuery(stmt2, QUERIES[48] + login +"'"); //Copie de la frontale de l'utilisateur
				RunQuery(stmt2, QUERIES[29]); //Copie de la tables des NoeudsTOR
				RunQuery(stmt2, QUERIES[32]); //Detache initDB

				// Creation de la BD non securisee pour l'utilisateur
				Connection con3=DBManager.OpenCreateDB(PREFIX + "/" + login+ ".sqlite");
				Statement stmt3=DBManager.CreateStatement(con3);
				RunQuery(stmt3, QUERIES[26]); //attache initDB
				RunQuery(stmt3, QUERIES[49] + login +"'"); // Types
				RunQuery(stmt3, QUERIES[50] + login +"'"); //Donnees_Chiffree
				RunQuery(stmt3, QUERIES[51] + login +"'"); //Cles_Types
				RunQuery(stmt3, QUERIES[32]); //Detache initDB

			}

			// 11 - CREATION DES FRONTALES
			//-------------------------------------------------------------------------------------------------------
			ResultSet rs2 = stmt4.executeQuery(QUERIES[63]); // Recupere les Frontales
			String frontale =null;
			while (rs2.next()==true){
				frontale = rs2.getString("Frontale");
				//C'est parti !
				Connection con5=DBManager.OpenCreateDB(PREFIX + "/" + frontale + ".sqlite");
				Statement stmt5=DBManager.CreateStatement(con5);
				RunQuery(stmt5, QUERIES[26]); //attache initDB
				RunQuery(stmt5, QUERIES[52]); //server
				RunQuery(stmt5, QUERIES[53] + frontale + "'"); //Frontal
				RunQuery(stmt5, QUERIES[54] + frontale + "'");	//Frontales
				RunQuery(stmt5, QUERIES[55] + frontale +"'"); //table utilisateurs (Statut, Affectation generalises ? Ou desanonymisation ?)
				RunQuery(stmt5, QUERIES[56]+ frontale +"'"); //Creation Reelle table Donnees_Chiffrees
				RunQuery(stmt5, QUERIES[57] + frontale +"'"); //Table Cles_Types
				RunQuery(stmt5, QUERIES[58]+ frontale +"'"); //Creation table Liens
				RunQuery(stmt5, QUERIES[59]); //Table Routage
				RunQuery(stmt5, QUERIES[32]); //Detache initDB
			}
			rs2.close();

			// 12 - CREATION DU SERVEUR CENTRAL
			//-------------------------------------------------------------------------------------------------------
			Connection con6=DBManager.OpenCreateDB(PREFIX + "/Server.sqlite");
			Statement stmt6=DBManager.CreateStatement(con6);
			RunQuery(stmt6, QUERIES[26]); //attache initDB
			RunQuery(stmt6, QUERIES[60]); //Copie de la tables des Frontales
			RunQuery(stmt6, QUERIES[61]);  //Table Server
			RunQuery(stmt6, QUERIES[32]); //Detache initDB

	
			// 13 - CREATION DES NOEUDS TOR
			//-------------------------------------------------------------------------------------------------------
			ResultSet rs4 = stmt4.executeQuery(QUERIES[62]); // Recupere les noeuds TOR
			String NoeudTOR =null;
			while (rs4.next()==true){
				NoeudTOR = rs4.getString("NoeudTOR");
				Connection con7=DBManager.OpenCreateDB(PREFIX + "/" + NoeudTOR + ".sqlite");
				Statement stmt7=DBManager.CreateStatement(con7);
				RunQuery(stmt7, QUERIES[26]); //attache initDB
				RunQuery(stmt7, QUERIES[59]); //Table Routage
				RunQuery(stmt7, QUERIES[29]); //Table NoeudsTOR
				RunQuery(stmt7, QUERIES[32]); //Detache initDB
			}
			rs4.close();

			// 14 - CREATION DE LA BASE POUR LE LANCEMENT
			//-------------------------------------------------------------------------------------------------------
			Connection con8=DBManager.OpenCreateDB(PREFIX + "/Lancement.sqlite");
			Statement stmt8=DBManager.CreateStatement(con8);
			RunQuery(stmt8, QUERIES[26]); //attache initDB
			RunQuery(stmt8, QUERIES[64]); //Copie de la liste des utilisateurs
			RunQuery(stmt8, QUERIES[65]); //Copie de la liste des frontales
			RunQuery(stmt8, QUERIES[66]); //Copie de la liste des Noeuds TOR
			RunQuery(stmt8, QUERIES[32]); //Detache initDB

			// 15 - SUPPRESSION DES BASES INIT_DB COMPLETE ET DE LA BASE TEMP
			//-------------------------------------------------------------------------------------------------------
			Runtime.getRuntime().exec("rm " + INIT_FILE); 
			Runtime.getRuntime().exec("rm " + PREFIX + "/TEMP.sqlite");
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
	public final static String BD_COMPLETE = "CompleteDB.sqlite";
	
	public final static String INIT_FILE = PREFIX + "/" + BD_COMPLETE;
	public static final String[] QUERIES = {
			"CREATE TABLE Cles (ID_Cle TEXT(500) PRIMARY KEY, Kpub TEXT,Kpriv TEXT)",//0
			"insert into Cles Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle from Statuts UNION Select ID_Cle,'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Groupes "
			+ "UNION Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Affectations",//1
			"ALTER TABLE Donnees ADD COLUMN RefBD INTEGER", //2
			"UPDATE Donnees set RefBD = random()",//3
			"ALTER TABLE Cred_Autorise ADD COLUMN ID_Cred INTEGER",//4
			"UPDATE Cred_Autorise set ID_Cred = random()",//5
			"ALTER TABLE Types_Utili ADD COLUMN Ksec TEXT",//6
			"ALTER TABLE Types_Utili ADD COLUMN E_Cred_Ksec TEXT",//7
			"ALTER TABLE Utilisateurs ADD COLUMN Metadonnees TEXT",//8
			"ALTER TABLE Types_Utili ADD COLUMN Meta_Chiffrees TEXT",//9
			"ALTER TABLE Donnees ADD COLUMN Valeur_Chiffree TEXT",//10
			
			"SELECT ID_Cle from Cles",//11
			"UPDATE Cles set Kpub = ?, Kpriv = ? WHERE ID_CLe = ?",//12
			"SELECT rowid from Types_Utili",//13
			"UPDATE Types_Utili set Ksec = ? WHERE rowid = ?",//14
			"SELECT ID_Donnee, Valeur, Ksec FROM Donnees D, Types_Utili T WHERE D.Login = T.Login AND D.Type = T.Type",//15
			"UPDATE Donnees SET Valeur_Chiffree = ? WHERE ID_Donnee = ?",//16
			"SELECT Login, Statut, Affectation  FROM Utilisateurs U, Statuts S, Affectations A WHERE U.ID_Statut=S.ID_Statut and U.ID_Affectation=A.ID_Affectation",//17
			"SELECT G.Groupe FROM Groupes G, Utilisateurs U, Inscription I where U.Login = I.Login and I.ID_Groupe = G.ID_Groupe and U.Login = '",//18
			"UPDATE Utilisateurs SET Metadonnees = '",//19
			"' WHERE Login = '",//20
			
			"SELECT Metadonnees, Ksec FROM Utilisateurs U, Types_Utili T WHERE U.Login = T.Login",//21
			"UPDATE Types_Utili SET Meta_Chiffrees = ? WHERE Ksec = ?",//22
			"SELECT Ksec, Cred_Auto_Ref from Types_Utili T, Cred_Autorise C WHERE T.Politique = C.Politique",//23
			"SELECT Kpub from Cles where Cles.ID_Cle = '",//24
			"UPDATE Types_Utili SET E_Cred_Ksec = ? WHERE Ksec = ?",//25
			"ATTACH '"+ INIT_FILE + "' as 'BASE'",//26
			"create table Utilisateurs as select * from BASE.Utilisateurs",//27
			"create table Frontales as select * from BASE.Frontales",//28
			"create table NoeudsTOR as select * from BASE.NoeudsTOR",//29
			"create table Types_Utili as select * from BASE.Types_Utili",//30
			
			"create table Cles as select * from BASE.cles",//31
			"DETACH 'BASE';",//32
			"SELECT Login, Icone FROM Utilisateurs",//33
			"Update Cles SET Kpriv = NULL WHERE ID_Cle not in (SELECT ID_Cle from BASE.Statuts S, BASE.Utilisateurs U where U.ID_Statut = S.ID_Statut and Login = '",//34
			"UNION SELECT P.ID_Parent from BASE.Utilisateurs U, BASE.Statuts S, BASE.Predecesseurs_Cle P where U.ID_Statut = S.ID_Statut and S.ID_Cle = P.ID_Cle and Login = '",//35
			"UNION SELECT ID_Cle from BASE.Affectations A, BASE.Utilisateurs U where U.ID_Affectation = A.ID_Affectation and Login = '",//36
			"UNION SELECT P.ID_Parent from BASE.Utilisateurs U, BASE.Affectations A, BASE.Predecesseurs_Cle P where U.ID_Affectation = A.ID_Affectation and A.ID_Cle = P.ID_Cle and Login = '",//37
			"UNION SELECT ID_Cle from BASE.Groupes G, BASE.Inscription I where I.ID_Groupe = G.ID_Groupe and Login = '",//38
			"UNION SELECT P.ID_Parent from BASE.Inscription I, BASE.Groupes G, BASE.Predecesseurs_Cle P where I.ID_Groupe = G.ID_Groupe and G.ID_Cle = P.ID_Cle and Login = '",//39
			"create table Groupes as select * from BASE.Groupes",//40
			
			"create table Statuts as select * from BASE.Statuts",//41
			"create table Affectations as select * from BASE.Affectations",//42
			"create table Donnees_Clair as select Type, Valeur from BASE.Donnees WHERE Login = '",//43
			"create table Politiques as Select P.Politique, Expression from BASE.Types_Utili T , BASE.Politiques P where P.Politique = T.Politique and Login = '",//44
			"create table Types as select Type, Ksec, Dispo, Politique from BASE.Types_Utili WHERE Login = '",//45
			"create table Cred_Autorise as Select C.Politique, Cred_Auto_Ref from BASE.Types_Utili T , BASE.Cred_Autorise C where C.Politique = T.Politique and Login = '",//46
			"create table Utilisateurs as Select Login, Password, Ip, Port, ID_Statut, ID_Affectation from BASE.Utilisateurs where Login = '",//47
			"create table Frontales as Select F.IP, F.internalPort from BASE.Utilisateurs U , BASE.Frontales F where U.Frontale = F.Frontale and Login = '",//48
			"create table Types as select Type, Meta_Chiffrees from BASE.Types_Utili WHERE Login = '",//49
			"create table Donnees_Chiffrees as select Type, Valeur_Chiffree from BASE.Donnees WHERE Login = '",//50
			
			
			"CREATE TABLE Cles_Types AS select Type, Cred_Auto_Ref,E_Cred_Ksec FROM BASE.Types_Utili T, BASE.Cred_Autorise C where T.politique=C.politique and Login = '",//51
			"create table Server as select * from BASE.Server",//52
			"CREATE TABLE Frontale as SELECT * from BASE.Frontales WHERE Frontale = '",//53
			"CREATE TABLE Frontales as SELECT * from BASE.Frontales WHERE Frontale != '",//54
			"create table Utilisateurs as select U.IP, U.Port, S.Statut_Gen, A.Affect_Gen from BASE.Utilisateurs U, BASE.Statuts S, BASE.Affectations A "
			+ "where U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and Frontale = '",//55
			"CREATE TABLE Donnees_Chiffrees AS SELECT RefBD, D.Type, Statut_Gen, Affect_Gen, Valeur_Chiffree, Meta_Chiffrees FROM BASE.Utilisateurs U,"
			+ " BASE.Donnees D, BASE.Statuts S, BASE.Affectations A, BASE.Types_Utili T WHERE T.Login = U.Login AND U.ID_Statut = S.ID_Statut "
			+ "AND U.ID_Affectation = A.ID_Affectation AND U.Login= D.Login AND D.Type = T.Type and  Frontale = '",//56
			"CREATE TABLE Cles_Types AS select distinct ID_Cred, Cred_Auto_Ref,E_Cred_Ksec FROM BASE.Types_Utili T, BASE.Cred_Autorise C, "
			+ "BASE.Utilisateurs U where U.Login = T.Login AND T.politique=C.politique AND Frontale = '",//57
			"CREATE TABLE Liens AS SELECT RefBD, ID_Cred FROM BASE.Donnees D, BASE.Types_Utili T, BASE.Cred_Autorise C, BASE.Utilisateurs U"
			+ " WHERE T.Login = U.Login AND T.Login = D.Login AND T.Type = D.Type AND T.Politique = C.Politique AND Frontale = '",//58
			"CREATE TABLE Routage (ID_Req TEXT PRIMARY KEY  NOT NULL , IP_Emmeteur TEXT, Port_Emetteur TEXT, MSG TEXT(2000))",//59
			"create table Frontales as select Frontale,Famille,IP,externalPort from BASE.Frontales", //60
			
			
			"CREATE TABLE Server as SELECT * from BASE.Server ",//61
			"SELECT NoeudTOR, Icone from NoeudsTOR",//62
			"SELECT Frontale, Icone from Frontales", //63
			"CREATE Table Utilisateurs as SELECT Login, Icone from BASE.Utilisateurs",
			"CREATE Table Frontales as SELECT Frontale, Icone from BASE.Frontales",
			"CREATE Table NoeudsTOR as SELECT NoeudTOR, Icone from BASE.NoeudsTOR",
			""};
}
