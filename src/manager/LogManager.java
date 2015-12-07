package manager;



public class LogManager {
	private CoreManager core = null;

	public LogManager(CoreManager Core) {
		this.core = core;
	}
	
	public void log(String str){
		System.out.println("(Log) >>\'" + str + "\'");
	}
}
