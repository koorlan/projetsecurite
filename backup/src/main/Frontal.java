package main;

import java.util.ArrayList;
import java.util.HashMap;
import manager.CoreManager;
import manager.DialogQueryManager;
import manager.FrontalManager;
import manager.LogManager;
import manager.PacketManager;
import manager.RequestManager;
import manager.SecurityManager;
import manager.ServerManager;
import manager.TerminalManager;
import model.CoreModel;
import model.FrontalModel;
import model.PacketModel;
import model.RequestModel;
import model.SecurityModel;
import model.ServerModel;
import model.TerminalModel;
import model.UserModel;

public class Frontal {
	private CoreManager core = null;
	
	public Frontal(String familly, String name, String ip, int internalPort, int externalPort) throws Exception{
		super();
		this.initialize(familly, name, ip, internalPort, externalPort);
                
	}
	
	public void initialize(String familly, String name, String ip, int internalPort, int externalPort) throws Exception{
		 CoreModel CoreM = new CoreModel();
		 CoreManager Core = new CoreManager(CoreM, "internalFrontal");
		 CoreM.setManager(Core);
		 
		 TerminalModel TerminalM = new TerminalModel();
		 TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		 Core.set(Terminal);
		 TerminalM.setManager(Terminal);
		 
		 LogManager Log = new LogManager(Core);
		 Core.set(Log);
                 
                 ServerModel internalServerM = new ServerModel(ip, internalPort, 0);
		 ServerManager internalServer = new ServerManager(internalServerM, Core);
		 Core.set(internalServer);
		 internalServerM.setManager(internalServer);
		 
                 ServerModel externalServerM = new ServerModel(ip, externalPort, 0);
		 ServerManager externalServer = new ServerManager(externalServerM, Core);
		 Core.set(externalServer);
		 externalServerM.setManager(externalServer);
                 
		 FrontalModel frontalM = new FrontalModel(name, familly, internalServer, externalServer);
		 FrontalManager frontal = new FrontalManager(frontalM, Core);
		 Core.set(frontal);
		 frontalM.setManager(frontal);
		 
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
