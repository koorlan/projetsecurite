/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import main.Frontal;
import main.User;
import manager.FrontalManager;
import manager.ServerManager;

/**
 *
 * @author allali
 */
public class FrontalModel {
    private FrontalManager manager = null;
    private String familly;
    private String name;
    
    private int internalPort;
    private int externalPort;
    private String ip;
    
    private ServerManager internalserverManager = null;
    private ServerManager externalserverManager = null;
    private HashMap<Integer, Socket> frontalMap = new HashMap<Integer, Socket>();
    private HashMap<String, ArrayList<Frontal>> frontalFamillyMap = new HashMap<String, ArrayList<Frontal>>();
    private ArrayList<User> userList = new ArrayList<User>();
    
    public FrontalManager getManager() {
        return manager;
    }

    public void setManager(FrontalManager manager) {
        this.manager = manager;
    }

    public String getFamilly() {
        return familly;
    }

    public String getName() {
        return name;
    }

    public ServerManager getInternalserverManager() {
        return internalserverManager;
    }

    public void setInternalserverManager(ServerManager internalserverManager) {
        this.internalserverManager = internalserverManager;
    }

    public ServerManager getExternalserverManager() {
        return externalserverManager;
    }

    public void setExternalserverManager(ServerManager externalserverManager) {
        this.externalserverManager = externalserverManager;
    }

    public HashMap<Integer, Socket> getFrontalMap() {
        return frontalMap;
    }

    public void setFrontalMap(HashMap<Integer, Socket> frontalMap) {
        this.frontalMap = frontalMap;
    }


   
    
    public HashMap<String, ArrayList<Frontal>> getFrontalFamillyMap() {
		return frontalFamillyMap;
	}

	public void setFrontalFamillyMap(HashMap<String, ArrayList<Frontal>> frontalFamillyMap) {
		this.frontalFamillyMap = frontalFamillyMap;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public int getInternalPort() {
		return internalPort;
	}

	public void setInternalPort(int internalPort) {
		this.internalPort = internalPort;
	}

	public int getExternalPort() {
		return externalPort;
	}

	public void setExternalPort(int externalPort) {
		this.externalPort = externalPort;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setFamilly(String familly) {
		this.familly = familly;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FrontalModel(String familly, String name, ServerManager internalserverManager, ServerManager externalserverManager){
        this.familly = familly;
        this.name = name;
        this.internalserverManager = internalserverManager;
        this.externalserverManager = externalserverManager;
        
        this.internalPort = internalserverManager.getModel().getPort();
        this.externalPort = externalserverManager.getModel().getPort();
        
    }
}
