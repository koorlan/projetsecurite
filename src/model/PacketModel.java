package model;

import java.io.Serializable;
import java.util.Arrays;

import manager.PacketManager;

public class PacketModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PacketManager manager;
	
	public enum Type { GET, POST};
	
	private Type type;
	private byte[] content;
	private boolean eof;
	
	private PacketModel temp;
	
	public boolean isEof() {
		return eof;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}



	public void setContent(byte[] content) {
		this.content = content;
		this.eof = true;
	}



	public void setManager(PacketManager manager) {
		this.manager = manager;
	}
	
	public void save(PacketModel packet){
		//clean previous TODO ... maybe dealocate.
		this.temp = null;
		this.temp = packet;
	}
	
	public PacketModel getPacket(){
		return this.temp;
	}

	@Override
	public String toString() {
		return "PacketModel [manager=" + manager + ", type=" + type + ", content=" + Arrays.toString(content) + ", eof="
				+ eof + ", temp=" + temp + ", isEof()=" + isEof() + ", getType()=" + getType() + ", getContent()="
				+ Arrays.toString(getContent()) + ", getPacket()=" + getPacket() + "]";
	}



	
}
