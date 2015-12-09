package manager;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.SerializationUtils;

import model.RequestModel;
import model.SecurityModel;

public class SecurityManager {
	private CoreManager core = null;
	private SecurityModel model = null;
	
	private int sessionKey;
	private byte[] data;
	
	
	public SecurityManager(SecurityModel model ,CoreManager core){
		super();
		this.core = core;
		this.model = model;
	}
	
	public byte[] encryptRequest(byte[] request){

		
		//TestingPart
		try {
			byte[] secureContent = null;
			
			SecurityModel packet = new SecurityModel();
			
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128);
			SecretKey key = keyGenerator.generateKey();
			byte[] bKey = this.EncryptSecretKey(key);

			//now Encrypt the message with the non-encrypted ticket stored in bytesTicket
			
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			packet.setKey(bKey);
			packet.setData(cipher.doFinal(request));
			return SerializationUtils.serialize(packet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decryptRequest(byte[] request){

		
	    try {
			byte[] decryptedContent = null;	
			
			SecurityModel packet = new SecurityModel();
			packet = (SecurityModel)SerializationUtils.deserialize(request);
			
			SecretKey key = decryptAESKey(packet.getKey());
			
			//now decrypt packet
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
	      
	      	packet.setKey(key.getEncoded());
	      	byte[] newBytes = cipher.doFinal(packet.getData());
	      	return newBytes;
	    } catch (Exception ex) {
	     	this.core.getLogManager().err(this,"Error in decryption" );
	    }
	  return null;  
	}

	private byte[] EncryptSecretKey (SecretKey skey)
	{
	    Cipher cipher = null;
	    byte[] key = null;

	    try
	    {
	        // initialize the cipher with the user's public key
	        cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.ENCRYPT_MODE, this.core.getUserManager().getPublicKey() );
	        key = cipher.doFinal(skey.getEncoded());
	    }
	    catch(Exception e )
	    {
	        System.out.println ( "exception encoding key: " + e.getMessage() );
	        e.printStackTrace();
	    }
	    return key;
	}

	private SecretKey decryptAESKey(byte[] data )
    {
        SecretKey key = null;
        PrivateKey privKey = null;
        Cipher cipher = null;

        try
        {
            // this is OUR private key
            privKey = this.core.getUserManager().getPrivateKey();

            // initialize the cipher...
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey );

            // generate the aes key!
            key = new SecretKeySpec ( cipher.doFinal(data), "AES" );
        }
        catch(Exception e)
        {
            System.out.println ( "exception decrypting the aes key: " 
                                                   + e.getMessage() );
            return null;
        }

        return key;
    }
}
