package model;

import java.io.Serializable;

import dataFormatter.DataUtil;
import generalizer.GsaList;
import manager.RequestManager;

public class RequestModel  implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestManager manager = null;
	private UserModel user;
	private DataUtil du = null; 
	
	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public void setManager(RequestManager manager){
		this.manager = manager;
	}

	public DataUtil getDu() {
		return this.du;
	}

	public void setDu(DataUtil du) {
		this.du = du;
	}

}
