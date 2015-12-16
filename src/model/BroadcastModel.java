package model;

import java.util.ArrayList;

import manager.BroadcastManager;

public class BroadcastModel {
	private BroadcastManager manager;
	private ArrayList<Front> fronts;
	
	//TODO Maybe Change for a list of FrontalModel
	
	
	public void setManager(BroadcastManager manager){
		this.manager = manager;
	}
	public ArrayList<Front> getFronts() {
		return fronts;
	}
	public void setFronts(ArrayList<Front> fronts) {
		this.fronts = fronts;
	}
	
	public void addFronts(Front front){
		this.fronts.add(front);
	}
}
