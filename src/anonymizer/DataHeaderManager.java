package anonymizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import manager.CoreManager;

public class DataHeaderManager {

	private CoreManager core;
	private DataHeaderModel model;

	public DataHeaderManager(DataHeaderModel model, CoreManager core)
	{
		this.core = core;
		this.model = model;
	}
	/**
	 * Requirements :
	 * 	keys, a list of true and false keys 
	 * 	a DBManager which : 
	 * 		finds true keys
	 * 		adds the combo in requestModel for filtering 
	 * 		finds false keys
	 * 		returns the entire keys list (not combined)
	 *  
	 * @return
	 * 	-1 if public keys list cannot be created
	 * 	0 in successful
	 * @throws Exception
	 */
	public int getKeysList() throws Exception
	{
		List<String> keys = new ArrayList<String>();
		keys = this.core.getDB().buildKeysLists();
		return (keys != null ? setSubHeader(keys) : -1);
	}
	/**
	 * Sets subheader to send & stores owner's keys combination 
	 * @param referencesOfKeys
	 * 			public keys list to add to the request 
	 * @return 
	 * 		0 in successful
	 */
	public int setSubHeader(List<String> keys)
	{	
		String subHeader = "/KBEG/";
		for(int i = 0 ; i < keys.size() ; i++ )
		{	
			subHeader += keys.get(i) 
					+ (i == keys.size() - 1 ? "/KEND/" : ",");		
		}
		this.model.setData(subHeader);
		return 0;
	}

	/**
	 * Interprets subheader and combines
	 *  public keys received
	 * @param data
	 * 			formatted subheader received
	 * @return
	 * 		localref, combination of all keys
	 */
	public ArrayList<String> combines(String data)
	{
		// TODO clean 
		System.out.println("header received : " + data);
		String[] header = data.split("/KEND/"); 
		String toDel="/KBEG/";                          
		header[0] = header[0].replace(toDel, "");  
		String[] values = header[0].split(",");
		ArrayList<String> localref = new ArrayList<String>() ;
		for (int i = 0 ; i <  values.length ; i++ ) 
		{ 
			localref.add( values[i] ) ;		 
		}	
		// display and store combinations 
		localref.add(localref.get(0) + "," + localref.get(1));
		localref.add(localref.get(0) + "," + localref.get(2));
		localref.add(localref.get(0) + "," + localref.get(3));
		localref.add(localref.get(1) + "," + localref.get(2));
		localref.add(localref.get(1) + "," + localref.get(3));
		localref.add(localref.get(2) + "," + localref.get(3));		
		localref.add(localref.get(0) + "," + localref.get(1) + "," + localref.get(2));
		localref.add(localref.get(0) + "," + localref.get(1) + "," + localref.get(3));
		localref.add(localref.get(0) + "," + localref.get(2) + "," + localref.get(3));
		localref.add(localref.get(1) + "," + localref.get(2) + "," + localref.get(3));
		localref.add(localref.get(0) + "," + localref.get(1) + "," + localref.get(2) + "," + localref.get(3));
		// TODO clean 
		System.out.println(localref);
		return localref;
	}				
	/**
	 * Compares authorized credentials references 
	 * 	with owners credentials combo list
	 * @param response
	 * 			host response to request 
	 * @return	result 			
	 * 	null if credentials list elements don't match 
	 * 	a list of credentials combination which matches 
	 */
	public ArrayList<String> checkPolicy(ArrayList <String> response)
	{
		int n = response.size() / 4;
		System.out.println("response size : " + response.size());
		ArrayList<String> result = new ArrayList<String>();
		if(n == 0)
			return null; 
		for(int i = 0; i < n; i++)
		{
			for(String str : this.model.getCombination() )
			{
				if(response.get(4 * i + 1).equals(str) )
				{
					result.add(response.get(4 * i));
					result.add(response.get(4 * i + 1));
					result.add(response.get(4 * i + 2));
					result.add(response.get(4 * i + 3));
				}
			}
		}
		
		System.out.println(result);
		return result.isEmpty() ? null : result;  
	}
	
	/**
	 * Setting owner public keys combination list into model 
	 * @param statusRef
	 * 	current user's status pkey reference
	 * @param assignementRef
	 * 	current user's assignement key reference
	 */
	public void setCombination(String statusRef, String assignementRef)
	{
		ArrayList <String> combo = new ArrayList<String>();
		combo.add(statusRef);
		combo.add(assignementRef);
		combo.add(statusRef + "," + assignementRef);
		this.model.setCombination(combo);
	}
	
	public String getDataM()
	{
		return this.model.getData();
	}
}
