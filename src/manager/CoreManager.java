package manager;

import model.CoreModel;

public class CoreManager{
	private CoreModel model = null;
	
	//Modules
	private TerminalManager terminal = null;
	private LogManager log = null;
	private UserManager user = null;
	private ServerManager server = null;
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
	
	public void start(){
		//Start all part of application including thread
		terminal.start();
		
		//do smthg
		terminal.write("(Core) Started...Starting all..");
		
		//wait closing
		this.loop();
	}
	
	public void loop(){
		while(true){
			if(terminal.isRunning()) break;
		}
		this.close();
	}
	
	public void close(){
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
}
