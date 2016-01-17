package initialisation_BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import javax.crypto.*;
import java.util.Scanner;

import crypto.AES;
import crypto.MyRSA;

import java.util.StringTokenizer;

public class DatabaseMain {

	public static void main(String[] args) throws BadPaddingException {
		while (true) {
			try {
				init();
				break;
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static void init() throws Exception {
	
			System.out.println("Mise en place des repertoires");
			Runtime.getRuntime().exec("rm -r " + PREFIX);
			Thread.sleep(1000);    
			Runtime.getRuntime().exec("mkdir " + PREFIX);
			Thread.sleep(1000);    
			// Runtime.getRuntime().exec(new String[] {"mkdir ",PREFIX});
			Runtime.getRuntime().exec("cp " + PREFIX + "/../" + INIT_NAME + " " + PREFIX + "/" + BD_COMPLETE);
			Thread.sleep(1000);    
			Scanner scanner = new Scanner(System.in);

			// 1 - ON COMPLETE LA BD INITIALE AVEC TOUTES LES INFORMATIONS
			// DEDUITES/CALCULEES
			// --------------------------------------------------------------------------------
			Connection con1 = DBManager.OpenCreateDB(INIT_FILE);
			Statement stmt1 = DBManager.CreateStatement(con1);

			// Création et remplissage de la table Cles
			System.out.println("\nCréation de la table Clés :\n");

			RunQuery(stmt1, QUERIES[0]); // Cree la table Cles avec ID_Cle, Kpub
											// et Kpriv
			RunQuery(stmt1, QUERIES[1]); // Rempli la table Cles (ID de
											// Groupes/Statuts/Affectations)

			//// scanner.nextLine();

			// Les deux ID aleatoires ci-dessous sont utiles pour associer les
			// donnees chiffrees aux Cles_Types sur la BD frontale
			System.out.println("Ajout d'une Ref BD et d'une ID_Cred :\n");

			RunQuery(stmt1, QUERIES[2]); // On rajoute une colonne refBD a
											// Donnees
			RunQuery(stmt1, QUERIES[3]); // On rempli refBD avec une valeur
											// aleatoire dans la table Donnees
			// RunQuery(stmt1, QUERIES[4]); //On rajoute une colonne ID_Cred a
			// Cred_Autorise
			// RunQuery(stmt1, QUERIES[5]); //On rempli ID_Cred avec une valeur
			// aleatoire dans Cred_Autorise

			// scanner.nextLine();

			// Rajout des colonnes (en rouge dans le PPT)
			RunQuery(stmt1, QUERIES[6]); // Rajoute le champ Ksec à la table
											// Types_Utili
			RunQuery(stmt1, QUERIES[7]); // On cree la colonne Liste_Groupe dans
											// la table Utilisateurs
			RunQuery(stmt1, QUERIES[8]); // On cree la colonne Metadonnees dans
											// la table Utilisateurs
			RunQuery(stmt1, QUERIES[9]); // On cree la colonne Meta_Chiffrees
											// dans la table Types_Utili
			RunQuery(stmt1, QUERIES[10]); // On cree la colonne Valeur_Chiffree
											// dans la table Donnees

			// 2 - GENERATION DES CLES PUBLIQUES/PRIVEES ET STOCKAGE DANS LA
			// TABLE Cles
			// --------------------------------------------------------------------------------

			System.out.println("\nGénération des clés asymétriques RSA...\n");

			ResultSet rss = stmt1.executeQuery(QUERIES[11]);
			String ID_Cle2 = null;
			while (rss.next() == true) {
				ID_Cle2 = rss.getString("ID_Cle");
				byte[][] keys = TestCipher.generateRSA_KEYS();
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[12]);
				pstmt.setBytes(1, keys[0]); // kpub
				pstmt.setBytes(2, keys[1]); // kpriv
				pstmt.setString(3, ID_Cle2);
				pstmt.execute();
			}
			rss.close();

			// 3 - GENERATION DES CLES SECRETES DE TYPE
			// --------------------------------------------------------------------------------

			System.out.println("\nGénération des clés symétriques AES...\n");

			rss = stmt1.executeQuery(QUERIES[13]); // On recupere les rowid
			String ID = null;
			while (rss.next() == true) {
				ID = rss.getString("rowid");
				byte[] encodedSecreteKey = TestCipher.generateAES_KEY();
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[14]);
				pstmt.setBytes(1, encodedSecreteKey);
				pstmt.setString(2, ID);
				pstmt.execute();
			}
			rss.close();

			// scanner.nextLine();

			// 4 - CHIFFREMENT DES DONNEES AVEC KSEC
			// --------------------------------------------------------------------------------

			System.out.println("Chiffrement des données avec Ksec...\n");

			rss = stmt1.executeQuery(QUERIES[15]);
			while (rss.next() == true) {
				byte[] aesKey = rss.getBytes("Ksec");
				int ID_Donnee = rss.getInt("ID_Donnee");
				// System.out.println("On chiffre donnée n° " + ID_Donnee);

				byte[] encodedEncryptedValue = TestCipher.cryptWithAes(rss.getString("Valeur").getBytes(),
						TestCipher.decodeAES_KEY(aesKey));
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[16]);
				pstmt.setBytes(1, encodedEncryptedValue);
				pstmt.setInt(2, ID_Donnee);
				pstmt.execute();
			}
			rss.close();

			// scanner.nextLine();

			// 5 - CALCUL DES METADONNEES ET STOCKAGE DANS LA TABLE Utilisateurs
			// --------------------------------------------------------------------------------

			System.out.println("Calcul des métadonnées :\n");
			// scanner.nextLine();

			// Calcul de la liste des groupes puis des metadonnees de chaque
			// utilisateur
			Statement stmt9 = DBManager.CreateStatement(con1);
			rss = stmt9.executeQuery(QUERIES[17]); // On recupere les Logins,
													// statut, affectation
			String login2 = null;
			while (rss.next() == true) {
				login2 = rss.getString("Login");
				String statut = rss.getString("Statut");
				String affectation = rss.getString("Affectation");
				ResultSet rss1 = stmt1.executeQuery(QUERIES[18] + login2 + "'"); // On
																					// recupere
																					// les
																					// groupes
				String Meta = "";
				String Liste_Groupe = "";
				while (rss1.next() == true) {
					Liste_Groupe += rss1.getString("Groupe") + ",";
				}
				Meta = login2 + "," + Liste_Groupe + statut + "," + affectation;
				rss1.close();
				RunQuery(stmt1, QUERIES[19] + Meta + QUERIES[20] + login2 + "'");
				RunQuery(stmt1, QUERIES[35] + Liste_Groupe + QUERIES[20] + login2 + "'");
			}
			rss.close();

			// 6 - CHIFFREMENT DES METADONNEES AVEC LES KSec DANS LA TABLE
			// Type_Utili
			// --------------------------------------------------------------------------------

			System.out.println("\nChiffrement des métadonnées...\n");

			rss = stmt1.executeQuery(QUERIES[21]);
			while (rss.next() == true) {
				byte[] aesKey = rss.getBytes("Ksec");
				String Metadonnees = rss.getString("Metadonnees");
				System.out.println("On chiffre métadonnée KSEC " + new String(aesKey));
				byte[] encodedEncryptedValue = TestCipher.cryptWithAes(Metadonnees.getBytes(),
						TestCipher.decodeAES_KEY(aesKey));
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[22]);
				pstmt.setBytes(1, encodedEncryptedValue);
				pstmt.setBytes(2, aesKey);
				pstmt.execute();
			}
			rss.close();

