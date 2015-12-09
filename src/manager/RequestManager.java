package manager;

import java.security.NoSuchAlgorithmException;

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
	
}
