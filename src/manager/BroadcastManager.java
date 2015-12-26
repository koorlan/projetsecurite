package manager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import model.BroadcastModel;
import model.Front;

public class BroadcastManager {
	private CoreManager core;
	private BroadcastModel model;
	public BroadcastManager( BroadcastModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void broadcast(byte[] packet){
		Socket socket;
		for(Front front :this.model.getFronts()){			
			try {
				socket = new Socket(InetAddress.getByName(front.getIp()),front.getPort());		
				socket.getOutputStream().write(packet);
				socket.close();			
			}catch( ConnectException e) {
				this.core.getLog().err(this, "Connection refused");
			}
			catch (NumberFormatException | IOException e ) {
				e.printStackTrace();
			} 
		}
		
	}
	
	
}