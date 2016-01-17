package crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import initialisation_BD.TestCipher;
import manager.CoreManager;

public class CryptoUtilsManager {
	
	CoreManager core; 
	CryptoUtilsModel model;
	
	public CryptoUtilsManager(CryptoUtilsModel model, CoreManager core)
	{
		this.core = core;
		this.model = model; 
	}
	
	public void setPrivateKeys(Map<String, byte[]> keys) throws ClassNotFoundException, SQLException
	{
		this.model.setKeysMap(keys);
		this.model.setInitialized(true); 
	}
	
	public Set<String> getKeySet()
	{
		return this.model.getKeysList();
	}
	

	public byte[] decipher(byte[] cipher, String keysRef) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException

	{
		if( !this.model.isInitialized() )
		{	
			this.core.getLog().err(this, "Crypto model is still uninitialized");
			return null; 
		}
		this.core.getLog().log(this, "Deciphering");

		byte[] currentPrivK;
		byte[] plain = null;

		if(keysRef.contains(","))
		{
			this.core.getLog().log(this, "Multiple keys required");
			String[] hMapKeys = keysRef.split(",");
			String hMapK;
			
			//Save data
			byte[] tmpPlain; 
			for(int i = hMapKeys.length-1 ; i >= 0; i--)
			{
				hMapK = hMapKeys[i];
				currentPrivK = this.model.getKeysMap().get(hMapK);
				
				if(i == 0){
					//last decrypt
					tmpPlain = TestCipher.decryptWithRSApadding(cipher, TestCipher.decodeRSA_KEYS(null, currentPrivK).getPrivate());	
				}else{
					//other decrypt
					tmpPlain = TestCipher.decryptWithRSA(cipher, TestCipher.decodeRSA_KEYS(null, currentPrivK).getPrivate());	
				}
				cipher = tmpPlain;
			}
			plain = cipher;
		}
		else 
		{
			this.core.getLog().log(this, "Unique key required");
			// TODO : idem  
			currentPrivK = this.model.getKeysMap().get(keysRef);
			plain = TestCipher.decryptWithRSApadding(cipher, TestCipher.decodeRSA_KEYS(null, currentPrivK).getPrivate());	
		
		}
		return plain;
	}
	

	public static byte[] generateAES_KEY() throws NoSuchAlgorithmException{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        byte[] aeskb = key.getEncoded();
        return aeskb;
    }
    
    public static byte[][] generateRSA_KEYS() throws NoSuchAlgorithmException{
         KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("RSA");
         KeyGen.initialize(1024);
         KeyPair pair = KeyGen.generateKeyPair();
         
         //PrivateKey kpriv =  KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec());
         //PublicKey kpub =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec());
         byte[][] pairs = new byte[2][];
         pairs[0] = pair.getPublic().getEncoded();
         pairs[1] = pair.getPrivate().getEncoded();
         
         return pairs;
    }
    
    public static SecretKey decodeAES_KEY(byte[] aeskb){
        SecretKey aesk = new SecretKeySpec(aeskb,"AES");
        return aesk;
    }
    
    public static KeyPair decodeRSA_KEYS(byte[] kpubs, byte[] kprivs) throws InvalidKeySpecException, NoSuchAlgorithmException{
        if(kpubs != null && kprivs != null)
        	return null;
        if(kpubs == null){
    		PrivateKey kpriv =  KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(kprivs));
    		 KeyPair pair = new KeyPair(null,kpriv);
    		 return pair;
        }
        if(kprivs == null){
        	PublicKey kpub =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(kpubs));
        	KeyPair pair = new KeyPair(kpub,null);
    		return pair;
        }
      
		PrivateKey kpriv =  KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(kprivs));
    	PublicKey kpub =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(kpubs));
        KeyPair pair = new KeyPair(kpub,kpriv); 
        return pair;
    }
    

    //Crypt AES
    public static byte[] cryptWithAes(byte[] data, SecretKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    
    //Decrypt AES
    public static byte[] decryptWithAes(byte[] data, SecretKey key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

   //Crypt RSA 
    public static byte[] cryptWithRSApadding(byte[] data, PublicKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    	 Cipher cipher = Cipher.getInstance("RSA");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         return cipher.doFinal(data);
    }
    
    public static byte[] cryptWithRSA(byte[] data, PublicKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
   	 Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
   }
   
    //Decrypt RSA
    public static byte[] decryptWithRSApadding(byte[] data, PrivateKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
   	 Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] toReturn = cipher.doFinal(data);
        return toReturn;
   }
    
    public static byte[] decryptWithRSA(byte[] data, PrivateKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
      	 Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
           cipher.init(Cipher.DECRYPT_MODE, key);
           byte[] toReturn = cipher.doFinal(data);
           return toReturn;
      }
	
}
