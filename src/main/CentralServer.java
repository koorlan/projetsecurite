package main;

import java.sql.SQLException;

import manager.*;
import model.*;

public class CentralServer {
	// Don't worry of erros.. need to push in order to work from anywhere <3
	private CoreManager core = null;

	public CentralServer(String db) throws ClassNotFoundException, SQLException {
		super();
		this.initialize();
		this.fill(db);
	}

	public void initialize() {
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

		PacketModel PacketM = new PacketModel();
		PacketManager Packet = new PacketManager(PacketM, Core);
		Core.set(Packet);
		PacketM.setManager(Packet);

		TerminalModel TerminalM = new TerminalModel();
		TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		Core.set(Terminal);
		TerminalM.setManager(Terminal);

		DBManager db = new DBManager(Core);
		Core.set(db);

		this.core = Core;
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
