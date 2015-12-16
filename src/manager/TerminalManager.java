package manager;


import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;

import model.PacketModel;
import model.SecurityModel;
import model.TerminalModel;

public class TerminalManager {
	private TerminalModel model = null;
	private CoreManager core = null;
	
	private Thread inputThread = null;
	private TerminalInput inputRunnable = null;

	
	public TerminalManager(TerminalModel model, CoreManager core) {
		super();
		this.model = model;
		this.core = core;
	}
	
	public void start() throws Exception{		
		this.core.getLog().log(this,"Starting input handler");
		this.inputRunnable = new TerminalInput(this);
		this.inputThread  = new Thread(this.inputRunnable);
		this.inputThread.start();

		return;
	}
	
	public void close() throws Exception{
		this.core.getLog().log(this,"Stopping....");
		this.inputRunnable.stop();
		this.inputThread.interrupt();
		
		//TODO check why it's blocking...
		//this.inputThread.join();
		this.inputThread = null;

		
	}
	
	
	public boolean isRunning(){
		return this.inputThread != null;
	}
	
	public void process(String str) throws Exception{
		System.out.println("<<DEBUG>>  \'" + str +"\'");
			if(str.startsWith(".")) str = str.replaceFirst(".",""); else return;
				
			if(str.equalsIgnoreCase("bye")){
				this.core.getLog().log(this,"closing input handler");
				this.inputThread = null;
				return;
			}
			if(str.contains("testsocket")){
				String[] cmd = str.split(" ");
				
				String ip = cmd[1];
				int port = Integer.parseInt(cmd[2]);
				
				
				PacketModel packet = new PacketModel();
				packet = this.core.getPacket().forge("POST", "FLOOD");
				while(true){
					this.core.getPacket().sendPacket(packet, cmd[2]);
					System.out.println("Sended...");
					Thread.sleep(2000);
					if(false)
						break;
				}
			}
			
			this.core.process(str);
			
	}
	
	public void write(String str){
		this.model.write(str);
	}
	
	
	public void update(String str){
		if(str.equalsIgnoreCase("buffer")){
			System.out.println(this.model.getBuffer());
		}
	}
	
	//maybe create inherance...
	public CoreManager getCore(){
		return this.core;
	}
	
}
