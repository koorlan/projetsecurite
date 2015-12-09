package model;

import java.io.Serializable;

import manager.RequestManager;

public class RequestModel  implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestManager manager = null;
	
	private UserModel user;

	
	
	public UserModel getUser() {
		return user;
	}



	public void setUser(UserModel user) {
		this.user = user;
	}



	public void setManager(RequestManager manager){
		this.manager = manager;
	}
}
