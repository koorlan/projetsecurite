package model;

import java.util.ArrayList;

import manager.BroadcastManager;

public class BroadcastModel {
	private BroadcastManager manager;
        private ServerModel server;
	private ArrayList<FrontalModel> frontal;
	
	//TODO Maybe Change for a list of FrontalModel
	
	public void setManager(BroadcastManager manager){
		this.manager = manager;
	}

        public ServerModel getServer() {
            return server;
        }

        public void setServer(ServerModel server) {
            this.server = server;
        }
        
	public ArrayList<FrontalModel> getFrontal() {
		return frontal;
	}
	public void setFrontal(ArrayList<FrontalModel> frontal) {
		this.frontal = frontal;
	}
	
	public void addFrontal(FrontalModel frontal){
		this.frontal.add(frontal);
	}
}
