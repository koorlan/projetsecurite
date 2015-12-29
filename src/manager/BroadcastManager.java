package manager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import model.BroadcastModel;
import model.FrontalModel;

public class BroadcastManager {
	private CoreManager core;
	private BroadcastModel model;
	public BroadcastManager( BroadcastModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}

	public void addFrontal(FrontalModel front){
		this.model.addFrontal(front);
		this.core.getLog().log(this, "added "+ front.getName() +"@" + front.getInternalserverManager().getModel().getIpDest() +":"+ front.getInternalserverManager().getModel().getPort());
	}
	
	public void broadcast(byte[] packet){
		String buffer = new String(packet);
		this.core.getLog().log(this, "ready to broadcast \'" + buffer + "'");
		Socket socket;
		for(FrontalModel front :this.model.getFrontals()){			
			try {
				socket = new Socket(InetAddress.getByName(front.getInternalserverManager().getModel().getIpDest()),front.getInternalserverManager().getModel().getPort());		
				socket.getOutputStream().write(packet);
				socket.close();
				this.core.getLog().log(this, "sended to "+ front.getName() +"@" + front.getInternalserverManager().getModel().getIpDest() +":"+ front.getInternalserverManager().getModel().getPort());
			}catch( ConnectException e) {
				this.core.getLog().err(this, "Connection refused");
			}
			catch (NumberFormatException | IOException e ) {
				e.printStackTrace();
			} 
		}
		
	}	
	
}
