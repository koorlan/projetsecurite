package manager;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.CoreModel;

public class CoreManager{
	private CoreModel model = null;
	
	//Modules
	private TerminalManager terminal = null;
	private LogManager log = null;
	private UserManager user = null;
	private ServerManager server = null;
	private RequestManager request = null;
	private SecurityManager security = null;
	//private ClientManager client = null;
	
	public CoreManager(CoreModel core) {
		super();
		this.model = core;
	}

	
	public void setTerminal(TerminalManager terminal){
		this.terminal = terminal;
		return;
	}
	
	public void setLog(LogManager log){
		this.log = log;
		return;
	}
	
	public void setUser(UserManager user){
		this.user = user;
		return;
	}
	
	public void setServer(ServerManager server){
		this.server = server;
		return;
	}
	
	
	public void setRequest(RequestManager request) {
		this.request = request;
	}
	
	public void setSecurity(SecurityManager security) {
		this.security = security;
	}


	public void start() throws Exception{
		//Start all part of application including thread
		this.initCommands();
		//do smthg
		this.log.log(this, "Core started.");
		terminal.start();
		
		System.out.println("Monitor start");
		//wait closing
		this.loop();
	}
	
	public void loop() throws InterruptedException{
		while(true){
			if(!this.terminal.isRunning()){
				break;
			}else{
				Thread.sleep(1000);
			}
		}
		this.close();
	}
	
	public void close(){
		this.log.log(this, "Closing app.");
		return;
	}
	
	//commands
	private Map<String, Method> commands = new HashMap<String, Method>();
	private Map<String, Object> instance = new HashMap<String, Object>();
	
	public void initCommands()throws Exception{
		//populate Object in the hashMap Instance
		String instanceName = this.terminal.getClass().getName();
        instanceName = instanceName.replace("manager.", "");
        instanceName = instanceName.replace("Manager", "");
        instanceName = instanceName.toLowerCase();
		instance.put(instanceName, this.terminal);
		
		instanceName = this.user.getClass().getName();
        instanceName = instanceName.replace("manager.", "");
        instanceName = instanceName.replace("Manager", "");
        instanceName = instanceName.toLowerCase();
		instance.put(instanceName, this.user);
		
		instanceName = this.server.getClass().getName();
        instanceName = instanceName.replace("manager.", "");
        instanceName = instanceName.replace("Manager", "");
        instanceName = instanceName.toLowerCase();
		instance.put(instanceName, this.server);
		
		instanceName = this.request.getClass().getName();
        instanceName = instanceName.replace("manager.", "");
        instanceName = instanceName.replace("Manager", "");
        instanceName = instanceName.toLowerCase();
		instance.put(instanceName, this.request);
		
		
		 Iterator it = instance.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry objMap = (Map.Entry)it.next();
		        //do stuff
		        Method[] m = objMap.getValue().getClass().getDeclaredMethods();	
		        //System.out.println("Using \'" + objMap.getKey() + "\'");
				for(int i = 0; i < m.length; i++) {
			        //System.out.println(m[i].getName());       
			        commands.put(objMap.getKey()+"-"+m[i].getName(),m[i]);     
			     }
		 }
		
	}
	
	public void process(String str) throws Exception{
		//Now work with cmd[0] cmd[1] ect.. ect.
		String[] cmd = str.split(" ");
		//TODO Display Help when 1 args ?
		if (cmd.length < 2){
			this.log.err(this,"Minimum of 2 instruction to run a command see help.");
			return;
		}
		String command = cmd[0];
		String manager = cmd[1];
		int nbArgs = cmd.length - 2 ; //remove command and instance 
		if(!this.instance.containsKey(manager) || !this.commands.containsKey(manager+"-"+command)){
			this.log.err(this,command + " not found or " + manager + " not exist/loaded" );
			return;
		}
		if(nbArgs < this.commands.get(manager+"-"+command).getParameterTypes().length ){
			this.log.err(this,"Number of arg expected : " +  this.commands.get(manager+"-"+command).getParameterTypes().length);
			return;
		}
		
		Object obj;
		switch (this.commands.get(manager+"-"+command).getParameterTypes().length){
		case 0:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager))) != null){
				this.log.log(this,obj.toString());
			}
			break;
		case 1:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager), cmd[2])) != null){
				this.log.log(this,obj.toString());
			}
			break;
		case 2:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager), cmd[2], cmd[3])) != null){
				this.log.log(this,obj.toString());
			}
		default:
			
			break;
		}
			
		return;
	}
	
	//Redirect to right module
	public TerminalManager getTerminalManager(){
		return this.terminal;
	}
	public LogManager getLogManager(){
		return this.log;
	}
	public UserManager getUserManager(){
		return this.user;
	}
	public RequestManager getRequestManager(){
		return this.request;
	}
	
	public SecurityManager getSecurityManager(){
		return this.security;
	}
	
}