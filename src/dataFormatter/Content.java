package dataFormatter;

public enum Content {
	
	TRASH("0", "EMPTY ANSWER RECEIVED"), 
	FULL("1", "FULL ANSWER RECEIVED");

	private String key = "";
	private String value = "";
	
	Content(String key, String value) 
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
