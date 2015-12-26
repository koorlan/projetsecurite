package manager;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import model.PacketModel.*;
import model.RequestModel;
import model.PacketModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

//import org.apache.commons.lang3.SerializationException;
//import org.apache.commons.lang3.SerializationUtils;



public class PacketManager {
	private CoreManager core;
	private PacketModel model;

        public CoreManager getCore() {
            return core;
        }
	
	public PacketManager(PacketModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void process(byte[] bPacket) throws ClassNotFoundException, SQLException{	
		this.core.getLog().log(this,"New Packet arrived..processing");
		//maybe check integrity maybe after decoding...
		
		//Dirty but for debug
		//System.out.println("PlainPacket");
		//System.out.println(new String(bPacket));
		
		this.core.getLog().log(this,"Decrypting.....");
		
		PacketModel packet = new PacketModel();
		packet = (PacketModel)SerializationUtils.deserialize(this.core.getSecurity().decryptPacket(bPacket));
		if(packet != null){
			switch(packet.getType()){
			case GET:
				try{
					this.core.getLog().log(this,"Pass to RequestManager");
					this.core.getRequest().process((RequestModel)SerializationUtils.deserialize(packet.getContent()));
				} catch (SerializationException e){
					this.core.getLog().err(this,"Not a SerializedRequestModel type.");
				}
				
				break;
			case POST:
				this.core.getLog().log(this,"Pass to UnamedManager ;)");
				break;
			default:
				this.core.getLog().log(this,"Don't know this command abort..");
				//System.out.println("Packet Type :" + packet.getType().toString());
				//System.out.println("Stringed content: " + new String(packet.getContent()));
				//System.out.println("EOF flag: " + packet.isEof());	
				break;	
			}
		}else{
			this.core.getLog().warn(this,"There was an error on the packet" );
		}
		return;
	}	
	
	public PacketModel forge(String type, Object content){
		try{
			PacketModel packet = new PacketModel();
			packet.setType(Type.valueOf(type));
			if( content instanceof String)
				packet.setContent(((String) content).getBytes());
			else //assuming bytes..
				packet.setContent(SerializationUtils.serialize((Serializable) content));

			//post current to packet model in order to send ... Support 1 packet in temp.
			//normally not need when automation but usefull when forge the send.
			this.model.save(packet);
			return packet;
		}catch(IllegalArgumentException e){
			this.core.getLog().err(this,"args <"+ type.getClass().getName().toString() +" {"+ Arrays.toString(Type.values()) +"},"+content.getClass().getName().toString()+ ">");
		}
		return null;
	}
	
	public void send(String port){
		//grab temp packet
		PacketModel packet = this.model.getPacket();
		//Security
		byte[] bPacket = this.core.getSecurity().encryptPacket(SerializationUtils.serialize(packet));
		
		
		//construct socket on the fly.
		//TODO get frontal information
		
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(port));		
			socket.getOutputStream().write(bPacket);
			socket.close();
		}catch( ConnectException e) {
			this.core.getLog().err(this, "Connection refused");
		}
		catch (NumberFormatException | IOException e ) {
			e.printStackTrace();
		} 
	}
	
	public void sendPacket(PacketModel req, String port){
		this.model.save(req);
		this.send(port);
	}
        
        public void send(byte[] packet, String ip, int port){
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName(ip), port);		
			socket.getOutputStream().write(SerializationUtils.serialize(packet));
			socket.close();
		}catch( ConnectException e) {
			this.core.getLog().err(this, "Connection refused");
		}
		catch (NumberFormatException | IOException e ) {
			e.printStackTrace();
		} 
	}

        public void sendToCentralServer(byte [] packet){
            Socket socket;
		try {
                    socket = new Socket(InetAddress.getByName(this.getCore().getBroadcast().getServer().getIpDest()), this.getCore().getBroadcast().getServer().getPort());		
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