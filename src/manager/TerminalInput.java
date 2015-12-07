package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalInput implements Runnable{

	private  TerminalManager manager = null;
	private BufferedReader consoleIn   = null;
	
	public TerminalInput(TerminalManager manager) {
		super();
		this.manager = manager;
		consoleIn = new BufferedReader( new InputStreamReader(System.in));
	}

	@Override
	public void run()  {
		String line = "";
		while(this.manager.isThreadRunning()){
				try {
					line = consoleIn.readLine();
					this.manager.process(line);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		System.out.println("Goodbye");
	}

	public void test(String str){
		this.manager.write(str);
	}
}
