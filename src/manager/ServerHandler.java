package manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerHandler implements Runnable{

	private ServerManager manager = null;
	private boolean running = true;
	
	private ServerSocket socket = null;
	
	public ServerHandler(ServerManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void run() {
		
		int max = (this.manager.getModel().getMaxConnexion() !=0) ? this.manager.getModel().getMaxConnexion() : 10 ;
		String ip = (this.manager.getModel().getIpDest() != null) ? this.manager.getModel().getIpDest() : "127.0.0.1" ;
		try {
			this.socket = new ServerSocket(this.manager.getModel().getPort(),max, InetAddress.getByName(ip));
			this.manager.getCore().getLogManager().log(this,"started on " + socket.getInetAddress() + ":" +socket.getLocalPort() +" max: " + max);
			while(this.running){
					try{
					Socket sclient = this.socket.accept();
					ClientHandler client = new ClientHandler(this, sclient);
					Thread tclient = new Thread(client);
					tclient.start();
					} catch(SocketException e){
						this.manager.getCore().getLogManager().warn(this,"socket closed");				
						continue;
					}
			}
			this.manager.getCore().getLogManager().log(this,"stopped");
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	
	public void stop() throws IOException{
		this.running = false;
		//Stop accept incomming connection throw SocketException
		this.socket.close();
		//Todo close all socket connected...
	}
	
	public CoreManager getCore(){
		return this.manager.getCore();
	}
}
