package manager;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import static java.lang.Math.random;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FrontalModel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import model.PacketModel;
import model.UserModel;



public class ClientHandler  implements Runnable{
	private ServerHandler server;
	private Socket socket;
	ObjectInputStream reader ;  
	
	public ClientHandler(ServerHandler server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			
			 InputStream inputStream = socket.getInputStream();
		        int count;
		        byte[] packet = new byte[0];
		        byte[] buffer = new byte[64];

		        //TODO: rewrite
		        while (true) {
		            count = inputStream.read(buffer);
		            packet = ArrayUtils.addAll(packet, buffer);
		            if (count != buffer.length) {
		                break;
		            }
		        }
		     
		     switch (this.server.getCore().getService()){
                            case "user":
                                       this.server.getCore().getPacket().process(packet);	
                               break;
                            case "central":
                                this.server.getCore().getBroadcast().broadcast(packet);
                               break;
                            case "internalFrontal":
                                this.server.getCore().setService("externalFrontal");
                                PacketModel packetModel = (PacketModel) SerializationUtils.deserialize(packet);
                                switch (packetModel.getType()){
                                    case GET:
                                        packetModel.setSenderFamilly(server.getCore().getFrontal().getFamilly());
                                        int random;
                                        do {
                                            Random r = new Random();
                                            random = r.nextInt(1000); 
                                        } while(server.getCore().getFrontal().getFrontalMap().containsKey(random));
                                        server.getCore().getFrontal().getFrontalMap().put(random, socket);
                                        byte[] id = server.getCore().getSecurity().sha1(server.getCore().getFrontal().getName() + random);
                                        packetModel.setId(id);
                                        this.server.getCore().getPacket().sendToCentralServer(SerializationUtils.serialize(packetModel));	
                                        break;
                                    
                                    case POST:
                                        ArrayList<FrontalModel> frontalList = server.getCore().getFrontal().getFrontalFamillyMap().get(packetModel.getSenderFamilly());
                                        for(FrontalModel frontal : frontalList)
                                            this.server.getCore().getPacket().send(SerializationUtils.serialize(packetModel), frontal.getExternalserverManager().getModel().getIpDest(), frontal.getExternalserverManager().getModel().getPort());	
                                        break;
                                }
                            break;
                            case "externalFrontal":
                                this.server.getCore().setService("internalFrontal");
                                packetModel = (PacketModel) SerializationUtils.deserialize(packet);
                                switch (packetModel.getType()){
                                    case GET:
                                        ArrayList<UserModel> userList = server.getCore().getFrontal().getUserList();
                                        for(UserModel user : userList){
                                            String ip = user.getManager().getCore().getSever().getIpDest();
                                            int port = user.getManager().getCore().getSever().getPort();
                                            this.server.getCore().getPacket().send(SerializationUtils.serialize(packetModel), ip, port);
                                        }                                       
                                        break;
                                    case POST:
                                        for(Entry<Integer, Socket> entry : server.getCore().getFrontal().getFrontalMap().entrySet()){
                                            Integer key = entry.getKey();
                                            if(Arrays.equals(packetModel.getId(), server.getCore().getSecurity().sha1(server.getCore().getFrontal().getName() + key))){
                                                Socket s = entry.getValue();
                                                s.getOutputStream().write(SerializationUtils.serialize(packetModel));
                                                break;
                                            }
                                                
                                        }
                                        break;
                                }
                            break;
                            default:
                                break;     
		     }	
			this.socket.close();
		
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

}
