package manager;

import model.RequestModel.*;
import model.RequestModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;



public class RequestManager {
	private CoreManager core;
	private RequestModel model;
	
	public RequestManager(RequestModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void process(byte[] req){	
		this.core.getLogManager().log(this,"New Request arrived..processing");
		//maybe check integrity maybe after decoding...
		
		//Dirty way but for debug
		System.out.println("PlainPacket");
		System.out.println(new String(req));
		
		System.out.println("DECRYPTING.....");
		
		RequestModel request = new RequestModel();
		request = (RequestModel)SerializationUtils.deserialize(this.core.getSecurityManager().decryptRequest(req));
		if(req != null){
			System.out.println("Request Type :" + request.getType().toString());
			System.out.println("Stringed content: " + new String(request.getContent()));
			System.out.println("EOF flag: " + request.isEof());	
		}else{
			this.core.getLogManager().warn(this,"There was an error on the packet" );
		}
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
		byte[] sreq = this.core.getSecurityManager().encryptRequest(SerializationUtils.serialize(req));
		
		
		//construct socket on the fly.
		//TODO get frontal information
		
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(port));		
			socket.getOutputStream().write(sreq);
			socket.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRequest(RequestModel req, String port){
		this.model.save(req);
		this.send(port);
	}

}
