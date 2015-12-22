package generalizer;

import manager.CoreManager;
import java.util.ArrayList;
import generalizer.GeneralizerModel;

public class GeneralizerManager {

	private CoreManager core = null;
	
	public GeneralizerManager(GeneralizerModel model, CoreManager core) {
		super();
		this.core = core;
	}
	
	public GsaList generalize(ArrayList<String> list, String treeName)
	{
		GeneralizerModel generalizer = new GeneralizerModel();
		Tree currentTree = null;	

		if(treeName.compareToIgnoreCase("group") == 0)
			currentTree = generalizer.getGroupTree();
		else if(treeName.compareToIgnoreCase("status") == 0)
			currentTree = generalizer.getStatusTree();
		else if(treeName.compareToIgnoreCase("assignement") == 0)
			currentTree = generalizer.getAssignementTree(); 
		else 
		{
			this.core.getLog().err(this, "Unknown tree name");
			return null; 
		}		
		
		GsaList gsaList = new GsaList(list, currentTree);
		
		return gsaList;
	}
	
}
