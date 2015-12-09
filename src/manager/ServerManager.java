package manager;

import java.io.IOException;

import model.ServerModel;

public class ServerManager {
	private ServerModel model = null;
	private CoreManager core = null;
	
	private Thread serverThread = null;
	private ServerHandler serverRunnable = null;
	
	public ServerManager(ServerModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public void start(){
		this.core.getLogManager().log(this,"Starting server handler");
		this.serverRunnable = new ServerHandler(this);
		Thread tThread = new Thread(this.serverRunnable);
		this.serverThread = tThread;
		this.serverThread.start();
		return;
	}
	
	public void stop() throws InterruptedException, IOException{
		this.core.getLogManager().log(this,"Stopping..");
		this.serverRunnable.stop();
		this.serverThread.join();
		this.serverThread = null;		
		return;
	}
	
	public ServerModel getModel(){
		return this.model;
	}
	public CoreManager getCore(){
		return this.core;
	}
	
	
	public boolean isRunning(){
		if (this.serverThread != null)
			return true;
		else
			return false;
	}
}
