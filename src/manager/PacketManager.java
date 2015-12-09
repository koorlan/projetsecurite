package manager;

import model.PacketModel.*;
import model.PacketModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;



public class PacketManager {
	private CoreManager core;
	private PacketModel model;
	
	public PacketManager(PacketModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void process(byte[] bPacket){	
		this.core.getLogManager().log(this,"New Packet arrived..processing");
		//maybe check integrity maybe after decoding...
		
		//Dirty way but for debug
		System.out.println("PlainPacket");
		System.out.println(new String(bPacket));
		
		System.out.println("DECRYPTING.....");
		
		PacketModel packet = new PacketModel();
		packet = (PacketModel)SerializationUtils.deserialize(this.core.getSecurityManager().decryptPacket(bPacket));
		if(packet != null){
			System.out.println("Packet Type :" + packet.getType().toString());
			System.out.println("Stringed content: " + new String(packet.getContent()));
			System.out.println("EOF flag: " + packet.isEof());	
		}else{
			this.core.getLogManager().warn(this,"There was an error on the packet" );
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
				packet.setContent((byte[]) content);

			//post current to packet model in order to send ... Support 1 packet in temp.
			//normally not need when automation but usefull when forge the send.
			this.model.save(packet);
			return packet;
		}catch(IllegalArgumentException e){
			this.core.getLogManager().err(this,"args <"+ type.getClass().getName().toString() +" {"+ Arrays.toString(Type.values()) +"},"+content.getClass().getName().toString()+ ">");
		}
		return null;
	}
	
	public void send(String port){
		//grab temp packet
		PacketModel packet = this.model.getPacket();
		//Security
		byte[] bPacket = this.core.getSecurityManager().encryptPacket(SerializationUtils.serialize(packet));
		
		
		//construct socket on the fly.
		//TODO get frontal information
		
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(port));		
			socket.getOutputStream().write(bPacket);
			socket.close();
		}catch( ConnectException e) {
			this.core.getLogManager().err(this, "Connection refused");
		}
		catch (NumberFormatException | IOException e ) {
			e.printStackTrace();
		} 
	}
	
	public void sendPacket(PacketModel req, String port){
		this.model.save(req);
		this.send(port);
	}

}
