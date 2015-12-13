package generalizer;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Lisa
 *
 */

public class Leaf extends Node{
 
	public Leaf(String key, String data)
	{
		super(key, data);
	}
 
	public int size()
	{
		return super.size();
	}
	 
	public boolean contains(String i)
	{
		return super.contains(i);
	}
	
	public Node getNode(String value)
    {
		return super.getNode(value);
    }
	
	public void ordonize(ArrayList<String> generalizedResearch)
	{
		super.ordonize(generalizedResearch);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getKey()).append(" ");
	
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof Leaf))
			return false;
	 
		Leaf l = (Leaf)o;
		return getKey() == l.getKey();
	}
}


