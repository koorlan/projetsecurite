package dialog;

public class DialogDataReponse {
	
	private String nom; 
	private String type;
	private String affectation; 
	private String statut; 
	private String groupe;
	private String data;
	
	/**
	 * Enregistre une nouvelle réponse à afficher dans le tableau. 
	 * @author clementdelbar
	 * @param nom
	 * @param type
	 * @param affectation
	 * @param statut
	 * @param groupe
	 * @param data
	 */
	public DialogDataReponse(String nom, String type, String affectation, String statut, String groupe, String data){
		super(); 
		
		this.nom = nom; 
		this.type = type; 
		this.affectation = affectation; 
		this.statut = statut; 
		this.groupe = groupe;
		this.data = data;
	}
	
	/**
	 * Retourne le nom associé à la réponse. 
	 * @author clementdelbar
	 * @return nom
	 */
	
	public String getNom() {
		return nom;
	}
	
	/**
	 * Retourne le type associé à la réponse
	 * @author clementdelbar
	 * @return type
	 */
	
	public String getType() {
		return type;
	}
	
	/**
	 * Retourne l'affectation associé à la réponse
	 * @return affectation
	 */
	
	public String getAffectation() {
		return affectation;
	}
	
	/**
	 * Retourne le statut associé à la réponse.
	 * @return
	 */
	
	public String getStatut() {
		return statut;
	}
	
	/**
	 * Retourne le groupe associé à la réponse.
	 * @return
	 */
	
	public String getGroupe() {
		return groupe;
	}
	
	/**
	 * Retourne la donnée associée à la réponse
	 * @return
	 */

	public String getData() {
		return data;
	}
	
}
