package model;

import java.io.Serializable;

import dataFormatter.DataUtil;
import generalizer.GsaList;
import manager.RequestManager;

public class RequestModel  implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestManager manager = null;

	
	private DataUtil du = null; 
	private String header;


	public void setManager(RequestManager manager){
		this.manager = manager;
	}

	public DataUtil getDu() {
		return this.du;
	}

	public void setDu(DataUtil du) {
		this.du = du;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
