package manager;

import model.TerminalModel;

public class TerminalManager {
	private TerminalModel model = null;
	private CoreManager core = null;
	
	private Thread inputThread = null;

	public TerminalManager(TerminalModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public void start(){
		this.core.getLogManager().log("TerminalManager Starting thread");
		Thread tInputThread = new Thread(new TerminalInput(this));
		this.inputThread = tInputThread;
		this.inputThread.start();
		return;
	}
	
	public boolean isThreadRunning(){
		return (!(this.inputThread == null));
	}
	
	public boolean isRunning(){
		return this.isThreadRunning();
	}
	
	public void process(String str){
		System.out.println("<<DEBUG>>  \'" + str +"\'");
		if(str.equalsIgnoreCase(".bye")){
			this.core.getLogManager().log("TerminalManager closing thread");
			this.inputThread = null;
		}
		if(str.equalsIgnoreCase(".getUserName")){
			this.write(this.core.getUserManager().getNom());
		}
		if(str.startsWith(".setUserName")){
			String[] parse = str.split(" ");
			if(parse[1] != null) this.core.getUserManager().setNom(parse[1]);
		}
	}
	
	public void write(String str){
		this.model.write(str);
	}
	
	
	public void update(String str){
		if(str.equalsIgnoreCase("buffer")){
			System.out.println(this.model.getBuffer());
		}
	}
	
	
}
