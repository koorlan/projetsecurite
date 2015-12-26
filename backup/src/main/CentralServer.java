package main;

import manager.*;
import model.*;

public class CentralServer {
	//Don't worry of erros.. need to push in order to work from anywhere <3
	private CoreManager core = null;
	
	public CentralServer() throws Exception{
		super();
		this.initialize();
                
                //populate with fronts
		
	}
	
	public void initialize(){
		CoreModel CoreM = new CoreModel();
		CoreManager Core = new CoreManager(CoreM, "central");
		CoreM.setManager(Core);
		
		LogManager Log = new LogManager(Core);
		Core.set(Log);
		
		ServerModel ServerM = new ServerModel();
		ServerManager Server = new ServerManager(ServerM, Core);
		ServerM.setManager(Server);
		Core.set(Server);
		
		BroadcastModel BcastM = new BroadcastModel();
		BroadcastManager Bcast = new BroadcastManager(BcastM, Core);
		BcastM.setManager(Bcast);
		Core.set(Bcast);
		
		TerminalModel TerminalM = new TerminalModel();
		TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		Core.set(Terminal);
		TerminalM.setManager(Terminal);
		
		this.core = Core;
		return;
	}
	
	public CoreManager getCore(){
		return this.core;
	}
}
