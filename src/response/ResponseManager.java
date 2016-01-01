package response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataFormatter.Action;
import dataFormatter.Content;
import dataFormatter.DataUtil;
import manager.CoreManager;
import model.RequestModel;

/**
 * @author lisa
 *
 */
public class ResponseManager {
		
		ResponseModel model;
		CoreManager core;
		private Matcher matcher;
		
		public ResponseManager(ResponseModel model, CoreManager core)
		{
			this.core = core; 
			this.model = model;
		}
		
		private boolean match(String data)
		{
			Pattern p = Pattern.compile(this.model.getRegex());
			this.matcher = p.matcher(data);
			return matcher.matches();
		}
		
		public boolean isResponse(DataUtil du)
		{
			if(this.match(du.getData()))
			{
				this.model.setFormatted(true);
				for (Action action : Action.values()) 
				{
					if (matcher.group(2).compareTo(action.getKey()) == 0)
					{	//TODO : <DEBUG> Clean this later
						System.out.println(action.getValue());
						return (action == Action.ANSWER);
					}
				
				}		
			}
			else
			{
				this.core.getLog().err(this, "<POST METHOD> Non formatted data received");
			}
			return false;
		}
		
		public boolean isEmpty(DataUtil du)
		{	
			if(this.match(du.getData()))
			{
				for(Content content : Content.values() ) 
				{
					if(matcher.group(3).compareTo(content.getKey()) == 0)
					{
						return(content.getKey() == "1");
					}
				}
			}
			//TODO <DEBUG> clean this later
			System.out.println("<POST METHOD> Empty answer received");
			return false; 
		}
	
		
		public void trash(RequestModel response)
		{	
			this.core.getLog().warn(this, "Response trashed");
		}
		
		
		
}
