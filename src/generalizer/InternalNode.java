package generalizer;
/**
 * @author Lisa
 *
 */

import java.util.*;

public class InternalNode extends Node{
	
	private final List<Node> children;
	 
	public InternalNode(String key, String data, List<Node> children)
	{
		super(key, data);
		this.children = children;
	}
	 
	public List<Node> getChildren()
	{
		return children;
	}
	 
	public int size()
	{
		int size = 1;
		for(Node n : children)
		{
	 
			size += n.size();
		}
		return size;
	}
	 
	public boolean contains(String i)
	{
		if(getKey() == i)
			return true;
	 
		boolean ret = false;
		int j = 0;
	 
		while(!ret && j < children.size())
		{
			ret = children.get(j++).contains(i);
		}
		return ret;
	}
	
	public Node getNode(String value)
    {
		if(getKey() == value)
			return this;
	 
		Node ret = null;
		int j = 0;
	 
		while(ret == null && j < children.size())
		{
			ret = children.get(j++).getNode(value);
		}
		return ret; 
    }
	
	public void ordonize(ArrayList<String> generalizedResearch)
	{
		int j = 0; 
		while(j < children.size())
		{
			children.get(j++).ordonize(generalizedResearch);
		}
		generalizedResearch.add(this.getKey());
	}
	 
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getKey()).append(" ").append(children.toString());
	 
		return sb.toString();
	}
		 
	public boolean equals(Object o)
	{
		boolean ret = false;
	 
		if(!(o instanceof InternalNode))
			return ret;
	 
		InternalNode n = (InternalNode)o;
		return (getKey() == n.getKey() && children.equals(n.children));
	}
}
