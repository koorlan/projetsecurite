package manager;

import model.RequestModel.*;
import model.RequestModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;



public class RequestManager {
	private CoreManager core;
	private RequestModel model;
	
	public RequestManager(RequestModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void process(RequestModel req){
		this.core.getLogManager().log(this,"New Request arrived..processing");
		//Dirty way but for debug
		System.out.println("Request Type :" + req.getType().toString());
		System.out.println("Stringed content: " + new String(req.getContent()));
		System.out.println("EOF flag: " + req.isEof());	
		return;
	}	
	
	public RequestModel forge(String type, Object content){
		try{
		RequestModel req = new RequestModel();
		req.setType(Type.valueOf(type));
		if( content instanceof String)
			req.setContent(((String) content).getBytes());
		else //assuming bytes..
			req.setContent((byte[]) content);
		
		//post current to request model in order to send ... Support 1 request in temp.
		//normally not need when automation but usefull when forge the send.
		this.model.save(req);
		return req;
		}catch(IllegalArgumentException e){
			this.core.getLogManager().err(this,"args <"+ type.getClass().getName().toString() +" {"+ Arrays.toString(Type.values()) +"},"+content.getClass().getName().toString()+ ">");
		}
		return null;
	}
	
	public void send(String port){
		//grab temp request
		RequestModel req = this.model.getRequest();
		//Security
		req = this.core.getSecurityManager().encryptRequest(req);
		
		
		//construct socket on the fly.
		//TODO get frontal information
		
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(port));
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("write object");
			oos.writeObject(req);
			oos.flush();
			oos.close();
			socket.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRequest(RequestModel req){
		System.out.println("TARACE");
	}

}
