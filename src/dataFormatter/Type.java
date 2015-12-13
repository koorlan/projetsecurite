package dataFormatter;

public enum Type {
	
	ALL("0", "*"),
	NAME("1", "name"), 
	PICTURE("2", "img"),
	ADDRESS("3", "addr"),
	GROUP("4", "group"),
	STATUS("5", "status"),
	ASSIGNEMENT("6", "assignement");

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
