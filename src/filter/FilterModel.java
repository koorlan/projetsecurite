package filter;

import java.util.ArrayList;

import generalizer.GsaList;

public class FilterModel {

	private GsaList groupList; 
	private GsaList statusList; 
	private GsaList assignementList; 

	private String type;
	
	
	private ArrayList<String> response;
	public boolean isReady = false;
	
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

	public ArrayList<String> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<String> response) {
		this.response = response;
		this.isReady = (this.response.isEmpty() == true) ? false : true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
