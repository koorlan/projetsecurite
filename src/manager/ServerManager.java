package manager;

import model.ServerModel;

public class ServerManager {
	private ServerModel model = null;
	private CoreManager core = null;
	
	private Thread serverThread = null;
	
	public ServerManager(ServerModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public void start(){
		this.core.getLogManager().log("ServerManager Starting thread");
		Thread tThread = new Thread(new ServerHandler(this));
		this.serverThread = tThread;
		this.serverThread.start();
		return;
	}
	
	public ServerModel getModel(){
		return this.model;
	}
	
	public boolean isThreadRunning(){
		return (!(this.serverThread == null));
	}
	
	public boolean isRunning(){
		return this.isThreadRunning();
	}
}
