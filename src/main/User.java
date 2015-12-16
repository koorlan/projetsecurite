package main;

import manager.CoreManager;
import manager.DialogQueryManager;
import manager.LogManager;
import manager.PacketManager;
import manager.RequestManager;
import manager.SecurityManager;
import manager.ServerManager;
import manager.TerminalManager;
import manager.UserManager;
import model.CoreModel;
import model.PacketModel;
import model.RequestModel;
import model.SecurityModel;
import model.ServerModel;
import model.TerminalModel;
import model.UserModel;

public class User {
	private CoreManager core = null;
	
	public User() throws Exception{
		super();
		this.initialize();
	}
	
	public void initialize() throws Exception{
		 CoreModel CoreM = new CoreModel();
		 CoreManager Core = new CoreManager(CoreM);
		 CoreM.setManager(Core);
		 
		 TerminalModel TerminalM = new TerminalModel();
		 TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		 Core.setTerminal(Terminal);
		 TerminalM.setManager(Terminal);
		 
		 LogManager Log = new LogManager(Core);
		 Core.setLog(Log);
		 
		 UserModel UserM = new UserModel();
		 UserManager User = new UserManager(UserM, Core);
		 Core.setUser(User);
		 UserM.setManager(User);
		 
		 ServerModel ServerM = new ServerModel();
		 ServerManager Server = new ServerManager(ServerM, Core);
		 Core.setServer(Server);
		 ServerM.setManager(Server);
		 
		 PacketModel PacketM = new PacketModel();
		 PacketManager Packet = new PacketManager(PacketM, Core);
		 Core.setPacket(Packet);
		 PacketM.setManager(Packet);
		 
		 SecurityModel SecurityM = new SecurityModel();
		 SecurityManager Security = new SecurityManager(SecurityM, Core);
		 Core.setSecurity(Security);
		 SecurityM.setManager(Security);
		 
		 RequestModel RequestM = new RequestModel();
		 RequestManager Request = new RequestManager(RequestM,Core);
		 Core.setRequest(Request);
		 RequestM.setManager(Request);
		
		 DialogQueryManager dialog = new DialogQueryManager(Core);
		 Core.setDialog(dialog);
		 
		 this.core = Core;
		return;
	}
	public CoreManager getCore(){
		return this.core;
	}

}
