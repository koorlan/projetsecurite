package generalizer;
import java.util.*;

/**
 * @author Lisa
 *
 */

public class Tree {
	
	private final Node root;
	 
	public Tree(Node root)
	{
		this.root = root;
	}
	 
	public String toString()
	{
		return root.toString();
	}
	
	public int size()
	{
		return root.size();
	}
	 
	public boolean contains(String i) 
	{
		return root.contains(i);
	}
	 
	public boolean equals(Object o) 
	{
		return root.equals(o);
	}
	
	public Node getNode(String value)
	{
		return root.getNode(value);
	}
	
	public ArrayList<String> generalize(String currentResearch)
	{	
		ArrayList<String> ret = new ArrayList<String>();
		if(!this.contains(currentResearch))
			return null;
		
		int variation = 0;
		if(currentResearch.compareTo("A1")!=0)
		{		
			variation = this.getNode(currentResearch).getHeight() - 66;
		}
		
		/*
		 * For debugging 		 * 
		 * char newHeight = (char)(currentResearch.codePointAt(0) - variation);
		 * String newWidth = currentResearch.substring(1, currentResearch.length() - variation);
		 * System.out.println("new height " + newHeight);
		 * System.out.println("new width " + newWidth);
		 * 
		 */
		
		String newData = new StringBuffer().append((char)(currentResearch.codePointAt(0) - variation)).append(currentResearch.substring(1, currentResearch.length() - variation)).toString().intern();		
		/*
		 * For debugging 
		 * 
		System.out.println("current data " + this.getNode(currentResearch));
		System.out.println("variation " + variation);
		System.out.println("new data " + newData);
		System.out.println("to put in the list " + this.getNode(newData));
		*
		*/
		this.getNode(newData).ordonize(ret);
		//System.out.println(ret);
		return ret;
	}
	
	
}
