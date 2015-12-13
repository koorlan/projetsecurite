package dataFormatter;

public enum Action {
	
	SELECT("0", "SELECT type, ref, encipher_skey, owner_grp, owner_stat, owner_aff"), 
	CREATE("1", "CREATE");

	private String key = "";
	private String value = "";
	
	Action(String key, String value) 
	{	
		this.key = key;
		this.value = value; 
	}
	
	public String getKey() 
	{	
		return key;
	}
	
	public String getValue() 
	{	
		return value;
	}
	
	public String toString() 
	{	
		return key;
	}
}