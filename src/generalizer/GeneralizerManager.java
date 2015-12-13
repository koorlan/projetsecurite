package generalizer;

import manager.CoreManager;
import model.PacketModel;

import java.util.ArrayList;
import generalizer.GeneralizerModel;

public class GeneralizerManager {

	private GeneralizerModel model = null;
	private CoreManager core = null;
	
	public GeneralizerManager(GeneralizerModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public GsaList generalize(ArrayList<String> initialList, String treeName)
	{
		GeneralizerModel generalizer = new GeneralizerModel();
		Tree currentTree = null;	

		if(treeName.compareToIgnoreCase("group") == 0)
		{
			//generalizer.setGroupTree();
			currentTree = generalizer.getGroupTree();
		}
		else if(treeName.compareToIgnoreCase("status") == 0)
		{
			//generalizer.setStatusTree();
			currentTree = generalizer.getStatusTree();
		}
		else if(treeName.compareToIgnoreCase("assignement") == 0)
		{
			//generalizer.setAssignementTree();
			currentTree = generalizer.getAssignementTree(); 
		}
		else 
		{
			this.core.getLogManager().err(this, "Unknown tree name");
			return null; 
		}		
		
		GsaList gsaList = new GsaList(initialList, currentTree);
		
		return gsaList;
	}
	
}
