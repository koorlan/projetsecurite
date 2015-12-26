package main;

import java.util.ArrayList;
import java.util.HashMap;
import manager.*;
import model.*;

public class App {
	 public static void main(String[] args) throws Exception  {
                User user1 = new User();
                user1.getCore().getUser().setName("alice");
                user1.getCore().start();
                
		User user2 = new User();
                user2.getCore().getUser().setName("bob");
		user2.getCore().start(); 
                
                User user3 = new User();
                user3.getCore().getUser().setName("luc");
		user3.getCore().start();
                
                User user4 = new User();
                user4.getCore().getUser().setName("dupont");
		user4.getCore().start();
                
                User user5 = new User();
                user5.getCore().getUser().setName("duront");
		user5.getCore().start();
                
                HashMap<String, ArrayList<FrontalModel>> frontalFamillyMap = new HashMap<String, ArrayList<FrontalModel>>();
                ArrayList<FrontalModel> frontalList = new ArrayList<FrontalModel>();
                
                Frontal frontal1 = new Frontal("3ABC", "3ABCA", "127.0.0.1", 55555, 55556);
                //adding users to frontals
                frontal1.getCore().getFrontal().getUserList().add(user1.getCore().getUser().getModel());
                frontal1.getCore().getFrontal().getUserList().add(user2.getCore().getUser().getModel());
                //adding the frontal on this list to inisilize the hashMap familly 
                frontalList.add(frontal1.getCore().getFrontal().getModel());
                Frontal frontal2 = new Frontal("3ABC", "3ABCB", "127.0.0.1", 55557, 55558);
                frontal2.getCore().getFrontal().getUserList().add(user3.getCore().getUser().getModel());
                frontalList.add(frontal2.getCore().getFrontal().getModel());
                
                frontalFamillyMap.put("3ABC", frontalList);
                frontalList.clear();
                
                Frontal frontal3 = new Frontal("3DEF", "3DEFA", "127.0.0.1", 55559, 55560);
                frontal3.getCore().getFrontal().getUserList().add(user4.getCore().getUser().getModel());
                frontalList.add(frontal3.getCore().getFrontal().getModel());
                Frontal frontal4 = new Frontal("3DEF", "3DEFB", "127.0.0.1", 55561, 55562);
                frontal4.getCore().getFrontal().getUserList().add(user5.getCore().getUser().getModel());
                frontalList.add(frontal4.getCore().getFrontal().getModel());
                
                frontalFamillyMap.put("3DEF", frontalList);
                frontalList.clear();
                
                frontal1.getCore().getFrontal().setFrontalFamillyMap(frontalFamillyMap);
                frontal2.getCore().getFrontal().setFrontalFamillyMap(frontalFamillyMap);
                frontal3.getCore().getFrontal().setFrontalFamillyMap(frontalFamillyMap);
                frontal4.getCore().getFrontal().setFrontalFamillyMap(frontalFamillyMap);
                
                frontal1.getCore().start();
                frontal2.getCore().start();
                frontal3.getCore().start();
                frontal4.getCore().start();
		
                
                CentralServer server = new CentralServer();
		server.getCore().getBroadcast().getModel().getFrontal().add(frontal1.getCore().getFrontal().getModel());
                server.getCore().getBroadcast().getModel().getFrontal().add(frontal2.getCore().getFrontal().getModel());
                server.getCore().getBroadcast().getModel().getFrontal().add(frontal3.getCore().getFrontal().getModel());
                server.getCore().getBroadcast().getModel().getFrontal().add(frontal4.getCore().getFrontal().getModel());
                server.getCore().start();
		 
	 }
}
