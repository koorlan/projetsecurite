package manager;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dataFormatter.DataUtil;
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
	
	public void process(RequestModel request) throws ClassNotFoundException, SQLException
	{
		if( !(request instanceof RequestModel)){
			this.core.getLogManager().err(this,"Not a RequestModel format exiting.");
		}
		DBManager dbm = new DBManager();
		ArrayList<String> results = new ArrayList<String>();
		dbm.build(request.getDu());
		if(dbm.isFormatted())
		{
			results = dbm.search();
			System.out.println(results);
		}
		
		else
			this.core.getLogManager().err(this, "Not a DataUtil format.");
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
	
	public void group(String group, String port) throws NoSuchAlgorithmException{
		RequestModel request = new RequestModel(); 
		//populate fields we want with random number and null we don't want. Maybe use regex after to make something cool :)
		final UserModel user = new UserModel();
		user.setGroup(group);
		request.setUser(user);
		
		this.send(request, port);
	}
	
	private void send(RequestModel request, String port){
		this.core.getPacketManager().sendPacket(this.core.getPacketManager().forge("GET",request),port);
	}
	


	public void forge(String req, String port){

		//construct 3 GSA LIST		<<OK>> 
		//generate data utils		<<OK>>
		//store request				<<OK>>
		//send data utils                                                                                               
		
	
		RequestModel request = new RequestModel();

		String[] split = req.split(":");
		
		/* saving required data type */
		String type = split[0];
				
		/* building group list */		
		String[] groupSplit = split[1].split(",");
		ArrayList<String> groupList = new ArrayList<String>();
		for(String str: groupSplit)
		{
			groupList.add(str);
		}
		GeneralizerModel genModel = new GeneralizerModel();
		GeneralizerManager genManager = new GeneralizerManager(genModel, this.core);		
		request.setGroupList(genManager.generalize(groupList,"group"));
		request.getGroupList().printGsaList();
		
		/* building status list */
		String[] statusSplit = split[2].split(",");
		ArrayList<String> statusList = new ArrayList<String>();
		for(String str: statusSplit)
		{
			statusList.add(str);
		}
		request.setStatusList(genManager.generalize(statusList,"status"));
		request.getStatusList().printGsaList();
	
		/* building assignement list */
		String[] assignementSplit = split[3].split(",");
		ArrayList<String> assignementList = new ArrayList<String>();
		for(String str: assignementSplit)
		{
			assignementList.add(str);
		}
		request.setAssignementList(genManager.generalize(assignementList,"assignement"));
		request.getAssignementList().printGsaList();
		
		/* building formatted string */
		DataUtil du = new DataUtil();
		
		/* Set Action */
		du.setAction("SELECT");
		
		/* Set Type */ 
		du.setType(type);
		
		/* Set Table */ 
		du.setTable("SP_TABLE");
		
		/* Set GSA stuff */
		du.setGSA(request.getGroupList().getMainKeyList());
		du.setGSA(request.getStatusList().getMainKeyList());
		du.setGSA(request.getAssignementList().getMainKeyList());

		du.close();
	
		/* Print du */
		System.out.println(du);
		
		RequestModel tosend = new RequestModel();
		tosend.setDu(du);
		this.send(tosend, port);
	}

	
}
