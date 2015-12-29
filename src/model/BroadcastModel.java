package model;

import java.util.ArrayList;

import manager.BroadcastManager;

public class BroadcastModel {
	private BroadcastManager manager;
	private ArrayList<FrontalModel> frontals = new ArrayList<FrontalModel>();
	
	//TODO Maybe Change for a list of FrontalModel
	
	
	public void setManager(BroadcastManager manager){
		this.manager = manager;
	}
	public ArrayList<FrontalModel> getFrontals() {
		return frontals;
	}
	public void setFrontals(ArrayList<FrontalModel> frontals) {
		this.frontals = frontals;
	}
	
	public void addFrontal(FrontalModel frontal){
		this.frontals.add(frontal);
	}
}
