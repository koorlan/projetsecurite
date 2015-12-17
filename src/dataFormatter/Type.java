package dataFormatter;

public enum Type {
	
	ALL("0", "*"),
	NAME("1", "Nom"), 
	ASSIGNEMENT("2", "Affectation"),
	STATUS("3", "Statut"),
	MAIL("4", "AdresseMail"),
	TEL("5", "Tel"),
	PICTURE("6", "Photo");

	private String value = "";
	private String key = ""; 
	
	Type(String key, String value) 
	{
		this.value = value;
		this.key = key;	
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
		return "key :"+ key + " value :" + value;
	}
	
}
