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
	
	private String value;

	/* Request id value (0 : uninitialized) */ 
	private long id = 0;

	public GsaList(String nodeStr, Tree currentTree)
	{
		if(currentTree.contains(nodeStr.toString().intern()))
		{
			this.setValue(this.matchKeyToData(nodeStr.toString().intern(), currentTree));	
		}
	}
	
	public GsaList(ArrayList<String> inList, Tree currentTree)
	{	
		initialKeyList = new ArrayList<String>();
		initialDataList = new ArrayList<String>();
		ArrayList<String> TMPinitialDataList = new ArrayList<String>();
		for(String str : inList)
		{	
			if(currentTree.containsData(str.toString().intern()))
				TMPinitialDataList.add(str.toString().intern());
		}
		
		ArrayList<String> TMPinitialKeyList = matchDataToKey(TMPinitialDataList, currentTree);
		
		initialKeyList = currentTree.addSuccessors(TMPinitialKeyList.get(0));
		initialDataList = matchKeyToData(initialKeyList, currentTree);

		
/*		DEBUG 
		System.out.println("Initial Key list before adding successors " + TMPinitialKeyList );
		System.out.println("Initial key list " + initialKeyList);
		System.out.println("Initial Data list before adding successors " + TMPinitialDataList );
		System.out.println("Initial Data list " + initialDataList);
*/
		
		mainKeyList = new ArrayList<String>();
		for (String s : initialKeyList) 
		{
			mergeList(s, currentTree);
		}
		mainDataList = matchKeyToData(mainKeyList, currentTree);
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
	
	/**
	 * Find all generalized contents, merge them, remove duplicates and sort result
	 * @param s
	 * 			a node in tree
	 * @param tree
	 * 			a GSA tree
	 */
	private void mergeList(String s, Tree tree)
	{
		ArrayList<String> tmp = tree.generalize(s);
		mainKeyList.removeAll(tmp);
		for(String str : tmp)
		{
			if(str.charAt(0) == 'B')
				mainKeyList.add(str);
		}
		Collections.sort(mainKeyList);
	}
	
	public String matchKeyToData(String str, Tree tree)
	{
		return tree.getNode(str).getData();
	}
	
	public ArrayList<String> matchKeyToData(ArrayList<String> keyList, Tree tree)
	{
		ArrayList<String> tmp = new ArrayList<String>();
		for (String s : keyList) 
		{
			tmp.add(tree.getNode(s).getData());
	
		}
		return tmp;
	}
	
	private ArrayList<String> matchDataToKey(ArrayList<String> keyList, Tree tree)
	{
		ArrayList<String> tmp = new ArrayList<String>();
		for (String s : keyList) 
		{
			tmp.add(tree.getNodeData(s).getKey());
	
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
