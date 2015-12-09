package manager;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
			this.reader = new ObjectInputStream(this.socket.getInputStream());
			while (true) {
				Object data = this.reader.readObject();
				if(data instanceof RequestModel){
					this.server.getCore().getRequestManager().process((RequestModel)data);
					if (((RequestModel) data).isEof())
						break;
				}
				else
					break;
			}
			this.reader.close();
			this.socket.close();
		
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
