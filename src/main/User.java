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
		 CoreManager Core = new CoreManager(CoreM, "user");
		 CoreM.setManager(Core);
		 
		 TerminalModel TerminalM = new TerminalModel();
		 TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		 Core.set(Terminal);
		 TerminalM.setManager(Terminal);
		 
		 LogManager Log = new LogManager(Core);
		 Core.set(Log);
		 
		 UserModel UserM = new UserModel();
		 UserManager User = new UserManager(UserM, Core);
		 Core.set(User);
		 UserM.setManager(User);
		 
		 ServerModel ServerM = new ServerModel();
		 ServerManager Server = new ServerManager(ServerM, Core);
		 Core.set(Server);
		 ServerM.setManager(Server);
		 
		 PacketModel PacketM = new PacketModel();
		 PacketManager Packet = new PacketManager(PacketM, Core);
		 Core.set(Packet);
		 PacketM.setManager(Packet);
		 
		 SecurityModel SecurityM = new SecurityModel();
		 SecurityManager Security = new SecurityManager(SecurityM, Core);
		 Core.set(Security);
		 SecurityM.setManager(Security);
		 
		 RequestModel RequestM = new RequestModel();
		 RequestManager Request = new RequestManager(RequestM,Core);
		 Core.set(Request);
		 RequestM.setManager(Request);
		
		 DialogQueryManager dialog = new DialogQueryManager(Core);
		 Core.set(dialog);
		 
		 this.core = Core;
		return;
	}
	public CoreManager getCore(){
		return this.core;
	}

}
