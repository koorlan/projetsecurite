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

	private GsaList groupList = null; 
	private GsaList statusList = null; 
	private GsaList assignementList = null; 
	
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

	public void setGroupList(GsaList groupList) {
		this.groupList = groupList;
	}
	
	public void setStatusList(GsaList statusList) {
		this.statusList = statusList;
	}
	
	public void setAssignementList(GsaList assignementList) {
		this.assignementList = assignementList;
	}
	
	public GsaList getGroupList() {
		return this.groupList;
	}
	
	public GsaList getStatusList() {
		return this.statusList;
	}
	
	public GsaList getAssignementList() {
		return this.assignementList;
	}

	public DataUtil getDu() {
		return du;
	}

	public void setDu(DataUtil du) {
		this.du = du;
	}

}
