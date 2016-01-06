package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import anonymizer.DataHeaderManager;
import anonymizer.DataHeaderModel;
import crypto.CryptoUtilsManager;
import crypto.CryptoUtilsModel;
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

	CoreModel CoreM = new CoreModel();
	CoreManager Core = new CoreManager(CoreM, "frontal");
	TerminalModel TerminalM = new TerminalModel();
	TerminalManager Terminal = new TerminalManager(TerminalM, Core);
	LogManager Log = new LogManager(Core);
	ServerModel internalServerM = new ServerModel();
	ServerManager internalServer = new ServerManager(internalServerM, Core);
	ServerModel externalServerM = new ServerModel();
	ServerManager externalServer = new ServerManager(externalServerM, Core);
	FrontalModel frontalM = new FrontalModel("default", "default", internalServer, externalServer);
	FrontalManager frontal = new FrontalManager(frontalM, Core);
	PacketModel PacketM = new PacketModel();
	PacketManager Packet = new PacketManager(PacketM, Core);
	SecurityModel SecurityM = new SecurityModel();
	SecurityManager Security = new SecurityManager(SecurityM, Core);
	RequestModel RequestM = new RequestModel();
	RequestManager Request = new RequestManager(RequestM, Core);
	DBManager DB = new DBManager(Core);
	GeneralizerModel GeneralM = new GeneralizerModel();
	GeneralizerManager General = new GeneralizerManager(GeneralM, Core);
	
	CryptoUtilsModel CryptoM = new CryptoUtilsModel();
	CryptoUtilsManager	Crypto = new CryptoUtilsManager(CryptoM, Core);
	DataHeaderModel DataHeaderM = new DataHeaderModel();
	DataHeaderManager DataHeader = new DataHeaderManager(DataHeaderM, Core);
	
	public Frontal() throws Exception {
		super();
		this.initialize();
	}

	public void initialize() throws Exception {

		CoreM.setManager(Core);

		Core.set(Terminal);
		TerminalM.setManager(Terminal);

		Core.set(Log);

		Core.set(internalServer);
		internalServerM.setManager(internalServer);

		Core.set(externalServer);
		externalServerM.setManager(externalServer);

		Core.set(frontal);
		frontalM.setManager(frontal);
		frontalM.setInternalserverManager(internalServer);
		frontalM.setExternalserverManager(externalServer);

		Core.set(Packet);
		PacketM.setManager(Packet);

		Core.set(Security);
		SecurityM.setManager(Security);

		Core.set(Request);
		RequestM.setManager(Request);

// ADD
		Core.set(Crypto);
		Core.set(DataHeader);
// ENDOF 

		Core.set(DB);

		Core.set(General);
		// TODO setManager ??

		this.core = Core;

		return;
	}

	public void fill(String db) throws Exception {
		this.core.getDB().setDB_INFO(db);
		this.core.getDB().initialize();
		this.populate();
	}

	public void populate() throws Exception {
		// Now set Information to Instanced servers
		internalServerM.setPort(frontalM.getInternalPort());
		externalServerM.setPort(frontalM.getExternalPort());
		String IpDest = this.core.getDB().getFrontalIP();
		internalServerM.setIpDest(IpDest);
		externalServerM.setIpDest(IpDest);

		// register Users
		frontalM.setUserList(this.core.getDB().getUsers());

		// register otherFronts
		ArrayList<Frontal> otherFrontals = this.core.getDB().getFrontals();
		//order by familly name.
		HashMap<String, ArrayList<Frontal>> map = new HashMap<String, ArrayList<Frontal>>();
		for (Frontal frontal: otherFrontals){
			if(map.containsKey(frontal.core.getFrontal().getFamilly())){
				map.get(frontal.core.getFrontal().getFamilly()).add(frontal);
			}else{
				ArrayList<Frontal> newl = new ArrayList<Frontal>();
				newl.add(frontal);
				map.put(frontal.core.getFrontal().getFamilly(), newl );
			}
		}
		frontalM.setFrontalFamillyMap(map);
		//frontalM.getFrontalFamillyMap().put(arg0, arg1)
	}

	public CoreManager getCore() {
		return this.core;
	}

}
