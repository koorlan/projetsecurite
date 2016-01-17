package crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import manager.CoreManager;

public class CryptoUtilsManager {
	
	CoreManager core; 
	CryptoUtilsModel model;
	
	public CryptoUtilsManager(CryptoUtilsModel model, CoreManager core)
	{
		this.core = core;
		this.model = model; 
	}
	
	public void setPrivateKeys(Map<String, String> keys) throws ClassNotFoundException, SQLException
	{
		this.model.setKeysMap(keys);
		this.model.setInitialized(true); 
	}
	
	public Set<String> getKeySet()
	{
		return this.model.getKeysList();
	}
	
	public String decipherSecK(String cipher, String keysRef)
	{
		if( !this.model.isInitialized() )
		{	
			this.core.getLog().err(this, "Crypto model is still uninitialized");
			return null; 
		}
		this.core.getLog().log(this, "Deciphering");
		String currentPrivK;
		//String bCipher = cipher.getBytes();
		String plain = null;
		MyRSA rsa = new MyRSA();
		if(keysRef.contains(","))
		{
			this.core.getLog().log(this, "Multiple keys required");
			String[] hMapKeys = keysRef.split(",");
			String hMapK;
			
			for(int i = 0; i < hMapKeys.length; i ++)
			{
				hMapK = hMapKeys[i];
				currentPrivK = this.model.getKeysMap().get(hMapK);
				System.out.println("Current Hmap privK used0 : " + currentPrivK);
				if(i == hMapKeys.length - 1)
					plain = rsa.dechiffrementRSAInString(currentPrivK, cipher);
				else 
					cipher = rsa.dechiffrementRSAInString(currentPrivK, cipher);
			}
		}
		else 
		{
			this.core.getLog().log(this, "Unique key required");
			System.out.println("hmap key passed : " + keysRef);
			System.out.println("Check Hmap content : " + this.model.getKeysMap().containsKey(keysRef));
			currentPrivK = this.model.getKeysMap().get(keysRef);
			System.out.println("privK used : " + currentPrivK);
			System.out.println("cipher text used : " + cipher);
			plain = rsa.dechiffrementRSAInString(currentPrivK, cipher);
			
		}
		System.out.println("Plain sec key is <<" + plain + ">>");
		return plain;
	}
	
	public String decipher(String cipher, String kSec)
	{
		//AES aes = new AES(null);
		//return aes.dechiffrementAESInByte(kSec, cipher).toString();
		byte[] keyInByte = Base64.getDecoder().decode(kSec);
		byte[] ciphertextInByte = Base64.getDecoder().decode(cipher);
		AES aes;
		aes = new AES(keyInByte);
		return aes.dechiffrerMess(ciphertextInByte).toString();
		// System.out.println(new String(aes.dechiffrerMess(ciphertextInByte))+"valeur en clair ");
		 
	}
	

}
