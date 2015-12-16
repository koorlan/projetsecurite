DROP TABLE "Cles" ;
DROP TABLE "Affectations" ;
DROP TABLE "Statuts" ;
DROP TABLE "Groupes" ;
DROP TABLE "Frontales" ;
DROP TABLE "Utilisateurs" ;
DROP TABLE "Inscription" ;
DROP TABLE "Types" ;
DROP TABLE "Donnees" ;
DROP TABLE "Politiques" ;
DROP TABLE "Cred_autorise" ;
DROP TABLE "Type_Utili" ;
DROP TABLE "Server" ;
DROP TABLE "NoeudTOR" ;
CREATE TABLE "Cles" ("ID_Cle" INTEGER PRIMARY KEY  NOT NULL);
CREATE TABLE "Affectations" ("ID_Affectation" INTEGER PRIMARY KEY  NOT NULL , "Affectation" TEXT, "Generalisee" TEXT, ID_Cle INTEGER REFERENCES Cles);
CREATE TABLE "Statuts" ("ID_Statut" INTEGER, "Statut" TEXT, "Generalise" TEXT, ID_Cle INTEGER REFERENCES Cles);
CREATE TABLE "Groupes" ("ID_Groupe" INTEGER, "Groupe" TEXT, ID_Cle INTEGER REFERENCES Cles);
CREATE TABLE "Frontales" ("Frontale" TEXT PRIMARY KEY  NOT NULL , "IP" TEXT, "Port" INTEGER)
CREATE TABLE "Utilisateurs" ("Login" TEXT PRIMARY KEY  NOT NULL , "Password" BLOB, "IP" TEXT, "Port" INTEGER, ID_Statut INTEGER REFERENCES Statuts, ID_Affectation INTEGER REFERENCES Affectations, Frontale TEXT REFERENCES Frontales);
CREATE TABLE "Inscription" ("Login" TEXT REFERENCES Utilisateurs, ID_Groupe REFERENCES Groupe);
CREATE TABLE "Types" ("Type" TEXT PRIMARY KEY  NOT NULL);
CREATE TABLE "Donnees" ("ID_Donnee" INTEGER PRIMARY KEY  NOT NULL ,"Login" TEXT REFERENCES Utilisateurs, "Type" TEXT REFERENCES Types,Valeur TEXT NOT NULL);
CREATE TABLE "Politiques" ("Politique" TEXT PRIMARY KEY  NOT NULL ,"Expression" TEXT NOT NULL);
CREATE TABLE "Cred_autorise" ("Politique" TEXT REFERENCES Politiques,"Cred_Auto_Ref" TEXT NOT NULL);
CREATE TABLE "Type_Utili" ("Login" TEXT REFERENCES Utilisateurs, "Type" TEXT REFERENCES Types, "Dispo" TEXT, "Politique" TEXT REFERENCES Politiques);
CREATE TABLE "Server" ("IP" TEXT, "Port" INTEGER);
CREATE TABLE "NoeudTOR" ("IP" TEXT, "Port" INTEGER);

INSERT INTO "Frontales" VALUES('F1','127.0.0.1',1001);
INSERT INTO "Frontales" VALUES('F2','127.0.0.1',1002);
INSERT INTO "Frontales" VALUES('F3','127.0.0.1',1003);
INSERT INTO "Groupes" VALUES(1,'Tennis', 227);
INSERT INTO "Groupes" VALUES(2,'Echecs', 153);
INSERT INTO "Groupes" VALUES(3,'Wow', 767);
INSERT INTO "Groupes" VALUES(4,'Rock', 555);
INSERT INTO "Groupes" VALUES(5,'Peinture', 987);
INSERT INTO "Groupes" VALUES(6,'TD1', 345);
INSERT INTO "Groupes" VALUES(7,'Projet_secu', 876);
INSERT INTO "Statuts" VALUES(1,'Prépa intégré','Etudiant', 375);
INSERT INTO "Statuts" VALUES(2,'Elève ingénieur','Etudiant', 937);
INSERT INTO "Statuts" VALUES(3,'Master','Etudiant', 239);
INSERT INTO "Statuts" VALUES(4,'Doctorant','Etudiant', 932);
INSERT INTO "Statuts" VALUES(5,'Chef de département','Enseignant', 287);
INSERT INTO "Statuts" VALUES(6,'Enseignant chercheur','Enseignant', 201);
INSERT INTO "Statuts" VALUES(7,'Intervenant extérieur','Enseignant', 789);
INSERT INTO "Statuts" VALUES(8,'Service des formations et de la vie étudiante','Administratif', 264);
INSERT INTO "Statuts" VALUES(9,'Service de scolarité','Administratif', 102);
INSERT INTO "Statuts" VALUES(10,'Service relations internationales','Administratif', 908);
INSERT INTO "Statuts" VALUES(11,'Service des relations entreprise','Administratif', 890);
INSERT INTO "Statuts" VALUES(12,'Service informatique','Administratif', 547);
INSERT INTO "Statuts" VALUES(13,'Directeur','Administratif', 938);
INSERT INTO "Types" VALUES('Nom');
INSERT INTO "Types" VALUES('Affectation');
INSERT INTO "Types" VALUES('Statut');
INSERT INTO "Types" VALUES('Groupe');
INSERT INTO "Types" VALUES('AdresseMail');
INSERT INTO "Types" VALUES('Tel');
INSERT INTO "Types" VALUES('Photo');
insert into Cles (Select ID_Cle from Statuts UNION Select ID_Cle from Groupes UNION Select ID_Cle from Affectations);










