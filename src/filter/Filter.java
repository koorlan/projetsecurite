package filter;

import java.util.ArrayList;

import generalizer.GsaList;
import manager.CoreManager;

public class Filter {

	private CoreManager core; 
	private FilterModel model;
	
	public Filter(FilterModel model, CoreManager core)
	{
		this.core = core; 
		this.model = model;
	}

	public boolean isSuitable()
	{
		if(this.model.isReady == false)
		{
			this.core.getLog().err(this, "Empty response detected");
			return false; 
		}
		else
		{
			if(this.model.getAssignementList().getInitialDataList().contains(this.model.getResponse().get(1)) == false)
			{
				this.core.getLog().warn(this, "<Due to generalization> Non suitable response detected");
				return false;
			}
			else if(this.model.getStatusList().getInitialDataList().contains(this.model.getResponse().get(2)) == false)
			{
				this.core.getLog().warn(this, "<Due to generalization> Non suitable response detected");
				return false; 
			}
			return true; 
		}
	
	}
	/*
	 * building a fake response atm 
	 */
	public void buildResponse()
	{
		ArrayList<String> fResponse = new ArrayList<String>();
		/* 0 */
		fResponse.add("NAME");
		/* 1 */
		fResponse.add("INSA Rouen");
		/* 2 */
		fResponse.add("Service relations internationales");
		
		
		this.model.setResponse(fResponse);
	}
	
}