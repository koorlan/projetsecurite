package manager;

import java.io.Serializable;
import static java.lang.Math.random;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.SerializationUtils;

import model.PacketModel;
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
	
	public byte[] encryptPacket(byte[] bPacket){

		
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
			packet.setData(cipher.doFinal(bPacket));
			return SerializationUtils.serialize(packet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decryptPacket(byte[] bPacket){

		
	    try {
			byte[] decryptedContent = null;	
			
			SecurityModel packet = new SecurityModel();
			packet = (SecurityModel)SerializationUtils.deserialize(bPacket);
			
			SecretKey key = decryptAESKey(packet.getKey());
			
			//now decrypt packet
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
	      
	      	packet.setKey(key.getEncoded());
	      	byte[] newBytes = cipher.doFinal(packet.getData());
	      	return newBytes;
	    } catch (Exception ex) {
	     	this.core.getLog().err(this,"Error in decryption" );
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
	        cipher.init(Cipher.ENCRYPT_MODE, this.core.getUser().getPublicKey() );
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
            privKey = this.core.getUser().getPrivateKey();

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
        
    public static byte[] sha1(String message) throws NoSuchAlgorithmException{
        MessageDigest sha = MessageDigest.getInstance("sha-1");
        sha.update(message.getBytes());
        byte[] digest  = sha.digest();
        return digest;
    }
}
