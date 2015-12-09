package manager;

import java.security.PrivateKey;
import java.security.PublicKey;

import model.UserModel;

public class UserManager {
	private UserModel model = null;
	private CoreManager core = null;
	
	public UserManager(UserModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public String getName(){
		return this.model.getName();
	}
	
	public void setName(String str){
		this.model.setName(str);
	}
	
	public String getAssignement() {
		return this.model.getAssignement(); 
	}

	public void setAssignement(String assignement) {
		this.model.setAssignement(assignement);
	}

	public String getGroup() {
		return this.model.getGroup();
	}

	public void setGroup(String group) {
		this.model.setGroup(group);
	}
	
	public String debug(){
		return this.model.toString();
	}
	
	public PublicKey getPublicKey(){
		return this.model.getPublicKey();
	}
	public PrivateKey getPrivateKey(){
		return this.model.getPrivateKey();
	}
	
	public void update(String str){
		if(str.equals("name"))
			this.core.getLogManager().log(this,"Changed username to \'" + this.model.getName() + "\'");
		if(str.equals("assignement"))
			this.core.getLogManager().log(this,"Changed assignement to \'" + this.model.getAssignement() + "\'");
		if(str.equals("group"))
			this.core.getLogManager().log(this,"Changed group to \'" + this.model.getGroup() + "\'");
	}

}
