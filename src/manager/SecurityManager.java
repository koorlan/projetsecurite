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
	
	public RequestModel encryptRequest(RequestModel request){
		RequestModel req = new RequestModel();
		req.setType(request.getType());
		req.setContent(request.getContent());
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
			req.setContent(secureContent);
			return req;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RequestModel decryptRequest(RequestModel request){
		RequestModel req = new RequestModel();
		req.setType(request.getType());
		req.setContent(request.getContent());
		byte[] decryptedContent = null;		
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, this.core.getUserManager().getPrivateKey());
	      decryptedContent = cipher.doFinal(req.getContent());
	      req.setContent(decryptedContent);	
	      return req;
	    } catch (Exception ex) {
	     	this.core.getLogManager().err(this,"Error in decryption" );
	    }
	  return null;  
	}
}
