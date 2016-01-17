package filter;

import java.util.ArrayList;

import manager.CoreManager;

/**
 * @author lisa
 * 
 */
public class FilterManager{

	private CoreManager core; 
	private FilterModel model;
	
	public FilterManager(FilterModel model, CoreManager core)
	{
		this.core = core; 
		this.model = model;
	}
	
	/**
	 * @return true/false if received response meets user's requirements 
	 */
	public boolean isSuitable()
	{
		if(this.model.isReady == false)
		{
			this.core.getLog().err(this, "Empty response detected");
			return false; 
		}
		else
		{
			if(this.model.getAssignementList().getInitialDataList().contains(this.model.getResponse().get(this.model.getResponse().size()-1)) == false)
			{
				this.core.getLog().warn(this, "<Due to generalization> Non suitable response detected (Assignements)");
				return false;
			}
			else if(this.model.getStatusList().getInitialDataList().contains(this.model.getResponse().get(this.model.getResponse().size()-2)) == false)
			{
				this.core.getLog().warn(this, "<Due to generalization> Non suitable response detected (Status)");
				return false; 
			}
			//Calculate how many groups.
			int nbGroups = this.model.getResponse().size() -3 ; //name+status+assignement
			boolean oneFounded = false;
			for (int i=0; i < nbGroups ; i++){
				if(this.model.getGroupList().getInitialDataList().contains(this.model.getResponse().get(1+i)) == true)
					oneFounded = true;
			}
			if(!oneFounded){
				this.core.getLog().warn(this, "<Due to generalization> Non suitable response detected (Groups)");
				return false; 
			}
			
			return true; 
		}
	
	}
	
	public FilterModel getModel(){
		return this.model;
	}
	/**
	 * TODO : <CLEAN> building a fake response atm 
	 */
	public void buildResponse()
	{
		ArrayList<String> fResponse = new ArrayList<String>();
		/* 0 TYPE */
		fResponse.add("NAME");
		/* 1 ASSIGNEMENT */
		fResponse.add("INSA Rouen");
		/* 2 STATUS */
		fResponse.add("Service relations internationales");
		
		
		this.model.setResponse(fResponse);
	}
	
}