			// scanner.nextLine();

			// 7 - CALCUL DE E_Cred_KSec: CHIFFREMENT DE KSec avec les Kpub
			// (CREDENTIALS) DANS LA TABLE Types_Utili
			// -------------------------------------------------------------------------------------------------------

			System.out.println("Chiffrement des Ksec avec les Kpub RSA (crédentials)...\n");

			RunQuery(stmt1, QUERIES[29]);// Cree une version de Cles_Types avec
											// KSec
			rss = stmt1.executeQuery(QUERIES[23]);// recupere KSec et
													// Cred_Auto_Ref
			while (rss.next() == true) {
				byte[] Ksec = rss.getBytes("Ksec");
				String CAR = rss.getString("Cred_Auto_Ref");
				byte[] cleSecChiffree = TestCipher.decodeAES_KEY(Ksec).getEncoded(); // lol
																						// jer
																						// crois
																						// que
																						// �a
																						// sert
																						// a
																						// rien
																						// la
				// System.out.print("Ksec " + KsecString + " Cred_Auto_Ref " +
				// CAR);
				StringTokenizer creds = new StringTokenizer(CAR, ",");
				boolean firstCrypt = true;
				while (creds.hasMoreTokens()) {
					String cred = creds.nextToken();
					ResultSet rss5 = stmt9.executeQuery(QUERIES[24] + cred + "'");// recupere
																					// les
																					// Kpub
																					// de
																					// la
																					// table
																					// Cle
																					// ou
																					// ID_Cle
																					// =
																					// Cred_Auto_Ref
					byte[] ss = rss5.getBytes("Kpub");
					byte[] cleSecChiffree2;
					if (firstCrypt) {
						cleSecChiffree2 = TestCipher.cryptWithRSApadding(cleSecChiffree,
								TestCipher.decodeRSA_KEYS(ss, null).getPublic());
						firstCrypt = false;
					} else
						cleSecChiffree2 = TestCipher.cryptWithRSA(cleSecChiffree,
								TestCipher.decodeRSA_KEYS(ss, null).getPublic());
					System.out.println(new String(cleSecChiffree2) + " " + cleSecChiffree2.length);
					cleSecChiffree = cleSecChiffree2;
					rss5.close();
				}
				System.out.println(" Encrypted " + new String(cleSecChiffree));
				PreparedStatement pstmt = con1.prepareStatement(QUERIES[25]);
				pstmt.setBytes(1, cleSecChiffree);
				pstmt.setBytes(2, Ksec);
				pstmt.setString(3, CAR);
				pstmt.execute();
			}
			RunQuery(stmt1, QUERIES[4]); // On rajoute une colonne ID_Cred a
											// Cles_Types
			RunQuery(stmt1, QUERIES[5]); // On rempli ID_Cred avec une valeur
											// aleatoire dans Cles_Types

