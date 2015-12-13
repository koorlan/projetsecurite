package generalizer;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Lisa
 *
 */  

public abstract class Node {

	    private final String key;
	    private final String data; 
	
	    
	    public Node(String key, String data) 
	    {
	    	this.key = key;
	    	this.data = data;
	    }
	   	   
	    public String getKey()
	    {
	    	return key;
	    }
	
	    public String getData() 
		{
			return data;
		}
	    
	    public int size()
	    {
	    	return 1;
	    }
		 
	    public boolean contains (String i)
	    {
	    	return key == i;
	    }

	    public Node getNode(String value)
	    {
	    	return key == value ? this : null;
	    }
	    
	    public void ordonize(ArrayList<String> generalizedResearch)
	    {
	    	generalizedResearch.add(this.getKey());
	    }
	    
	    public int getHeight()
	    {
	    	return key.codePointAt(0);
	    }

}