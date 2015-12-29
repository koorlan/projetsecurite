package manager;

import java.io.IOException;

import model.ServerModel;

public class ServerManager {
	private ServerModel model = null;
	private CoreManager core = null;

	private Thread serverThread = null;
	private ServerHandler serverRunnable = null;

	public ServerManager(ServerModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}

	public void start() {
		this.core.getLog().log(this, "Starting server handler");
		this.serverRunnable = new ServerHandler(this);
		Thread tThread = new Thread(this.serverRunnable);
		this.serverThread = tThread;
		this.serverThread.start();
		return;
	}

	public void stop() throws InterruptedException, IOException {
		this.core.getLog().log(this, "Stopping..");
		this.serverRunnable.stop();
		this.serverThread.join();
		this.serverThread = null;
		return;
	}

	public ServerModel getModel() {
		return this.model;
	}

	public CoreManager getCore() {
		return this.core;
	}

	public boolean isRunning() {
		if (this.serverThread != null)
			return true;
		else
			return false;
	}

	public void setIp(String ip) {
		this.model.setIpDest(ip);
		this.core.getLog().log(this, "has changed his addr to : "+ip);
	}

	public void setPort(int port) {
		this.model.setPort(port);
		this.core.getLog().log(this, "has changed his port to : "+port);
	}
}
