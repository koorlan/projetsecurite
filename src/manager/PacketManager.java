package manager;

import model.PacketModel.*;
import model.RequestModel;
import model.FrontalModel;
import model.PacketModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import main.Frontal;
import main.User;

public class PacketManager {
	private CoreManager core;
	private PacketModel model;

	public PacketManager(PacketModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}

	public synchronized byte[] processFrontal(byte[] bPacket, String mode, Socket socket)
			throws IOException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
		// Request -> Answer
		// GI->GE->PI->PE
		ArrayList<String> results;
		RequestModel request ;
		
		PacketModel packet = new PacketModel();
		packet = (PacketModel) SerializationUtils.deserialize(bPacket);
		switch (packet.getType()) {
		case GET:
			switch (mode) {
			case "EXTERNAL":
				this.core.getLog().log(this, "packet GET + EXTERNAL arrived");
				for (User user : this.core.getFrontal().getUserList()) {
					String ip = user.getCore().getServer().getModel().getIpDest();
					int port = user.getCore().getServer().getModel().getPort();
					this.core.getPacket().sendPacket(packet, ip, port);
				}

				//results = new ArrayList<String>();
				
				request = (RequestModel) SerializationUtils.deserialize(packet.getContent());
				
				//this.core.getDB().build(request.getDu()); 
				//	if (this.core.getDB().isFormatted()) {
				//	results = this.core.getDB().search();
				//	RequestModel response = this.core.getRequest().forgeResponse(results);
					
				RequestModel response = this.core.getRequest().process(request);
				if(response != null)
				{
					this.core.getLog().log(this, "New external response forged");
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					PacketModel toSendP = this.forge("POST", response);
					toSendP.setId(packet.getId());
					toSendP.setSenderFamilly(packet.getSenderFamilly());
					//byte[] toSend = SerializationUtils.serialize(toSendP);
					//Send my results to frontal familly
					ArrayList<Frontal> frontalList = this.core.getFrontal().getFrontalFamillyMap()
							.get(packet.getSenderFamilly());
					for (Frontal frontal : frontalList)
						this.sendPacket(toSendP, frontal.getCore().getFrontal().getExternalserverManager().getModel().getIpDest(),
								frontal.getCore().getFrontal().getExternalserverManager().getModel().getPort());
					
					return null;
				} 
				else
					this.core.getLog().err(this, "Non formatted content");
				
				break;
			case "INTERNAL":
				this.core.getLog().log(this, "packet GET + INTERNAL arrived");
				packet.setSenderFamilly(this.core.getFrontal().getFamilly());
				int random;
				do {
					Random r = new Random();
					random = r.nextInt(1000);
				} while (this.core.getFrontal().getFrontalMap().containsKey(random));
				this.core.getFrontal().getFrontalMap().put(random, socket);
				byte[] id = this.core.getSecurity().sha1(this.core.getFrontal().getName() + random);
				packet.setId(id);
				this.core.getPacket().sendPacket(packet, this.core.getDB().getCentralIP(),this.core.getDB().getCentralPort());
				
				request = (RequestModel) SerializationUtils.deserialize(packet.getContent());
				RequestModel intResponse = this.core.getRequest().process(request);
				if (intResponse != null) 
				{	
					this.core.getLog().log(this, "New internal response forged");
					//results = this.core.getDB().search();
					//TODO ... if many results -> do a for to send X responses
					//RequestModel intResponse = this.core.getRequest().forgeResponse(results);
					
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					byte[] toSend = SerializationUtils.serialize(this.forge("POST", intResponse));
					dos.writeInt(toSend.length);
					dos.write(toSend);
					ArrayList<Frontal> frontalList = this.core.getFrontal().getFrontalFamillyMap()
							.get(packet.getSenderFamilly());
					for (Frontal frontal : frontalList)
						this.sendPacket(packet, frontal.getCore().getFrontal().getExternalserverManager().getModel().getIpDest(),
								frontal.getCore().getFrontal().getExternalserverManager().getModel().getPort());
					return null;
				} else
					this.core.getLog().err(this, "Non formatted content");				
				break;
			default:
				break;
			}
			break;
		case POST:
			switch (mode) {
			case "EXTERNAL":
				this.core.getLog().log(this, "packet POST + EXTERNAL arrived");
				for (Entry<Integer, Socket> entry : this.core.getFrontal().getFrontalMap().entrySet()) {
					Integer key = entry.getKey();
					if (Arrays.equals(packet.getId(),
							this.core.getSecurity().sha1(this.core.getFrontal().getName() + key))) {
						Socket s = entry.getValue();
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						byte[] toSend = SerializationUtils.serialize(packet);
						dos.writeInt(toSend.length);
						dos.write(toSend);
						//s.close();
						break;
					}
				}
				break;
			case "INTERNAL":
				this.core.getLog().log(this, "packet POST + INTERNAL arrived");
				ArrayList<Frontal> frontalList = this.core.getFrontal().getFrontalFamillyMap()
						.get(packet.getSenderFamilly());
				for (Frontal frontal : frontalList)
					this.sendPacket(packet, frontal.getCore().getFrontal().getExternalserverManager().getModel().getIpDest(),
							frontal.getCore().getFrontal().getExternalserverManager().getModel().getPort());
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return null;
	};

	public synchronized byte[] processCentral(byte[] bPacket) {
		this.core.getBroadcast().broadcast(bPacket);
		return null;
	};

	public synchronized byte[] processUser(byte[] bPacket) throws ClassNotFoundException, SQLException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		PacketModel packet = new PacketModel();
		packet = (PacketModel) SerializationUtils.deserialize(bPacket);
		if (packet != null) {
			switch (packet.getType()) {
			case GET:
				try {
					this.core.getLog().log(this, "Pass to RequestManager");
					//TODO : check this 
					packet.setContent(SerializationUtils.serialize(this.core.getRequest().process((RequestModel) SerializationUtils.deserialize(packet.getContent()))));
					return SerializationUtils.serialize(packet);
				} catch (SerializationException e) {
					this.core.getLog().err(this, "Not a SerializedRequestModel type.");
				}

				break;
			case POST:
				try {
					this.core.getLog().log(this, "Pass to ResponseManager ;)");
					//TODO : <?> 
					this.core.getRequest()
							.processResponse((RequestModel) SerializationUtils.deserialize(packet.getContent()));
				} catch (SerializationException e) {
					this.core.getLog().err(this, "Not a SerializedRequestModel type.");
				}

				break;
			default:
				this.core.getLog().log(this, "Don't know this command abort..");
				break;
			}
		} else {
			this.core.getLog().warn(this, "There was an error on the packet");
		}
		return null;
	}

