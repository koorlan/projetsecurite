package generalizer;

public class Filter {
	
	private GsaList groupList; 
	private GsaList statusList; 
	private GsaList assignementList; 
	
	public Filter()
	{
		this.groupList = null;
		this.statusList = null;
		this.assignementList = null;
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

}
