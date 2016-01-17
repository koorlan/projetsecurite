package main;

import java.sql.SQLException;

import anonymizer.DataHeaderManager;
import anonymizer.DataHeaderModel;
import crypto.CryptoUtilsManager;
import crypto.CryptoUtilsModel;
import dialog.DialogWindow;
import filter.FilterManager;
import filter.FilterModel;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import manager.CoreManager;
import manager.DBManager;
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
		
		 DialogWindow dialog = new DialogWindow(Core);
		 Core.set(dialog);
		 
		 DBManager db = new DBManager(Core);
		 Core.set(db);
		 
		 GeneralizerModel GeneralM = new GeneralizerModel();
		 GeneralizerManager General = new GeneralizerManager(GeneralM, Core);
		 Core.set(General);
		 
		 CryptoUtilsModel CryptoM = new CryptoUtilsModel();
		 CryptoUtilsManager	Crypto = new CryptoUtilsManager(CryptoM, Core);
		 // TODO see later
		 Core.set(Crypto);
		 
		 DataHeaderModel DataHeaderM = new DataHeaderModel();
		 DataHeaderManager DataHeader = new DataHeaderManager(DataHeaderM, Core);
		 Core.set(DataHeader);
		
		 FilterModel FilterM = new FilterModel();
		 FilterManager Filter = new FilterManager(FilterM,Core);
		 Core.set(Filter);
		 
		 this.core = Core;
		return;
	}
	public CoreManager getCore(){
		return this.core;
	}

	public void fill(String dbSec, String dbData) throws ClassNotFoundException, SQLException{
		/**Set secured DB**/
		this.core.getDB().setDB_INFO(dbSec);
		/**Set data DB **/
		this.core.getDB().setDB_DATA(dbData);
		this.core.getDB().initialize();
	}
}
