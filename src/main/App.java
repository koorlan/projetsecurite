package main;

import java.io.IOException;


import manager.*;
import manager.SecurityManager;
import model.*;

public class App {
	 public static void main(String[] args) throws Exception  {
		 
		 CoreModel CoreM = new CoreModel();
		 CoreManager Core = new CoreManager(CoreM);
		 CoreM.setManager(Core);
		 
		 TerminalModel TerminalM = new TerminalModel();
		 TerminalManager Terminal = new TerminalManager(TerminalM, Core);
		 Core.setTerminal(Terminal);
		 TerminalM.setManager(Terminal);
		 
		 LogManager Log = new LogManager(Core);
		 Core.setLog(Log);
		 
		 UserModel UserM = new UserModel();
		 UserManager User = new UserManager(UserM, Core);
		 Core.setUser(User);
		 UserM.setManager(User);
		 
		 ServerModel ServerM = new ServerModel();
		 ServerManager Server = new ServerManager(ServerM, Core);
		 Core.setServer(Server);
		 ServerM.setManager(Server);
		 
		 RequestModel RequestM = new RequestModel();
		 RequestManager Request = new RequestManager(RequestM, Core);
		 Core.setRequest(Request);
		 RequestM.setManager(Request);
		 
		 SecurityManager Security = new SecurityManager(Core);
		 Core.setSecurity(Security);
		 
		 
		 Core.start();
	 }
}