	public PacketModel forge(String type, Object content) {
		try {
			PacketModel packet = new PacketModel();
			packet.setType(Type.valueOf(type));
			if (content instanceof String)
				packet.setContent(((String) content).getBytes());
			else // assuming bytes..
				packet.setContent(SerializationUtils.serialize((Serializable) content));

			// post current to packet model in order to send ... Support 1
			// packet in temp.
			// normally not need when automation but usefull when forge the
			// send.
			this.model.save(packet);
			return packet;
		} catch (IllegalArgumentException e) {
			this.core.getLog().err(this, "args <" + type.getClass().getName().toString() + " {"
					+ Arrays.toString(Type.values()) + "}," + content.getClass().getName().toString() + ">");
		}
		return null;
	}

	public Socket sendTo(String ip, int port) {
		// grab temp packet
		PacketModel packet = this.model.getPacket();
		// Security
		// = this.core.getSecurity().encryptPacket(

		byte[] bPacket = SerializationUtils.serialize(packet);

		// construct socket on the fly.
		// TODO get frontal information

		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(bPacket.length);
			dos.write(bPacket);
			return socket;
			//socket.close();
		} catch (ConnectException e) {
			this.core.getLog().err(this, "Connection refused @" + ip + ":" + port);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Socket sendPacket(PacketModel req, String ip, int port) {
		this.model.save(req);
		return this.sendTo(ip, port);
	}

	
}
