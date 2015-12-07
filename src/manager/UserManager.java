package manager;

import model.UserModel;

public class UserManager {
	private UserModel model = null;
	private CoreManager core = null;
	
	public UserManager(UserModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public String getNom(){
		return this.model.getNom();
	}
	
	public void setNom(String str){
		this.model.setNom(str);
	}
	
	public void update(String str){
		if(str.equals("nom"))
			this.core.getLogManager().log("(USER) Changed username to \'" + this.model.getNom() + "\'");
	}
}
