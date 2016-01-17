package anonymizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import manager.CoreManager;

public class DataHeaderManager {

	private CoreManager core;
	private DataHeaderModel model;

	public DataHeaderManager(DataHeaderModel model, CoreManager core) {
		this.core = core;
		this.model = model;
	}

	/**
	 * Requirements : keys, a list of true and false keys a DBManager which :
	 * finds true keys adds the combo in requestModel for filtering finds false
	 * keys returns the entire keys list (not combined)
	 * 
	 * @return -1 if public keys list cannot be created 0 in successful
	 * @throws Exception
	 */

	public int getKeysList() throws Exception
	{
		List<String> tmpK = new ArrayList<String>(this.core.getCryptoUtils().getKeySet());
		Collections.sort(tmpK);
		
		// TODO debug clean this later
		System.out.println(tmpK);
		List<String> keys = new ArrayList<String>();
		keys = this.core.getDB().buildKeysList(tmpK);
		return (keys != null ? setSubHeader(keys) : -1);
	}

	/**
	 * Sets subheader to send & stores owner's keys combination
	 * 
	 * @param referencesOfKeys
	 *            public keys list to add to the request
	 * @return 0 in successful
	 */
	public int setSubHeader(List<String> keys) {
		String subHeader = "/KBEG/";
		for (int i = 0; i < keys.size(); i++) {
			subHeader += keys.get(i) + (i == keys.size() - 1 ? "/KEND/" : ",");
		}
		this.model.setData(subHeader);
		return 0;
	}

	/**
	 * Interprets subheader and combines public keys received
	 * 
	 * @param data
	 *            formatted subheader received
	 * @return localref, combination of all keys
	 */
	public ArrayList<String> combines(String data) {
		// TODO clean
		System.out.println("header received : " + data);
		String[] header = data.split("/KEND/");
		String toDel = "/KBEG/";
		header[0] = header[0].replace(toDel, "");
		String[] values = header[0].split(",");

		ArrayList<String> localref = new ArrayList<String>() ;
		for (int i = 0 ; i <  values.length ; i++ ) 
		{ 
			localref.add( values[i] ) ;		 
		}	
		// display and store combinations 
		String statusRef = localref.get(localref.size() - 1);
		localref.remove(localref.size() - 1);
		
		String assignementRef = localref.get(localref.size() - 1);
		localref.remove(localref.size() - 1);
		
		List<String> groupList = localref;
		
		return displayCombination(statusRef, assignementRef, groupList);
	}	
	

	/**
	 * Compares authorized credentials references with owners credentials combo
	 * list
	 * 
	 * @param response
	 *            host response to request
	 * @return result null if credentials list elements don't match a list of
	 *         credentials combination which matches
	 */
	public boolean[] checkPolicy(ArrayList<String> response) {
		System.out.println("response size : " + response.size());
		if (response.size() <= 0)
			return null;
		boolean[] result = new boolean[response.size()]; // Au max..
		for (int i = 0; i < response.size(); i++) {
				result[i] = this.model.getCombination().contains(response.get(i))?true:false;

		}
		System.out.println(result);
		return result.length == 0 ? null : result;
	}

	/**
	 * Setting owner public keys combination list into model
	 * 
	 * @param statusRef
	 *            current user's status pkey reference
	 * @param assignementRef
	 *            current user's assignement key reference
	 */

	public void setCombination(String statusRef, String assignementRef, List<String> group)
	{
	    Combination cb = new Combination();
		ArrayList<String> list = new ArrayList<String>();
	    List<List<String>> powerSet = new LinkedList<List<String>>();
		list.addAll(group);
	    list.add(statusRef);
		list.add(assignementRef);
		for (int i = 1; i < list.size(); i++)
	    	powerSet.addAll(Combination.combination(list, i));
	    this.model.setCombination(cb.formatMyList(powerSet));
	    this.core.getLog().log(this, "Combination added" + this.model.getCombination());
	}

	
	public ArrayList<String> displayCombination(String statusRef, String assignementRef, List<String> group)
	{
	    Combination cb = new Combination();
		ArrayList<String> list = new ArrayList<String>();
	    List<List<String>> powerSet = new LinkedList<List<String>>();
		list.addAll(group);
	    list.add(statusRef);
		list.add(assignementRef);
		for (int i = 1; i < list.size(); i++)
	    	powerSet.addAll(Combination.combination(list, i)); 
	    return cb.formatMyList(powerSet);
	}

	
	public String getDataM()
	{
		return this.model.getData();
	}
}
