package generalizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import manager.PacketManager;

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
		List<Node> listSports = new ArrayList<Node>();
		listSports.add(new Leaf("D1111", "Tennis"));
		listSports.add(new Leaf("D1112", "Basket"));
		List<Node> listJeuxVideos = new ArrayList<Node>();
		listJeuxVideos.add(new Leaf("D1121", "Wow"));
		List<Node> listJeuxSociete = new ArrayList<Node>();
		listJeuxSociete.add(new Leaf("D1131", "Belote"));
		listJeuxSociete.add(new Leaf("D1132", "Echecs"));
		List<Node> listLoisirs = new LinkedList<Node>();
		listLoisirs.add(new InternalNode("C111", "Sports", listSports));
		listLoisirs.add(new InternalNode("C112", "Jeux Videos", listJeuxVideos));
		listLoisirs.add(new InternalNode("C113", "Jeux de Societe", listJeuxSociete));
		List<Node> listMusique = new ArrayList<Node>();
		listMusique.add(new Leaf("D1211", "Celine Dion"));
		listMusique.add(new Leaf("D1212", "Guitaristes"));
		listMusique.add(new Leaf("D1213", "Pianistes"));
		List<Node> listCulture = new LinkedList<Node>();
		listCulture.add(new InternalNode("C121", "Musique", listMusique));
		List<Node> listGlobalAffectation = new LinkedList<Node>();
		listGlobalAffectation.add(new InternalNode("B11", "Loisirs", listLoisirs));
		listGlobalAffectation.add(new InternalNode("B12", "Culture", listCulture));
		listGlobalAffectation.add(new Leaf("B13", "Travail"));
		Node group = new InternalNode("A1", "Affectation Globale", listGlobalAffectation);
		groupTree = new Tree(group);
	}
	
	public void setStatusTree()
	{
		/* 
		 * Generate Status tree
		 */
		List<Node> listEnseignants = new ArrayList<Node>();
		listEnseignants.add(new Leaf("D1121", "Charges de filiaire"));
		List<Node> listEleves = new ArrayList<Node>();
		listEleves.add(new Leaf("D1131", "Doctorants"));
		List<Node> listInterne = new LinkedList<Node>();
		listInterne.add(new Leaf("C111", "Administration"));
		listInterne.add(new InternalNode("C112", "Enseignants", listEnseignants));
		listInterne.add(new InternalNode("C113", "Eleves", listEleves));
		listInterne.add(new Leaf("C114", "Directeur"));
		List<Node> listIntervenants = new ArrayList<Node>();
		listIntervenants.add(new Leaf("D1211", "Intervenants reguliers"));
		listIntervenants.add(new Leaf("D1212", "Intervenants ponctuels"));
		List<Node> listExterne = new LinkedList<Node>();
		listExterne.add(new InternalNode("C121", "Intervenants", listIntervenants));
		List<Node> listGlobalStatus = new LinkedList<Node>();
		listGlobalStatus.add(new InternalNode("B11", "Interne", listInterne));
		listGlobalStatus.add(new InternalNode("B12", "Externe", listExterne));
		Node status = new InternalNode("A1", "Statut Global", listGlobalStatus);
		statusTree = new Tree(status);
	}
	
	public static void setAssignementTree()
	{
		/* 
		 * Generate Affectations tree
		 */
		List<Node> listBourges = new ArrayList<Node>();
		listBourges.add(new Leaf("D1111", "STI"));
		listBourges.add(new Leaf("D1112", "MRI"));
		List<Node> listInsaCvl = new LinkedList<Node>();
		listInsaCvl.add(new InternalNode("C111", "Bourges", listBourges));
		listInsaCvl.add(new Leaf("C112", "Blois"));
		List<Node> listInsa = new LinkedList<Node>();
		listInsa.add(new InternalNode("B11", "Insa Cvl", listInsaCvl));
		listInsa.add(new Leaf("B12", "Insa Lyon"));
		listInsa.add(new Leaf("B13", "Insa Strasbourg"));
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
