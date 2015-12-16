package main;

import manager.*;
import model.*;

public class CentralServer {
	//Don't worry of erros.. need to push in order to work from anywhere <3
	private CoreManager core = null;
	
	public CentralServer(){
		super();
		this.initialize();
	}
	
	public void initialize(){
		CoreModel CoreM = new CoreModel();
		CoreManager Core = new CoreManager(CoreM);
		CoreM.setManager(Core);
		
		LogManager Log = new LogManager(Core);
		Core.setLog(Log);
		
		ServerModel ServerM = new ServerModel();
		ServerManager Server = new ServerManager(ServerM, Core);
		ServerM.setManager(Server);
		Core.setServer(Server);
		
		BroadcastModel BcastM = new BroadcastModel();
		BroadcastManager Bcast = new BroadcastManager(BcastM, Core);
		BcastM.setManager(Bcast);
		Core.setBroadcast(Bcast);
		
		this.core = Core;
		return;
	}
	
	public CoreManager getCore(){
		return this.core;
	}
}
