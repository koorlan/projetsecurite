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
		System.out.println("Got a packet <3");
		System.out.println(request.getUser().toString());
	}
	
	
	public void group(String group, String port) throws NoSuchAlgorithmException{
		RequestModel request = new RequestModel(); 
		//populate fields we want with random number and null we don't want. Maybe use regex after to make something cool :)
		final UserModel user = new UserModel();
		user.setGroup("aaaaaaaaa");
		request.setUser(user);
		
		this.send(request, port);
	}
	
	private void send(RequestModel request, String port){
		this.core.getPacketManager().sendPacket(this.core.getPacketManager().forge("GET",request),port);
	}
	
}
