package dataFormatter;

public enum Table {
	
	//share policy table 
	SP_TABLE("0", "sp_table"),
	//
	DATA_TABLE("1", "data_table");
	
	private String key = "";
	private String value = "";
	
	Table(String key, String value) 
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
