package dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DialogTabReponse extends AbstractTableModel {
	private final List<DialogDataReponse> reponses = new ArrayList<DialogDataReponse>();
	
	private String entetes[] = {
			"Nom",
			"Type",
			"Affectation",
			"Statut", 
			"Groupe"
		};
	
	public DialogTabReponse(){
		super();
		int i; 
		
	}
	
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
	
	public int getRowCount() {
        return reponses.size();
    }

	public int getColumnCount() {
        return entetes.length;
    }

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
	        default:
	            return null;
	    }
	}
	
	public void addReponse(DialogDataReponse elt) {
		reponses.add(elt);
		fireTableRowsInserted(reponses.size() - 1, reponses.size() - 1);
	}
}
