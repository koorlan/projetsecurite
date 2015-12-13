package generalizer;
import java.util.ArrayList;
import java.util.Collections;

public class GsaList{
	
	private static String newline = System.getProperty("line.separator");
	
	/* Lists for key values (e.g "A1") */
	private ArrayList<String> initialKeyList;
	private ArrayList<String> mainKeyList;
	
	/* Lists for data values (e.g "Statut global") */
	private ArrayList<String> initialDataList;
	private ArrayList<String> mainDataList;
	
	/* Request id value (0 : uninitialized) */ 
	private long id = 0;

	public GsaList(ArrayList<String> inList, Tree currentTree)
	{	
		initialKeyList= new ArrayList<String>();
		for(String str : inList)
		{	
			if(currentTree.contains(str.toString().intern()))
				initialKeyList.add(str.toString().intern());
		}
		
		/* keep request's goal in initial* lists */ 
		initialDataList = matchList(initialKeyList, currentTree);
		
		/* generalize request and put results in main* lists */
		mainKeyList = new ArrayList<String>();
		for (String s : initialKeyList) 
		{
			mergeList(s, currentTree);
		}
		mainDataList = matchList(mainKeyList, currentTree);
		
	}

	public ArrayList<String> getInitialKeyList()
	{
		return initialKeyList; 
	}
	
	
	public ArrayList<String> getMainKeyList()
	{
		return mainKeyList; 
	}
	
	public ArrayList<String> getInitialDataList()
	{
		return initialDataList; 
	}

	public ArrayList<String> getMainDataList()
	{
		return mainDataList; 
	}
	
	private void mergeList(String s, Tree tree)
	{
		/* Merge all generalized contents, remove duplicates and sort result */  
		ArrayList<String> tmp = tree.generalize(s);
		mainKeyList.removeAll(tmp);
		mainKeyList.addAll(tmp);
		Collections.sort(mainKeyList);
	}
	
	private ArrayList<String> matchList(ArrayList<String> keyList, Tree tree)
	{
		ArrayList<String> tmp = new ArrayList<String>();
		for (String s : keyList) 
		{
			tmp.add(tree.getNode(s).getData());
	
		}
		return tmp;
	}
	
	public void printGsaList()
	{
		System.out.println(newline + "_____________________________" + newline);
		System.out.println("Niveaux demandes initialement");
		System.out.println(this.getInitialKeyList() + newline + this.getInitialDataList());
		System.out.println(newline + "Niveaux retenus apres bruitage");
		System.out.println(this.getMainKeyList() + newline + this.getMainDataList());
		System.out.println("_____________________________");
	}
	
	public long getId()
	{
		return this.id;
	}
}
