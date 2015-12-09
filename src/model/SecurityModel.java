package model;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class SecurityModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private manager.SecurityManager manager;
	
	private byte[] key;
	private byte[] data;
	
	
	
	
	public byte[] getKey() {
		return key;
	}
	public void setKey(byte[] key) {
		this.key = key;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public void setManager(manager.SecurityManager security) {
		this.manager = security;
	}
	
	
}
