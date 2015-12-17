package dataFormatter;

public enum Action {
	
	SELECT("0", "SELECT ct.E_Cred_Ksec, ct.Cred_Auto_Ref, dc.Metadonnees, dc.Valeur"), 
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