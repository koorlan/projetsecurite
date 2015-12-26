/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import model.FrontalModel;
import model.UserModel;

/**
 *
 * @author allali
 */
public class FrontalManager {
    private FrontalModel model = null;
    private CoreManager core = null;
    
    
    public String getFamilly() {
        return this.model.getFamilly();
    }
    
    public String getName() {
        return this.model.getName();
    }
    
    public ServerManager getInternalserverManager() {
        return this.model.getInternalserverManager();
    }

    public void setInternalserverManager(ServerManager internalserverManager) {
        this.model.setInternalserverManager(internalserverManager);
    }

    public ServerManager getExternalserverManager() {
        return this.model.getExternalserverManager();
    }

    public void setExternalserverManager(ServerManager externalserverManager) {
        this.model.setExternalserverManager(externalserverManager);
    }

    public HashMap<Integer, Socket> getFrontalMap() {
        return this.model.getFrontalMap();
    }

    public void setFrontalMap(HashMap<Integer, Socket> frontalMap) {
        this.model.setFrontalMap(frontalMap);
    }

    public HashMap<String, ArrayList<FrontalModel>> getFrontalFamillyMap() {
        return this.model.getFrontalFamillyMap();
    }

    public void setFrontalFamillyMap(HashMap<String, ArrayList<FrontalModel>> frontalFamillyMap) {
        this.model.setFrontalFamillyMap(frontalFamillyMap);
    }

    public ArrayList<UserModel> getUserList() {
        return this.model.getUserList();
    }

    public void setUserList(ArrayList<UserModel> userList) {
        this.model.setUserList(userList);
    }

    public FrontalModel getModel() {
        return model;
    }

    public CoreManager getCore() {
        return core;
    }
    
    public FrontalManager(FrontalModel model, CoreManager core) {
        this.model = model;
        this.core = core;
    }
}
