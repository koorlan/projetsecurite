package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import manager.CoreManager;
import manager.DBManager;
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

	public Frontal(String db) throws Exception {
		super();
		this.initialize(db);
	}

	public void initialize(String db) throws Exception{
		 CoreModel CoreM = new CoreModel();
		 CoreManager Core = new CoreManager(CoreM, "frontal");
		 CoreM.setManager(Core);
		 
		 TerminalModel TerminalM = new TerminalModel();
		 TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		 Core.set(Terminal);
		 TerminalM.setManager(Terminal);
		 
		 LogManager Log = new LogManager(Core);
		 Core.set(Log);
                 
         ServerModel internalServerM = new ServerModel();
		 ServerManager internalServer = new ServerManager(internalServerM, Core);
		 Core.set(internalServer);
		 internalServerM.setManager(internalServer);
		 
         ServerModel externalServerM = new ServerModel();
		 ServerManager externalServer = new ServerManager(externalServerM, Core);
		 Core.set(externalServer);
		 externalServerM.setManager(externalServer);
                 
		 FrontalModel frontalM = new FrontalModel("default", "default", internalServer, externalServer);
		 FrontalManager frontal = new FrontalManager(frontalM, Core);
		 Core.set(frontal);
		 frontalM.setManager(frontal);
		 frontalM.setInternalserverManager(internalServer);
		 frontalM.setExternalserverManager(externalServer);
		 
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
		 
		 DBManager DB = new DBManager(Core);
		 Core.set(DB);
		 
		 GeneralizerModel GeneralM = new GeneralizerModel();
		 GeneralizerManager General = new GeneralizerManager(GeneralM,Core);
		 Core.set(General);
		 //TODO setManager ??
		 
		 this.core = Core;
		 
		 this.fill(db);
		 
		 //Now set Information to Instanced servers 
		 internalServerM.setPort(frontalM.getInternalPort());
		 externalServerM.setPort(frontalM.getExternalPort());
		 String IpDest = this.core.getDB().getFrontalIP();
		 internalServerM.setIpDest(IpDest);
		 externalServerM.setIpDest(IpDest);

		 //register Users
		 frontalM.setUserList(this.core.getDB().getUsers());
		 
		 //register otherFronts
		 
		return;
	}

	public void fill(String db) throws ClassNotFoundException, SQLException {
		this.core.getDB().setDB_INFO(db);
		this.core.getDB().initialize();
	}

	public CoreManager getCore() {
		return this.core;
	}

}
