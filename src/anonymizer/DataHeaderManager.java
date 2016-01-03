package anonymizer;

import java.util.ArrayList;
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
			subHeader += "::"+li.next();
		subHeader += "/KEND/";
		this.model.setData(subHeader);
		return 0;
	}
}
