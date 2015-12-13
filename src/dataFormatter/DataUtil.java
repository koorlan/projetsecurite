package dataFormatter;
import java.io.Serializable;
import java.util.ArrayList;

public class DataUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String data;
	
	public DataUtil()
	{
		this.data = "/BEG/";
	}
	
	public void setAction(String action)
	{
		if(action.compareTo("SELECT") == 0)
		{	
			data += Action.SELECT.getKey();
		}
		else if(action.compareTo("CREATE") == 0)
		{	
			data += Action.CREATE.getKey();
		}
		
		data += "::";
	}
	
	public void setType(String type)
	{
		if(type.compareTo("name") == 0)
		{	
			data += Type.NAME.getKey();
		}
		else if(type.compareTo("img") == 0)
		{	
			data += Type.PICTURE.getKey();
		}	
		else if(type.compareTo("addr") == 0)
		{	
			data += Type.ADDRESS.getKey();
		}
		else if(type.compareTo("group") == 0)
		{
			data += Type.GROUP.getKey();
		}
		else if(type.compareTo("status") == 0)
		{
			data += Type.STATUS.getKey();
		}
		else if(type.compareTo("assignement") == 0)
		{
			data += Type.ASSIGNEMENT.getKey();
		}	
		data += "::";
	}
	
	public void setTable(String table)
	{
		if(table.compareTo("SP_TABLE") == 0)
		{	
			data += Table.SP_TABLE.getKey();
		}
		else if(table.compareTo("DATA_TABLE") == 0)
		{	
			data += Table.DATA_TABLE.getKey();
		}
		
		data += "::";
	}

	public void setGSA(ArrayList<String> gsaList)
	{
		for(String gsa : gsaList)
			data += gsa;
		data += "::";
	}
	
	public void close()
	{
		data += "/END/";
	}
	
	public String getData()
	{
		return this.data;
	}

	public String toString()
	{
		return this.data;
	}
}
