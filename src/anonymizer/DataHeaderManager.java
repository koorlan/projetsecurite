package anonymizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import manager.CoreManager;

public class DataHeaderManager {

	CoreManager core;
	DataHeaderModel model;

	public DataHeaderManager(DataHeaderModel model, CoreManager manager)
	{
		this.core = core;
		this.model = model;
	}
	/**
	 * 
	 * @return
	 * 	-1 if public keys list cannot be created
	 * 	0 in successful
	 * @throws Exception
	 */
	public int getKeysList() throws Exception
	{
		List<String> referencesOfKeys = new ArrayList<String>();
		referencesOfKeys = this.core.getDB().buildKeysList();
		return (referencesOfKeys != null ? setSubHeader(referencesOfKeys) : -1);
	}
	/**
	 * 
	 * @param referencesOfKeys
	 * 			the public keys list to add to the request 
	 */
	public int setSubHeader(List<String> referencesOfKeys)
	{	
		String subHeader = "/KEBG";
		subHeader += referencesOfKeys.size(); 
		ListIterator<String> li = referencesOfKeys.listIterator();
		while(li.hasNext()) 
			subHeader += ","+li.next();
		subHeader += "/KEND/";
		this.model.setData(subHeader);
		return 0;
	}

	public ArrayList<String> combines(String data)
	{
		String[] header = data.split("/KEND/"); 
		String toDel="/KBEG/";                          
		header[0] = header[0].replace(toDel, "");  
		String[] values = header[0].split(",");
		ArrayList<String> localref = new ArrayList<String>() ;
		for (int n = 0 ; n <  Integer.parseInt(values[0]) ; ++n ) 
		{ 
			localref.add( values[n+1] ) ;		 
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

		return localref;
	}				

}
