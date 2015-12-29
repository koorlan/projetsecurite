package dataFormatter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lisa
 *
 */
public class DataUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private String data;
	
	public DataUtil()
	{
		this.data = "/BEG/";
	}
	
	/**
	 * 
	 * Method called by : [FRONTAL] [USER'S LOCAL APP]
	 * Context : To forge a response  
	 * @param content	Matches with a key value in "Content", shows whether answer is empty or not
	 */
	public void setContent(String content)
	{
		if(content.compareTo("TRASH") == 0)
		{
			data += Content.TRASH.getKey();
		}
		else if(content.compareTo("FULL") == 0)
		{
			data += Content.FULL.getKey();
		}
	}
	
	/**
	 * 
	 * Method called by : [FRONTAL] [USER'S LOCAL APP]
	 * Context : To forge a response 
	 * @param result	The full database results  	
	 */
	public void setResults(ArrayList<String> result)
	{
		if(result.isEmpty())
		{	//TODO : <CLEAN> debug only 
			System.out.println("<Building answer failed> Empty result non recognized previously");
		}
		else
		{
			data += result.toString();
		}
		
	}
	
	/**
	 * 
	 * Method called by : [USER'S LOCAL APP]
	 * Context : To forge a query or a response 
	 * @param action	Matches with a key value in "Action", shows whether request contains a query or a response
	 */
	public void setAction(String action)
	{
		if(action.compareTo("QUERY") == 0)
		{	
			data += Action.QUERY.getKey();
		}
		else if(action.compareTo("ANSWER") == 0)
		{	
			data += Action.ANSWER.getKey();
		}
		
		data += "::";
	}

	/**
	 * 
	 * Method called by : [USER'S LOCAL APP]
	 * Context : To forge a query 
	 * @param type	Matches with a key value in "Type", shows required data type (filled by user)
	 */
	public void setType(String type)
	{
		if(type.compareTo("Nom") == 0)
		{	
			data += Type.NAME.getKey();
		}
		else if(type.compareTo("Affectation") == 0)
		{	
			data += Type.ASSIGNEMENT.getKey();
		}	
		else if(type.compareTo("Statut") == 0)
		{	
			data += Type.STATUS.getKey();
		}
		else if(type.compareTo("AdresseMail") == 0)
		{
			data += Type.MAIL.getKey();
		}
		else if(type.compareTo("Tel") == 0)
		{
			data += Type.TEL.getKey();
		}
		else if(type.compareTo("Photo") == 0)
		{
			data += Type.PICTURE.getKey();
		}	
		data += "::";
	}
	
	/**
	 * 
	 * Method called by : [USER'S LOCAL APP]
	 * Context : To forge a query 
	 * @param table		Matches with a key value in "Table", shows what tables will be used to process query 
	 */
	public void setTable(String table)
	{
		if(table.compareTo("FRONT_TABLE") == 0)
		{	
			data += Table.FRONT_TABLE.getKey();
		}
		else if(table.compareTo("DATA_TABLE") == 0)
		{	
			data += Table.DATA_TABLE.getKey();
		}
		
		data += "::";
	}
	
	/**
	 * 
	 * Method called by : [USER'S LOCAL APP]
	 * Context : To forge a query 
	 * @param gsaList	Shows required GSA (filled by user)
	 */
	public void setGSA(ArrayList<String> gsaList)
	{
		for(String gsa : gsaList)
			data += gsa;
		data += "::";
	}
	
	/**
	 * 
	 * Method called by : [FRONTAL] [USER'S LOCAL APP]
	 * Context : To forge a query or response 
	 */
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
