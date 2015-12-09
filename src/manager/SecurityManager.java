package manager;

import model.RequestModel;

public class SecurityManager {
	private CoreManager core = null;
	
	//I think no need a model for the component he just need to gather usefull informations from other manager to encrypt/decryptdata
	public SecurityManager(CoreManager core){
		super();
		this.core = core;
	}
	
	public RequestModel encryptRequest(RequestModel req){
		byte[] secureContent = req.getContent();
		String secureContentString = new String(secureContent);
		secureContentString = "<SECURITY>" + secureContentString + "/<SECURITY>";
		secureContent = secureContentString.getBytes();
		req.setContent(secureContent);
		return req;
	}
}
