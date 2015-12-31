package manager;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import filter.FilterManager;
import generalizer.GeneralizerManager;
import model.CoreModel;


public class CoreManager{
	
	Function<String, String> StandardizationModule = instanceName -> {
		instanceName = instanceName.replace("manager.", "");
        instanceName = instanceName.replace("Manager", "");
        instanceName = instanceName.toLowerCase();
        return instanceName;
	};
	
	
	private CoreModel model = null;
	
	
	private String service;
	
	//Modules
	private ArrayList<Object> modules = new ArrayList<Object>();
	
	
	public CoreManager(CoreModel core, String service) {
		super();
		this.model = core;
		this.service = service;
	}

	
	public void set(Object o){
		modules.add(o);
	}

	public void start() throws Exception{
		//Start all part of application including thread
		this.initCommands();
		//do smthg
		this.getLog().log(this, "Core started.");
		this.getTerminal().start();
		switch (this.service){
		case "user":
			break;
		case "frontal":
			this.getFrontal().getInternalserverManager().start();
			this.getFrontal().getExternalserverManager().start();
			break;
		case "central":
			break;
		}
		
		
		//wait closing
		this.loop();
	}
	
	public void loop() throws InterruptedException{
		while(true){
			if(!this.getTerminal().isRunning()){
				break;
			}else{
				Thread.sleep(1000);
			}
		}
		this.close();
	}
	
	public void close(){
		this.getLog().log(this, "Closing app.");
		//close openManager
		 Iterator<Entry<String, Object>> it = instance.entrySet().iterator();
		 while (it.hasNext()) {
		        Map.Entry<String, Object> objMap = (Map.Entry<String, Object>)it.next();
		        if((objMap.getValue()!= null) && commands.containsKey(objMap.getKey()+"-close") ){
		        	System.out.println("Closing  " + objMap.getKey());
		        }
		}
		return;
	}
	
	//commands
	private Map<String, Method> commands = new HashMap<String, Method>();
	private Map<String, Object> instance = new HashMap<String, Object>();
	
	public void initCommands()throws Exception{
		//populate Object in the hashMap Instance
		
		
		for(Object module: this.modules){
			if(module != null) instance.put(StandardizationModule.apply(module.getClass().getName()), module);
		}
			
		 Iterator<Entry<String, Object>> it = instance.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Object> objMap = (Map.Entry<String, Object>)it.next();
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
			this.getLog().err(this,"Minimum of 2 instruction to run a command. Syntax : .<method> <manager> <parameter>");
			return;
		}
		String command = cmd[0];
		String manager = cmd[1];
		int nbArgs = cmd.length - 2 ; //remove command and instance 
		if(!this.instance.containsKey(manager) || !this.commands.containsKey(manager+"-"+command)){
			this.getLog().err(this,command + " not found or " + manager + " not exist/loaded" );
			return;
		}
		if(nbArgs < this.commands.get(manager+"-"+command).getParameterTypes().length ){
			this.getLog().err(this,"Number of arg expected : " +  this.commands.get(manager+"-"+command).getParameterTypes().length);
			return;
		}
		
		Object obj;
		switch (this.commands.get(manager+"-"+command).getParameterTypes().length){
		case 0:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager))) != null){
				this.getLog().log(this,obj.toString());
			}
			break;
		case 1:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager), cmd[2])) != null){
				this.getLog().log(this,obj.toString());
			}
			break;
		case 2:
			if ( (obj = this.commands.get(manager+"-"+command).invoke(this.instance.get(manager), cmd[2], cmd[3])) != null){
				this.getLog().log(this,obj.toString());
			}
		default:
			
			break;
		}
			
		return;
	}
	
	
	public String getService() {
		return service;
	}
	//Redirect to right module
	//maybe execution policy ????
	public TerminalManager getTerminal(){
		for(Object o: this.modules){
			if(o instanceof TerminalManager){
				return (TerminalManager)o;
			}		
		}
		return null;
	}
	public LogManager getLog(){
		for(Object o: this.modules){
			if(o instanceof LogManager){
				return (LogManager)o;
			}		
		}
		return null;
	}
	public UserManager getUser(){
		for(Object o: this.modules){
			if(o instanceof UserManager){
				return (UserManager)o;
			}		
		}
		return null;
	}
	public PacketManager getPacket(){
		for(Object o: this.modules){
			if(o instanceof PacketManager){
				return (PacketManager)o;
			}		
		}
		return null;
	}
	
	public SecurityManager getSecurity(){
		for(Object o: this.modules){
			if(o instanceof SecurityManager){
				return (SecurityManager)o;
			}		
		}
		return null;
	}
	
	public RequestManager getRequest(){
		for(Object o: this.modules){
			if(o instanceof RequestManager){
				return (RequestManager)o;
			}		
		}
		return null;
	}
	
	public GeneralizerManager getGeneralizer(){
		for(Object o: this.modules){
			if(o instanceof GeneralizerManager){
				return (GeneralizerManager)o;
			}		
		}
		return null;
	}

	public DialogQueryManager getDialog() {
		for(Object o: this.modules){
			if(o instanceof DialogQueryManager){
				return (DialogQueryManager)o;
			}		
		}
		return null;
	}

	public BroadcastManager getBroadcast() {
		for(Object o: this.modules){
			if(o instanceof BroadcastManager){
				return (BroadcastManager)o;
			}		
		}
		return null;
	}
	
	public DBManager getDB(){
		for(Object o: this.modules){
			if(o instanceof DBManager){
				return (DBManager)o;
			}		
		}
		return null;
	}
	
	public ServerManager getServer(){
		for(Object o: this.modules){
			if(o instanceof ServerManager){
				return (ServerManager)o;
			}		
		}
		return null;
	}
	
	 public FrontalManager getFrontal() {
			for(Object o: this.modules){
				if(o instanceof FrontalManager){
					return (FrontalManager)o;
				}		
			}
			return null;
		}
	
	public ServerManager getServerByPort(int port){
		for(Object o: this.modules){
			if(o instanceof ServerManager && ((ServerManager) o).getModel().getPort() == port){
				return (ServerManager)o;
			}		
		}
		return null;
	}


	public FilterManager getFilter() {
		for(Object o: this.modules){
			if(o instanceof FilterManager){
				return (FilterManager)o;
			}		
		}
		return null;
	}
}
