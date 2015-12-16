package model;

import java.util.ArrayList;

import manager.BroadCastManager;

final class Front{
	private String name;
	private String ip;
	private int port;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}

public class BroadCastModel {
	private BroadCastManager manager;
	private ArrayList<Front> fronts;
	
	//TODO Maybe Change for a list of FrontalModel
	
	
	public void setManager(BroadCastManager manager){
		this.manager = manager;
	}
}
