package crypto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.StringTokenizer;

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
	
	public String decipher(String cipher, String keysRef)
	{
		if( !this.model.isInitialized() )
		{	
			this.core.getLog().err(this, "Crypto model is still uninitialized");
			return null; 
		}
		this.core.getLog().log(this, "Deciphering");
		byte[] currentPrivK;
		byte[] bCipher = cipher.getBytes();
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
				// TODO : encode this in B64 ? mb cipher too... 
				currentPrivK = Base64.getEncoder().encode(this.model.getKeysMap().get(hMapK).getBytes());
				System.out.println(currentPrivK);
				rsa.setPrivateKey(currentPrivK);
				if(i == hMapKeys.length - 1)
					plain = rsa.decryptInString(bCipher);
				else
					bCipher = rsa.decryptInBytes(bCipher);
			}
		}
		else 
		{
			this.core.getLog().log(this, "Unique key required");
			System.out.println("hmap key passed : " + keysRef);
			System.out.println("Check Hmap content : " + this.model.getKeysMap().containsKey(keysRef));
			// TODO : idem  
			currentPrivK = Base64.getEncoder().encode(this.model.getKeysMap().get(keysRef).getBytes());
			System.out.println("privK used : " + currentPrivK);
			rsa.setPrivateKey(currentPrivK);
			plain = rsa.decryptInString(bCipher);
		}
		return plain;
	}
}