			// scanner.nextLine();

			// 9 - CREATION D'UNE BD TEMPORAIRE
			// -------------------------------------------------------------------------------------------------------

			// On copie les tables contenant les users et les frontales dans une
			// BD TEMP
			// Pour pouvoir la parcourir (et en même temps attacher la BD
			// originale) car sinon
			// on a des problemes de verrouillage de la BD pendant le parcours
			Connection con4 = DBManager.OpenCreateDB(PREFIX + "/TEMP.sqlite");
			Statement stmt4 = DBManager.CreateStatement(con4);
			RunQuery(stmt4, QUERIES[26]); // attache initDB
			RunQuery(stmt4, QUERIES[27]); // Copie de la table utilisateurs
			RunQuery(stmt4, QUERIES[28]); // Copie de la table frontales
			RunQuery(stmt4, QUERIES[30]); // Copie de la table des
											// Types_Utilisateurs
			RunQuery(stmt4, QUERIES[31]); // Copie de la table des cles
			RunQuery(stmt4, QUERIES[32]); // Detache initDB

			// 9 - CREATION DES BD DES UTILISATEURS
			// -------------------------------------------------------------------------------------------------------

			System.out.println("\nCréation des BDs Utilisateurs :\n");
			// scanner.nextLine();

			ResultSet rs = stmt4.executeQuery(QUERIES[33]); // Recupere les
															// logins
			String login = null;
			while (rs.next() == true) {
				login = rs.getString("Login");

				// On cree la BD "Protegee". Normalement cette BD devrait être
				// deportee dans un token securise
				// Car elle contient les donnees en clair et les cles secretes
				// et privees....
				Connection con2 = DBManager.OpenCreateDB(PREFIX + "/" + login + "_SEC.sqlite");
				Statement stmt2 = DBManager.CreateStatement(con2);
				RunQuery(stmt2, QUERIES[26]); // attache initDB
				RunQuery(stmt2, QUERIES[31]); // Copie de la tables des cles
				// Suppression des cles privees qui ne sont pas associees à
				// l'utilisateur en gardant celles du chemin hierarchique
				RunQuery(stmt2, QUERIES[34] + login + "' " + QUERIES[36] + login + "' " + QUERIES[38] + login + "') ");
				RunQuery(stmt2, QUERIES[40]); // Copie de la tables des groupes
				RunQuery(stmt2, QUERIES[41]); // Copie de la tables des statuts
				RunQuery(stmt2, QUERIES[42]); // Copie de la tables des
												// affectations
				RunQuery(stmt2, QUERIES[43] + login + "'"); // Copie des donnees
															// de l'utilisateur
				RunQuery(stmt2, QUERIES[44] + login + "'"); // Copie des
															// politiques de
															// l'utilisateur
				RunQuery(stmt2, QUERIES[45] + login + "'"); // Copie des types
															// et cles,
															// politiques de
															// l'utilisateur
				RunQuery(stmt2, QUERIES[46] + login + "'"); // Copie des
															// credentials
															// autorises de
															// l'utilisateur
				RunQuery(stmt2, QUERIES[47] + login + "'"); // Copie du
															// login/passwd/ip/port
															// de l'utilisateur
				RunQuery(stmt2, QUERIES[48] + login + "'"); // Copie de la
															// frontale de
															// l'utilisateur
				RunQuery(stmt2, QUERIES[32]); // Detache initDB

				// Creation de la BD non securisee pour l'utilisateur
				Connection con3 = DBManager.OpenCreateDB(PREFIX + "/" + login + ".sqlite");
				Statement stmt3 = DBManager.CreateStatement(con3);
				RunQuery(stmt3, QUERIES[26]); // attache initDB
				RunQuery(stmt3, QUERIES[49] + login + "'"); // Types
				RunQuery(stmt3, QUERIES[50] + login + "'"); // Donnees_Chiffree
				RunQuery(stmt3, QUERIES[51] + login + "'"); // Cles_Types
				RunQuery(stmt3, QUERIES[37] + login + "'"); // Utilisateurs
				RunQuery(stmt3, QUERIES[32]); // Detache initDB

			}

