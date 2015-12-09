package manager;

import javax.crypto.Cipher;

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
		
		//TestingPart
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, this.core.getUserManager().getPublicKey());
			cipherText = cipher.doFinal(secureContent);
			secureContent = cipherText;
		} catch (Exception e) {
			e.printStackTrace();
		}

		req.setContent(secureContent);
		return req;
	}
	
	public RequestModel decryptRequest(RequestModel req){
		return null;
	}
}
