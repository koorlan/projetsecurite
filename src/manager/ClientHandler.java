package manager;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import model.RequestModel;



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
			//RequestModel request;
			
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
			this.server.getCore().getRequestManager().process((RequestModel)SerializationUtils.deserialize(packet));		
			this.socket.close();
		
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
