package main;

import manager.*;
import model.*;

public class CentralServer {
	//Don't worry of erros.. need to push in order to work from anywhere <3
	private CoreManager core = null;
	
	public CentralServer(){
		super();
		this.initialize();
		
		//populate with fronts
		this.core.getBroadcast().addFront("frontal1","127.0.0.1","5555");
		this.core.getBroadcast().addFront("frontal2","127.0.0.1","5556");
		this.core.getBroadcast().addFront("frontal3","127.0.0.1","5557");
		this.core.getBroadcast().addFront("frontal4","127.0.0.1","5558");
		this.core.getBroadcast().addFront("frontal5","127.0.0.1","5559");
		
	}
	
	public void initialize(){
		CoreModel CoreM = new CoreModel();
		CoreManager Core = new CoreManager(CoreM, "central");
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
		
		TerminalModel TerminalM = new TerminalModel();
		TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		Core.setTerminal(Terminal);
		TerminalM.setManager(Terminal);
		
		this.core = Core;
		return;
	}
	
	public CoreManager getCore(){
		return this.core;
	}
}
