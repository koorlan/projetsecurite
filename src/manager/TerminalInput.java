package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalInput implements Runnable{

	private  TerminalManager manager = null;
	private boolean running = true;
	private BufferedReader consoleIn   = null;
	
	public TerminalInput(TerminalManager manager) {
		super();
		this.manager = manager;
		consoleIn = new BufferedReader( new InputStreamReader(System.in));
	}

	@Override
	public void run() {
		String line = "";
		while(this.running){
			try {
				line = consoleIn.readLine();
				this.manager.process(line);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				//Thread.currentThread().interrupt();
				break;
			}
		}
		this.manager.getCore().getLogManager().log(this,"stopped");
		this.running = false;
		return;
	}
	
	public void stop() throws IOException{
		this.running = false;
		this.consoleIn.close();	
	}
	
	public void test(String str){
		this.manager.write(str);
	}
}
