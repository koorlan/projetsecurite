package model;

import java.io.Serializable;
import java.util.Arrays;

import manager.RequestManager;

public class RequestModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RequestManager manager;
	
	public enum Type { GET, POST};
	
	private Type type;
	private byte[] content;
	private boolean eof;
	
	private RequestModel temp;
	
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



	public void setManager(RequestManager manager) {
		this.manager = manager;
	}
	
	public void save(RequestModel req){
		//clean previous TODO ... maybe dealocate.
		this.temp = null;
		this.temp = req;
	}
	
	public RequestModel getRequest(){
		return this.temp;
	}

	@Override
	public String toString() {
		return "RequestModel [manager=" + manager + ", type=" + type + ", content=" + Arrays.toString(content)
				+ ", eof=" + eof + ", isEof()=" + isEof() + ", getType()=" + getType() + ", getContent()="
				+ Arrays.toString(getContent()) + "]";
	}

	
}
