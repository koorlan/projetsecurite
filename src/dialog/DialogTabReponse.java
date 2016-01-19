package dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DialogTabReponse extends AbstractTableModel {
	
	// Liste contenant l'ensemble des éléments du tableau d'affichage
	private final List<DialogDataReponse> reponses = new ArrayList<DialogDataReponse>();
	
	// Nous spécifions l'en-tête du tableau d'affichage
	private String entetes[] = {
			"Nom",
			"Type",
			"Affectation",
			"Statut", 
			"Groupe",
			"Data"
		};
	
	/**
	 * Initialisation du tableau de réponse
	 */
	public DialogTabReponse(){
		super();
	}
	
	/**
	 * Retourne le nom associé à une colonne
	 */
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
	
	/**
	 * Retourne le nombre de lignes dans le tableau d'affichage
	 */
	
	public int getRowCount() {
        return reponses.size();
    }

	/**
	 * Retourne le nombre de colonnes présentes dans le tableau
	 */
	public int getColumnCount() {
        return entetes.length;
    }

	/**
	 * Cette fonctionne permet de remplir le tableau
	 * @param rowIndex Numéro de l'entrée dans le tableau
	 * @param columnIndex Numéro de colonne dans le tableau
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
	        case 0:
	            return reponses.get(rowIndex).getNom();
	        case 1:
	            return reponses.get(rowIndex).getType();
	        case 2:
	            return reponses.get(rowIndex).getAffectation();
	        case 3:
	            return reponses.get(rowIndex).getStatut();
	        case 4:
	            return reponses.get(rowIndex).getGroupe();
	        case 5:
	        	 return reponses.get(rowIndex).getData();
	        default:
	            return null;
	    }
	}
	
	/**
	 * Ajoute un nouvel élément de réponse dans le tableau
	 * @param elt Element à ajouter
	 */
	
	public void addReponse(DialogDataReponse elt) {
		reponses.add(elt);
		fireTableRowsInserted(reponses.size() - 1, reponses.size() - 1);
	}
	
	/**
	 * Fonction permettant de rafraichir le tableau
	 */
	
	public void refresh(){
		reponses.clear();
		fireTableRowsInserted(reponses.size() - 1, reponses.size() - 1);
	}
}
