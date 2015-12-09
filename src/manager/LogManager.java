package manager;



public class LogManager {
	private CoreManager core = null;

	public LogManager(CoreManager Core) {
		this.core = core;
	}
	
	private String clean(String str){
		str = str.replace("java.", "");
		str = str.replace("lang.", "");
		str = str.replace("manager.", "");
		str = str.replace("model.", "");
		str = str.replace("main.", "");
		return str;		
	}
	
	public void log(Object o,String str){
		str = this.clean(str);
		if(o == null)
			System.out.println("(Log)> UNKOWN \'"+ str + "\'");
		else
			System.out.println("(Log)>\'" + o.getClass().getName().replaceFirst(".+?\\.", "")+" "+ str + "\'");
	}
	public void warn(Object o,String str){
		str = this.clean(str);
		if(o == null)
			System.out.println("(Log)> UNKOWN \'"+ str + "\'");
		else
			System.out.println("(Warning)>" + o.getClass().getName().replaceFirst(".+?\\.", "")+"\' "+ str + "\'");
	}
	public void err(Object o,String str){
		str = this.clean(str);
		if(o == null)
			System.out.println("(Log)> UNKOWN \'"+ str + "\'");
		else
			System.out.println("(Error)>" + o.getClass().getName().replaceFirst(".+?\\.", "")+"\' "+ str + "\'");
	}
	
	public void close(){
		System.out.println("(ByeBye)> I'm closing. See you soon.");
	}
}