			// 10 - CREATION DES FRONTALES
			// -------------------------------------------------------------------------------------------------------

			System.out.println("\nCréation des BDs Frontales :\n");
			// scanner.nextLine();

			ResultSet rs2 = stmt4.executeQuery(QUERIES[39]); // Recupere les
																// Frontales
			String frontale = null;
			int i = 0;
			while (rs2.next() == true) {
				frontale = rs2.getString("Frontale");
				// C'est parti !
				Connection con5 = DBManager.OpenCreateDB(PREFIX + "/" + frontale + ".sqlite");
				Statement stmt5 = DBManager.CreateStatement(con5);
				RunQuery(stmt5, QUERIES[26]); // attache initDB
				if (i == 0) {
					System.out.println("\nC" + "opie des tables Frontales et Server :");
					// scanner.nextLine();
				}
				RunQuery(stmt5, QUERIES[52]); // server
				RunQuery(stmt5, QUERIES[53] + frontale + "'"); // Frontal
				RunQuery(stmt5, QUERIES[54] + frontale + "'"); // Frontales
				if (i == 0) {
					System.out.println("\nCréation de la table Utilisateurs (par jointure) :");
					// scanner.nextLine();
				}
				RunQuery(stmt5, QUERIES[55] + frontale + "'"); // table
																// utilisateurs
																// (Statut,
																// Affectation
																// generalises ?
																// Ou
																// desanonymisation
																// ?)
				if (i == 0) {
					System.out.println("\nCréation des Donnees_Chiffrees et Cles_Types (par jointure) :");
					// scanner.nextLine();
				}
				RunQuery(stmt5, QUERIES[56] + frontale + "'"); // Creation
																// Reelle table
																// Donnees_Chiffrees
				RunQuery(stmt5, QUERIES[57] + frontale + "'"); // Table
																// Cles_Types
				if (i == 0) {
					System.out.println("\nCréation de la table Liens :");
					// scanner.nextLine();
				}
				RunQuery(stmt5, QUERIES[58] + frontale + "'"); // Creation table
																// Liens
				RunQuery(stmt5, QUERIES[59]); // Table Routage
				RunQuery(stmt5, QUERIES[32]); // Detache initDB

				i = i + 1;
			}
			rs2.close();

