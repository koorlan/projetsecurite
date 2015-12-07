package model;

import manager.TerminalManager;

public class TerminalModel {
	private TerminalManager manager = null;
	
	private String buffer = null;
	
	public void setManager(TerminalManager manager){
		this.manager = manager;
	}
	
	public void write(String str){
		this.buffer = str;
		this.manager.update("buffer");
	}

	public String getBuffer() {
		return buffer;
	}
}
