
package database;

import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseMain {

	public static void main(String[] args) 
	{
		try
		{
			// On commence par compléter la BD originale (avec les clés)
			Statement stmt1=DBManager.OpenCreateDB(INIT_FILE);
			RunQuery(stmt1, QUERIES[0]); //Supprime la table Cles
			RunQuery(stmt1, QUERIES[1]); //Cree la table Cles avec Kpub et Kpriv
			RunQuery(stmt1, QUERIES[2]); //Rempli Cles (ID de Groupes/Statuts/Affectations)
			RunQuery(stmt1, QUERIES[36]); //On crée la colonne Liste_groupes dans la table Utilisateurs
			
			// On copie les tables contenant les noeuds, users et frontales dans une BD TEMP
			// Pour pouvoir la parcourir (et en même temps attacher la BD originale) car sinon
			// on a des problèmes de verrouillage de la BD pendant le parcours
			Statement stmt4=DBManager.OpenCreateDB(PREFIX + "TEMP.sqlite");
			RunQuery(stmt4, QUERIES[5]); //attache initDB
			RunQuery(stmt4, QUERIES[6]); //Copie de la table utilisateurs
			RunQuery(stmt4, QUERIES[7]); //Copie de la table frontales
			RunQuery(stmt4, QUERIES[19]); //Copie de la table des NoeudsTOR
			RunQuery(stmt4, QUERIES[23]); //Copie de la table des Types_Utilisateurs
			RunQuery(stmt4, QUERIES[9]); //Copie de la table des clés
			RunQuery(stmt4, QUERIES[8]); //Detache initDB

			ResultSet rs3 = stmt4.executeQuery(QUERIES[4]); //On récupère les Logins 
			String login2 =null;
			while (rs3.next()==true ) {
				login2 = rs3.getString("Login");
				System.out.println("On traite " + login2);
				ResultSet rs4 = stmt1.executeQuery(QUERIES[37] + login2 + "'"); //On récupère les groupes
				String ListG = "";
				while (rs4.next() == true){
					ListG = ListG + "//" + rs4.getString("Groupe");
				}				
				rs4.close();
				RunQuery(stmt1, QUERIES[39] + ListG + QUERIES[40] + login2 + "'"); 
			}
			rs3.close();
		
			
			// TODO INSERER ICI LE CALCUL DES CLES PUB/PRIV
			// Il faut itérer sur cles (comme on itere sur les logins)

			RunQuery(stmt1, QUERIES[3]); //Rajoute le champ Ksec à la table Types_Utili
			// TODO INSERER ICI LE CALCUL DES CLES SECRETES DE TYPE
			// Il faut itérer sur Types_utili (comme on itere sur les logins)
			/*ResultSet rs3 = stmt4.executeQuery(QUERIES[35]); // Récupère les types utilisateur
			String Ksec =null;
			while (rs3.next()==true ) {
				Ksec = rs3.getString("Ksec");
			 */
			// TODO INSERER ICI LE CALCUL DU HASH DU PASSWORD (remplacer la valeur par son hash)

			// On crée les BD des utilisateurs
			ResultSet rs = stmt4.executeQuery(QUERIES[4]); // Récupère les logins
			String login =null;
			System.out.println("On y est");
			while (rs.next()==true ) {
				login = rs.getString("Login");
				// On crée la BD "Sécurisée". Normalement cette BD devrait être déportée dans un token sécurisé
				// Car elle contient les données en clair et les clés secrètes et privées....
				Statement stmt2=DBManager.OpenCreateDB(PREFIX + login + "_SEC.sqlite");
				RunQuery(stmt2, QUERIES[5]); //attache initDB
				RunQuery(stmt2, QUERIES[9]); //Copie de la tables des cles
				RunQuery(stmt2, QUERIES[20] + login +"' " + QUERIES[21] + login +"' " + QUERIES[22] + login +"') "); //Suppression des clés privées qui ne sont pas associées à l'utilisateur
				RunQuery(stmt2, QUERIES[10]); //Copie de la tables des groupes
				RunQuery(stmt2, QUERIES[11]); //Copie de la tables des statuts
				RunQuery(stmt2, QUERIES[12]); //Copie de la tables des affectations
				RunQuery(stmt2, QUERIES[13] + login +"'"); //Copie des données de l'utilisateur
				RunQuery(stmt2, QUERIES[14] + login +"'"); //Copie des politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[15] + login +"'"); //Copie des types et clés, politiques de l'utilisateur
				RunQuery(stmt2, QUERIES[16] + login +"'"); //Copie des credentials autorisés de l'utilisateur
				RunQuery(stmt2, QUERIES[17] + login +"'"); //Copie du login/passwd de l'utilisateur
				RunQuery(stmt2, QUERIES[18] + login +"'"); //Copie de la frontale de l'utilisateur
				RunQuery(stmt2, QUERIES[19]); //Copie de la tables des NoeudsTOR

				// Création de la BD non sécurisée pour l'utilisateur
				Statement stmt3=DBManager.OpenCreateDB(PREFIX + login+ ".sqlite");
				RunQuery(stmt3, QUERIES[5]); //attache initDB
				RunQuery(stmt3, QUERIES[24] + login +"'"); // Types
				RunQuery(stmt3, QUERIES[25] + login +"'"); //Données
				RunQuery(stmt3, QUERIES[26]); //Chiffrement données (en faux pour l'instant)
				RunQuery(stmt3, QUERIES[27] + login +"'"); //U_Cles_Types
				RunQuery(stmt3, QUERIES[28]); //Création colonne chiffrement crédendials
				RunQuery(stmt3, QUERIES[29]); //Chiffrement crédentials (en faux pour l'instant)
			}
			rs.close();

			// On crée maintenant les Frontales des clusters
			ResultSet rs2 = stmt4.executeQuery(QUERIES[30]); // Récupère les Frontales
			String frontale =null;
			while (rs2.next()==true){
				frontale = rs2.getString("Frontale");
				//C'est parti !
				Statement stmt5=DBManager.OpenCreateDB(PREFIX + frontale + ".sqlite");
				RunQuery(stmt5, QUERIES[5]); //attache initDB
				RunQuery(stmt5, QUERIES[31]); //server
				RunQuery(stmt5, QUERIES[32] + frontale +"'"); //table utilisateurs (Statut, Affectation généralisés ? Ou desanonymisation ?)
				RunQuery(stmt5, QUERIES[33] + frontale +"'"); //table Données_Chiffrees provisoires
				RunQuery(stmt5, QUERIES[41]); //Création colonne Metadonnées
				RunQuery(stmt5, QUERIES[42]); //Remplissage colonne Metadonnées
				RunQuery(stmt5, QUERIES[43]); //Creation Réelle table Données_Chiffrees
				RunQuery(stmt5, QUERIES[44]); //Supression table provisoire
				RunQuery(stmt5, QUERIES[26]); //Chiffrement données (en faux pour l'instant)
				RunQuery(stmt5, QUERIES[45]); //Chiffrement Metadonnées (en faux pour l'instant)
				RunQuery(stmt5, QUERIES[46] + frontale +"'"); //Table Cles_Types
				RunQuery(stmt5, QUERIES[47]); //E_Cred_Ksec
				RunQuery(stmt5, QUERIES[48]); //Remplissage E_Cred_Ksec
				RunQuery(stmt5, QUERIES[49]); //Table Logcom
				RunQuery(stmt5, QUERIES[50]); //Table Routage
				RunQuery(stmt5, QUERIES[51]);
				RunQuery(stmt5, QUERIES[52]);
			}
			rs2.close();
			
			// On crée maintenant le Serveur Central
			Statement stmt6 = DBManager.OpenCreateDB(PREFIX + "Server.sqlite");
			RunQuery(stmt6, QUERIES[5]); //attache initDB
			RunQuery(stmt6, QUERIES[35]); //Copie de la tables des Frontales
			RunQuery(stmt6, QUERIES[49]); //Table LogCOM

			// On crée maintenant la BD Noeud TOR
			Statement stmt7 = DBManager.OpenCreateDB(PREFIX + "Noeuds_TOR.sqlite");
			RunQuery(stmt7, QUERIES[49]); //Table Logcom
			RunQuery(stmt7, QUERIES[50]); //Table Routage
			RunQuery(stmt7, QUERIES[19]); //Table NoeudsTOR
	}
	catch (Exception e) 
	{
		e.printStackTrace();
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
	}

}

public final static String PREFIX = "../../BDs/";
public final static String INIT_FILE = PREFIX + "InitDB.sqlite";
public static final String[] QUERIES = {
		"DROP TABLE Cles",
		"CREATE TABLE Cles (ID_Cle INTEGER PRIMARY KEY  NOT NULL, Kpub TEXT(500),Kpriv TEXT(500))",
		"insert into Cles Select ID_Cle, '', '' from Statuts UNION Select ID_Cle, '', ''  from Groupes UNION Select ID_Cle, '', ''  from Affectations",
		"ALTER TABLE Types_Utili ADD COLUMN Ksec TEXT(500)",
		"SELECT Login from Utilisateurs",
		/*5*/
		"ATTACH '"+ INIT_FILE + "' as 'BASE'",
		"create table Utilisateurs as select * from BASE.Utilisateurs",
		"create table Frontales as select * from BASE.Frontales",
		"DETACH 'BASE';",
		"create table cles as select * from BASE.cles",
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
		"create table Utilisateurs as select U.IP, U.Port, S.Generalise, A.Generalisee from BASE.Utilisateurs U, BASE.Statuts S, BASE.Affectations A where U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and Frontale = '",
		"create table Provisoire as select D.Type, S.Generalise, A.Generalisee, D.Valeur, D.Login, A.Affectation, S.Statut, U.Liste_groupes from BASE.Donnees D, BASE.Statuts S, BASE.Affectations A, BASE.Utilisateurs U, BASE.Types_Utili T where U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and U.Login = D.Login and D.Login = T.Login and D.Type = T.Type and T.Dispo = 'Toujours' and Frontale = '",
		"SELECT Ksec from Types_utili",
		/*35*/
		"create table Frontales as select * from BASE.Frontales",
		"ALTER TABLE Utilisateurs ADD COLUMN Liste_groupes TEXT(500)",
		"SELECT G.Groupe FROM Groupes G, Utilisateurs U, Inscription I where U.Login = I.Login and I.ID_Groupe = G.ID_Groupe and U.Login = '",
		"",
		"UPDATE Utilisateurs set Liste_groupes = '",
		/*40*/
		"' WHERE Login = '",
		"ALTER TABLE Provisoire ADD COLUMN Metadonnees TEXT(500)",
		"UPDATE Provisoire SET Metadonnees = Login || '//' || Affectation || '//' || Statut || Liste_groupes",
		"create table Donnees_Chiffrees as select Type, Generalise, Generalisee, Valeur, Metadonnees from Provisoire",
		"DROP table Provisoire",
		/*45*/
		"Update Donnees_Chiffrees set Metadonnees = Metadonnees || ' + CHIFFREMENT' ",
		"create table Cles_Types as select T.Type, Cred_Auto_Ref from BASE.Types_Utili T, BASE.Cred_Autorise C, BASE.Utilisateurs U where C.Politique = T.Politique and T.Login = U.Login and Frontale = '",
		"ALTER TABLE Cles_Types ADD COLUMN E_Cred_Ksec TEXT",
		"Update Cles_Types set E_Cred_Ksec = Cred_Auto_ref || ' + CHIFFREMENT' ",
		"CREATE  TABLE LogCOM (Date DATETIME, IP_Emmeteur TEXT, Port_Emetteur TEXT, IP_Recepteur TEXT, Port_Recepteur TEXT, MSG TEXT)",
		/*50*/
		"CREATE  TABLE Routage (ID_Req TEXT PRIMARY KEY  NOT NULL , IP_Emmeteur TEXT, Port_Emetteur TEXT)",
		"ALTER TABLE Donnees_Chiffrees ADD COLUMN RefBD",
		"Update Donnees_Chiffrees set RefBD = random()",
""};

}
