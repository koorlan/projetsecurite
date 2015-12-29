package model;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import manager.UserManager;

//TODO check if a better way than we can serialize user...
public class UserModel implements Serializable{
	private UserManager manager = null;
	
	private String name = new String();
	
	private String group = new String();
	private String status = new String();
	private String assignement = new String();
	
	
	private KeyPair key = null;
	
	public UserModel() throws NoSuchAlgorithmException{		
		 KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("RSA");
		 KeyGen.initialize(2048);
		 KeyPair pair = KeyGen.generateKeyPair();
		 this.key = pair;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.update("name");
	}



	public String getAssignement() {
		return assignement;
	}



	public void setAssignement(String assignement) {
		this.assignement = assignement;
		this.update("assignement");
	}



	public String getGroup() {
		return this.group;
	}



	public void setGroup(String group) {
		this.group = group;
		
		this.update("group");		
	}



	public void setManager(UserManager manager){
		this.manager = manager;
		return;
	}

	public PublicKey getPublicKey(){
		return this.key.getPublic();
	}
	public PrivateKey getPrivateKey(){
		return this.key.getPrivate();
	}
	
	public void update(String str) {
		if(this.manager != null)
			this.manager.update(str);
		else 
			return;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserModel [manager=" + manager + ", name=" + name + ", assignement=" + assignement + ", group=" + group
				+ ", getName()=" + getName() + ", getAssignement()=" + getAssignement() + ", getGroup()=" + getGroup()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
}
