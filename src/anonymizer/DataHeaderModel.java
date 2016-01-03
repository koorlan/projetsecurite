package anonymizer;

import java.util.*;

public class DataHeaderModel {

	private String data;
	private List<String> keys;
	
	public DataHeaderModel()
	{
		this.data = "";
		this.keys = new ArrayList<String>();
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
