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
	private String senderFamilly;
    private byte[] id;
	private byte[] content;
	
	private PacketModel temp;

	public String getSenderFamilly() {
		return senderFamilly;
	}

	public void setSenderFamilly(String senderFamilly) {
		this.senderFamilly = senderFamilly;
	}

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
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




	
}
