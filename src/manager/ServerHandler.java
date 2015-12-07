package manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable{

	private ServerManager manager = null;
	
	private ServerSocket socket = null;
	
	public ServerHandler(ServerManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void run() {
		int max = (this.manager.getModel().getMaxConnexion() !=0) ? this.manager.getModel().getMaxConnexion() : 10 ;
		String ip = (this.manager.getModel().getIpDest() != null) ? this.manager.getModel().getIpDest() : "0.0.0.0" ;
		try {
			this.socket = new ServerSocket(this.manager.getModel().getPort(),max, InetAddress.getByName(ip));
			while(this.manager.isThreadRunning()){
					Socket client = this.socket.accept();
			
			}		
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}

}
