package anonymizer;

import java.util.*;

public class DataHeaderModel {
	/**
	 * @attributes 
	 * 	data 
	 * 		formatted subheader to send with packet
	 * 	combination
	 * 		current user's public keys list 
	 * 	 
	 */
	private String data;
	private ArrayList<String> combination;
	
	public DataHeaderModel()
	{
		this.data = "";
		this.combination = new ArrayList<String>();
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public ArrayList<String> getCombination() {
		return combination;
	}
	public void setCombination(ArrayList<String> cb) {
		this.combination = cb;
	}
	
	
}


