package dialog;

public class DialogDataReponse {
	
	private String nom; 
	private String type;
	private String affectation; 
	private String statut; 
	private String groupe;
	
	public DialogDataReponse(String nom, String type, String affectation, String statut, String groupe){
		super(); 
		
		this.nom = nom; 
		this.type = type; 
		this.affectation = affectation; 
		this.statut = statut; 
		this.groupe = groupe;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getType() {
		return type;
	}
	
	public String getAffectation() {
		return affectation;
	}
	
	public String getStatut() {
		return statut;
	}
	
	public String getGroupe() {
		return groupe;
	}
}
