package manager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;

import model.BroadcastModel;
import model.FrontalModel;
import model.PacketModel;
import model.RequestModel;

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
		//String buffer = new String(packet);
		//this.core.getLog().log(this, "ready to broadcast \'" + buffer + "'");
		PacketModel packetM = (PacketModel)SerializationUtils.deserialize(packet);
		RequestModel requestM = (RequestModel)SerializationUtils.deserialize(packetM.getContent());
		
		System.out.println("Broadcast : >>");
		System.out.println("<Packet>");
		System.out.println("<id>"+ new String(packetM.getId())+" </id>");
		System.out.println("<familly>"+ packetM.getSenderFamilly() +" </familly>");
		System.out.println("<Request>");
		
		System.out.println("<DataUtil>"+ requestM.getDu() +"</DataUtil>");
		
		System.out.println("</Request>");
		System.out.println("</Packet>");
		
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