			// 11 - CREATION DU SERVEUR CENTRAL
			// -------------------------------------------------------------------------------------------------------
			Connection con6 = DBManager.OpenCreateDB(PREFIX + "/Server.sqlite");
			Statement stmt6 = DBManager.CreateStatement(con6);
			RunQuery(stmt6, QUERIES[26]); // attache initDB
			RunQuery(stmt6, QUERIES[60]); // Copie de la tables des Frontales
			RunQuery(stmt6, QUERIES[61]); // Table Server
			RunQuery(stmt6, QUERIES[32]); // Detache initDB

			// 13 - CREATION DE LA BASE POUR LE LANCEMENT
			// -------------------------------------------------------------------------------------------------------
			Connection con7 = DBManager.OpenCreateDB(PREFIX + "/Lancement.sqlite");
			Statement stmt7 = DBManager.CreateStatement(con7);
			RunQuery(stmt7, QUERIES[26]); // attache initDB
			RunQuery(stmt7, QUERIES[62]); // Copie de la liste des utilisateurs
			RunQuery(stmt7, QUERIES[63]); // Copie de la liste des frontales
			RunQuery(stmt7, QUERIES[32]); // Detache initDB

			// 14 - SUPPRESSION DES BASES INIT_DB COMPLETE ET DE LA BASE TEMP

