/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ServerManager internalserverManager = null;
    private ServerManager externalserverManager = null;
    private HashMap<Integer, Socket> frontalMap = new HashMap<Integer, Socket>();
    private HashMap<String, ArrayList<FrontalModel>> frontalFamillyMap = new HashMap<String, ArrayList<FrontalModel>>();
    private ArrayList<UserModel> userList = new ArrayList<UserModel>();
    
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

    public HashMap<String, ArrayList<FrontalModel>> getFrontalFamillyMap() {
        return frontalFamillyMap;
    }

    public void setFrontalFamillyMap(HashMap<String, ArrayList<FrontalModel>> frontalFamillyMap) {
        this.frontalFamillyMap = frontalFamillyMap;
    }

    public ArrayList<UserModel> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserModel> userList) {
        this.userList = userList;
    }
    
    public FrontalModel(String familly, String name, ServerManager internalserverManager, ServerManager externalserverManager){
        this.familly = familly;
        this.name = name;
        this.internalserverManager = internalserverManager;
        this.externalserverManager = externalserverManager;
    }
}
