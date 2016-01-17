package generalizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GeneralizerModel {
	
	private static Tree groupTree;
	private static Tree statusTree;
	private static Tree assignementTree;
	
	public GeneralizerModel()
	{
		setGroupTree();
		setStatusTree();
		setAssignementTree();
	}
	
	public void setGroupTree()
	{
		/* 
		 * Generate Groups tree
		 */
		List<Node> listProjetSecu = new ArrayList<Node>();
		listProjetSecu.add(new Leaf("D1311", "TD1"));
		
		List<Node> listSports = new ArrayList<Node>();
		listSports.add(new Leaf("D1111", "Tennis"));
		
		List<Node> listJeuxVideos = new ArrayList<Node>();
		listJeuxVideos.add(new Leaf("D1121", "Wow"));
		
		List<Node> listJeuxSociete = new ArrayList<Node>();
		listJeuxSociete.add(new Leaf("D1131", "Echecs"));

		List<Node> listTravail = new ArrayList<Node>();
		listTravail.add(new InternalNode("C131", "Projet_Secu", listProjetSecu));
		
		List<Node> listLoisirs = new LinkedList<Node>();
		listLoisirs.add(new InternalNode("C111", "Sports", listSports));
		listLoisirs.add(new InternalNode("C112", "JeuxVideo", listJeuxVideos));
		listLoisirs.add(new InternalNode("C113", "JeuxSociete", listJeuxSociete));
		

		List<Node> listMusique = new ArrayList<Node>();
		listMusique.add(new Leaf("D1211", "Rock"));
		
		List<Node> listArt = new ArrayList<Node>();
		listArt.add(new Leaf("D1221", "Peinture"));
		
		List<Node> listCulture = new LinkedList<Node>();
		listCulture.add(new InternalNode("C121", "Musique", listMusique));
		listCulture.add(new InternalNode("C122", "Arts", listArt));
		
		List<Node> listGlobalAffectation = new LinkedList<Node>();
		listGlobalAffectation.add(new InternalNode("B11", "Loisirs", listLoisirs));
		listGlobalAffectation.add(new InternalNode("B12", "Culture", listCulture));
		listGlobalAffectation.add(new InternalNode("B13", "Travail", listTravail));
		
		Node group = new InternalNode("A1", "Affectation Globale", listGlobalAffectation);
		groupTree = new Tree(group);
	}
	
	public void setStatusTree()
	{
		/* 
		 * Generate Status tree
		 */
		List<Node> listEnseignants = new ArrayList<Node>();
		listEnseignants.add(new Leaf("D1111", "Enseignant chercheur"));
		listEnseignants.add(new Leaf("D1112", "Chef de departement"));
		listEnseignants.add(new Leaf("D1113", "Intervenant exterieur"));
		
		List<Node> listServices = new ArrayList<Node>();
		listServices.add(new Leaf("D1121", "Service des formations et de la vie etudiante"));
		listServices.add(new Leaf("D1122", "Service de scolarite"));
		listServices.add(new Leaf("D1123", "Service relations internationales"));
		listServices.add(new Leaf("D1124", "Service des relations entreprises"));
		listServices.add(new Leaf("D1125", "Service informatique"));
		
		List<Node> listEleves = new ArrayList<Node>();
		listEleves.add(new Leaf("C121", "Prepa integree"));
		listEleves.add(new Leaf("C122", "Eleve ingenieur"));
		listEleves.add(new Leaf("C123", "Master"));
		listEleves.add(new Leaf("C124", "Doctorant"));
		
		List<Node> listPersonnel = new LinkedList<Node>();
		listPersonnel.add(new InternalNode("C111", "Enseignants", listEnseignants));
		listPersonnel.add(new InternalNode("C112", "Service administratifs", listServices));
		listPersonnel.add(new Leaf("C113", "Directeur"));
		
		List<Node> listGlobalStatus = new LinkedList<Node>();
		listGlobalStatus.add(new InternalNode("B11", "Personnel", listPersonnel));
		listGlobalStatus.add(new InternalNode("B12", "Eleves", listEleves));
		Node status = new InternalNode("A1", "Statut Global", listGlobalStatus);
		statusTree = new Tree(status);
	}
	
	public static void setAssignementTree()
	{
		/* 
		 * Generate Affectations tree
		 */
		List<Node> listInsacvl = new ArrayList<Node>();
		listInsacvl.add(new Leaf("C111", "STI"));
		listInsacvl.add(new Leaf("C112", "MRI"));
		
		List<Node> listInsa = new LinkedList<Node>();
		listInsa.add(new InternalNode("B11", "INSA CVL", listInsacvl));
		listInsa.add(new Leaf("B12", "INSA Lyon"));
		listInsa.add(new Leaf("B13", "INSA Rennes"));
		listInsa.add(new Leaf("B14", "INSA Rouen"));
		listInsa.add(new Leaf("B15", "INSA Toulouse"));
		listInsa.add(new Leaf("B16", "INSA Strasbourg"));
		Node assignement = new InternalNode("A1", "Insa", listInsa);
		assignementTree = new Tree(assignement);
	}
	
	public static Tree getGroupTree()
	{
		return GeneralizerModel.groupTree;
	}
	
	public static Tree getStatusTree()
	{
		return GeneralizerModel.statusTree;
	}
	
	public static Tree getAssignementTree()
	{
		return GeneralizerModel.assignementTree;
	}
}