			// -------------------------------------------------------------------------------------------------------
			// Runtime.getRuntime().exec("rm " + INIT_FILE);
			Runtime.getRuntime().exec("rm " + PREFIX + "/TEMP.sqlite");
			scanner.close();

	
	}

	public static void RunQuery(Statement stmt, String sql) {

		System.out.println("Execution de : " + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public final static String PREFIX = "Initialisation/datas";

	public final static String INIT_NAME = "InitDB.sqlite";
	public final static String BD_COMPLETE = "CompleteDB.sqlite";

	public final static String INIT_FILE = PREFIX + "/" + BD_COMPLETE;
	public static final String[] QUERIES = { "CREATE TABLE Cles (ID_Cle TEXT(500) PRIMARY KEY, Kpub TEXT,Kpriv TEXT)", // 0
			"insert into Cles Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle from Statuts UNION Select ID_Cle,'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Groupes "
					+ "UNION Select ID_Cle, 'Kpub' ||ID_Cle, 'Kpriv' ||ID_Cle  from Affectations", // 1
			"ALTER TABLE Donnees ADD COLUMN RefBD INTEGER", // 2
			"UPDATE Donnees set RefBD = random()", // 3
			"ALTER TABLE Cles_Types ADD COLUMN ID_Cred INTEGER", // 4
			"UPDATE Cles_Types set ID_Cred = random()", // 5
			"ALTER TABLE Types_Utili ADD COLUMN Ksec TEXT", // 6
			"ALTER TABLE Utilisateurs ADD COLUMN Liste_Groupe TEXT", // 7
			"ALTER TABLE Utilisateurs ADD COLUMN Metadonnees TEXT", // 8
			"ALTER TABLE Types_Utili ADD COLUMN Meta_Chiffrees TEXT", // 9
			"ALTER TABLE Donnees ADD COLUMN Valeur_Chiffree TEXT", // 10

			"SELECT ID_Cle from Cles", // 11
			"UPDATE Cles set Kpub = ?, Kpriv = ? WHERE ID_CLe = ?", // 12
			"SELECT rowid from Types_Utili", // 13
			"UPDATE Types_Utili set Ksec = ? WHERE rowid = ?", // 14
			"SELECT ID_Donnee, Valeur, Ksec FROM Donnees D, Types_Utili T WHERE D.Login = T.Login AND D.Type = T.Type", // 15
			"UPDATE Donnees SET Valeur_Chiffree = ? WHERE ID_Donnee = ?", // 16
			"SELECT Login, Statut, Affectation  FROM Utilisateurs U, Statuts S, Affectations A WHERE U.ID_Statut=S.ID_Statut and U.ID_Affectation=A.ID_Affectation", // 17
			"SELECT G.Groupe FROM Groupes G, Utilisateurs U, Inscription I where U.Login = I.Login and I.ID_Groupe = G.ID_Groupe and U.Login = '", // 18
			"UPDATE Utilisateurs SET Metadonnees = '", // 19
			"' WHERE Login = '", // 20

			"SELECT Metadonnees, Ksec FROM Utilisateurs U, Types_Utili T WHERE U.Login = T.Login", // 21
			"UPDATE Types_Utili SET Meta_Chiffrees = ? WHERE Ksec = ?", // 22
			"SELECT Ksec, Cred_Auto_Ref from Cles_Types", // 23
			"SELECT Kpub from Cles where Cles.ID_Cle = '", // 24
			"UPDATE Cles_Types SET E_Cred_Ksec = ? WHERE Ksec = ? and Cred_Auto_Ref = ?", // 25
			"ATTACH '" + INIT_FILE + "' as 'BASE'", // 26
			"create table Utilisateurs as select * from BASE.Utilisateurs", // 27
			"create table Frontales as select * from BASE.Frontales", // 28
			"CREATE TABLE Cles_Types AS select Login, Type, Ksec, Cred_Auto_Ref, '' as E_Cred_Ksec FROM Types_Utili T, Cred_Autorise C where T.politique=C.politique", // 29
			"create table Types_Utili as select * from BASE.Types_Utili", // 30

			"create table Cles as select * from BASE.cles", // 31
			"DETACH 'BASE';", // 32
			"SELECT Login, Icone FROM Utilisateurs", // 33
			"Update Cles SET Kpriv = NULL WHERE ID_Cle not in (SELECT ID_Cle from BASE.Statuts S, BASE.Utilisateurs U where U.ID_Statut = S.ID_Statut and Login = '", // 34
			"UPDATE Utilisateurs SET Liste_Groupe = '", // 35
			"UNION SELECT ID_Cle from BASE.Affectations A, BASE.Utilisateurs U where U.ID_Affectation = A.ID_Affectation and Login = '", // 36
			"CREATE TABLE Utilisateurs AS SELECT U.Ip, U.Port, S.Statut_Gen, A.Affect_Gen FROM BASE.Utilisateurs U, BASE.Affectations A, BASE.Statuts S "
					+ "WHERE U.ID_Statut = S.ID_Statut AND U.ID_Affectation = A.ID_Affectation AND Login = '", // 37
			"UNION SELECT ID_Cle from BASE.Groupes G, BASE.Inscription I where I.ID_Groupe = G.ID_Groupe and Login = '", // 38
			"SELECT Frontale, Icone from Frontales", // 39
			"create table Groupes as select * from BASE.Groupes", // 40

			"create table Statuts as select * from BASE.Statuts", // 41
			"create table Affectations as select * from BASE.Affectations", // 42
			"create table Donnees_Clair as select Type, Valeur from BASE.Donnees WHERE Login = '", // 43
			"create table Politiques as Select P.Politique, Expression from BASE.Types_Utili T , BASE.Politiques P where P.Politique = T.Politique and Login = '", // 44
			"create table Types as select Type, Ksec, Dispo, Politique from BASE.Types_Utili WHERE Login = '", // 45
			"create table Cred_Autorise as Select C.Politique, Cred_Auto_Ref from BASE.Types_Utili T , BASE.Cred_Autorise C where C.Politique = T.Politique and Login = '", // 46
			"create table Utilisateurs as Select Login, Password, Ip, Port, ID_Statut, ID_Affectation, Liste_Groupe from BASE.Utilisateurs where Login = '", // 47
			"create table Frontales as Select F.IP, F.internalPort from BASE.Utilisateurs U , BASE.Frontales F where U.Frontale = F.Frontale and Login = '", // 48
			"create table Types as select Type, Meta_Chiffrees from BASE.Types_Utili WHERE Login = '", // 49
			"create table Donnees_Chiffrees as select Type, Valeur_Chiffree from BASE.Donnees WHERE Login = '", // 50

			"CREATE TABLE Cles_Types AS select Type, Cred_Auto_Ref,E_Cred_Ksec FROM BASE.Cles_Types WHERE Login = '", // 51
			"create table Server as select * from BASE.Server", // 52
			"CREATE TABLE Frontale as SELECT * from BASE.Frontales WHERE Frontale = '", // 53
			"CREATE TABLE Frontales as SELECT * from BASE.Frontales WHERE Frontale != '", // 54
			"create table Utilisateurs as select U.IP, U.Port, S.Statut_Gen, A.Affect_Gen from BASE.Utilisateurs U, BASE.Statuts S, BASE.Affectations A "
					+ "where U.ID_Statut = S.ID_Statut and U.ID_Affectation = A.ID_Affectation and Frontale = '", // 55
			"CREATE TABLE Donnees_Chiffrees AS SELECT RefBD, D.Type, Statut_Gen, Affect_Gen, Valeur_Chiffree, Meta_Chiffrees FROM BASE.Utilisateurs U,"
					+ " BASE.Donnees D, BASE.Statuts S, BASE.Affectations A, BASE.Types_Utili T WHERE T.Login = U.Login AND U.ID_Statut = S.ID_Statut "
					+ "AND U.ID_Affectation = A.ID_Affectation AND U.Login= D.Login AND D.Type = T.Type and  Frontale = '", // 56
			"CREATE TABLE Cles_Types AS select distinct ID_Cred, C.Cred_Auto_Ref AS Cred_Auto_Ref, E_Cred_Ksec FROM BASE.Types_Utili T, BASE.Cred_Autorise C, BASE.Utilisateurs U, BASE.Cles_Types CT "

					+ "where U.Login = T.Login AND T.politique=C.politique AND T.Ksec = CT.Ksec and C.Cred_Auto_Ref = CT.Cred_Auto_Ref AND Frontale = '", // 57
			"CREATE TABLE Liens AS SELECT RefBD, ID_Cred FROM BASE.Donnees D, BASE.Utilisateurs U, BASE.Cles_Types C"
					+ " WHERE D.Login = U.Login AND C.Login = D.Login AND C.Type = D.Type AND Frontale = '", // 58
			"CREATE TABLE Routage (ID_Req TEXT PRIMARY KEY  NOT NULL , IP_Emmeteur TEXT, Port_Emetteur TEXT, MSG TEXT(2000))", // 59
			"create table Frontales as select Frontale,Famille,IP,externalPort from BASE.Frontales", // 60

			"CREATE TABLE Server as SELECT * from BASE.Server ", // 61
			"CREATE Table Utilisateurs as SELECT Login, Icone from BASE.Utilisateurs", // 62
			"CREATE Table Frontales as SELECT Frontale, Icone from BASE.Frontales", // 63
			"", // 64
			"", // 65
			"", // 66
			"", // 67
			"", // 68
			"", // 69
			"" };
}
