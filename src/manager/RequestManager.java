package manager;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dataFormatter.DataUtil;
import filter.Filter;
import filter.FilterModel;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import model.RequestModel;
import model.UserModel;

public class RequestManager {
	
	private CoreManager core;
	private RequestModel model;
	
	public RequestManager(RequestModel model,CoreManager core){
		super();
		this.core = core;
		this.model = model;
	}
	
	//Not here..
	//public void sendIdentity(String port){
	//	this.core.getPacketManager().sendPacket(this.core.getPacketManager().forge("POST", this.core.getUserManager().debug()),port);
	//}
	
	public byte[] process(RequestModel request) throws ClassNotFoundException, SQLException
	{
		ArrayList<String> results = new ArrayList<String>();
		this.core.getDB().build(request.getDu());
		if(this.core.getDB().isFormatted())
		{
			results = this.core.getDB().search();
			System.out.println(results);
			
			//here forge POST to return...
			this.core.getPacket().forge("POST", "ANSWER");
			return null;
		}
		else
			this.core.getLog().err(this, "Non formatted content");
		return null;
	}	
	/*
	public void process(RequestModel request){
		//Just user here but free feel to add attribute in RequestModel (Note: don't forget Serializable)
		if( !(request instanceof RequestModel)){
			this.core.getLogManager().err(this,"Not a RequestModel format exiting.");
		}
		
		System.out.println(request.getUser().toString());
		
		
		//prepare response for the Answer manager TODO(he is not implemented... maybe think about a standard to communicate)
		//For the moment just concat GSA
		String query = "";
		//Analyze.
		if(!request.getUser().getName().equals(""))
			query += "n";
		if(!request.getUser().getGroup().equals(""))
			query += "g";
		if(!request.getUser().getAssignement().equals(""))
			query += "a";
		
		System.out.println("Query Interpreted : \'" + query +"\'");
		System.out.println("Mini - Interpreter - w/out check policy");
		if(query.contains("n"))
			System.out.println("Name :" + request.getUser().getName() + "Response :" + this.core.getUserManager().getName() );
		if(query.contains("g"))
			System.out.println("Group :" + request.getUser().getGroup()+ "Response :" + this.core.getUserManager().getGroup());
		if(query.contains("a"))
			System.out.println("Assignement :" + request.getUser().getAssignement()+ "Response :" + this.core.getUserManager().getAssignement());
		
	}
	*/

	private void send(RequestModel request) throws ClassNotFoundException, SQLException{
		this.core.getPacket().sendPacket(this.core.getPacket().forge("GET",request),this.core.getDB().getFrontalIP(),this.core.getDB().getFrontalPort());
	}
	


	public void forge(Object type, Object group, Object status, Object assignement) throws ClassNotFoundException, SQLException
	{
		if(!(type instanceof String && group instanceof String && status instanceof String && assignement instanceof String))
		{
			this.core.getLog().err(this, "Wrong fields");
		}
		FilterModel filterM = new FilterModel();
		Filter filter = new Filter(filterM, this.core);
		
		GeneralizerModel genM = new GeneralizerModel();
		GeneralizerManager genManager = new GeneralizerManager(genM, this.core);	
		
		ArrayList<String> groupList = new ArrayList<String>();
		ArrayList<String> statusList = new ArrayList<String>();
		ArrayList<String> assignementList = new ArrayList<String>();
		
		groupList.add((String) group);
		statusList.add((String) status);
		assignementList.add((String) assignement);	
	
		filterM.setGroupList(genManager.generalize(groupList,"group"));
		filterM.getGroupList().printGsaList();
		filterM.setStatusList(genManager.generalize(statusList,"status"));
		filterM.getStatusList().printGsaList();
		filterM.setAssignementList(genManager.generalize(assignementList,"assignement"));
		filterM.getAssignementList().printGsaList();
		
		/* building formatted string */
		DataUtil du = new DataUtil();		
		/* Set Action */
		du.setAction("SELECT");		
		/* Set Type */ 
		du.setType((String)type);		
		/* Set Table */ 
		du.setTable("SP_TABLE");		
		/* Set GSA stuff */
		du.setGSA(filterM.getGroupList().getMainKeyList());
		du.setGSA(filterM.getStatusList().getMainKeyList());
		du.setGSA(filterM.getAssignementList().getMainKeyList());
		/* close formatted string */
		du.close();
		/* Print data util */
		System.out.println(du);
		
		RequestModel tosend = new RequestModel();
		tosend.setDu(du);
		this.send(tosend);
	}

	
}
