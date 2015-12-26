package model;

import manager.ServerManager;

public class ServerModel {
	private ServerManager manager = null;
	
    private String ipDest = "127.0.0.1";
    private int port = 5555;
    private int maxConnexion = 0;
	
	public void setManager(ServerManager manager){
		this.manager = manager;
	}

	public String getIpDest() {
		return ipDest;
	}

	public void setIpDest(String ipDest) {
		this.ipDest = ipDest;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxConnexion() {
		return maxConnexion;
	}

	public void setMaxConnexion(int maxConnexion) {
		this.maxConnexion = maxConnexion;
	}
	
	public ServerModel(){
            
        }
        
        public ServerModel(String ipDest, int port, int maxConnexion){
            this.ipDest = ipDest;
            this.port = port;
            this.maxConnexion = maxConnexion;
        }
}
