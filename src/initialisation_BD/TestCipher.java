package initialisation_BD;

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
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TestCipher {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeySpecException {
		String clair = "iezjfoizeuhiofioze";
		
		//gen AES key
		byte[] aes = TestCipher.generateAES_KEY();
		//gen Rsa keys
		byte[][] key1 = TestCipher.generateRSA_KEYS();
		byte[][] key2 = TestCipher.generateRSA_KEYS();
		byte[][] key3 = TestCipher.generateRSA_KEYS();
		
		byte[] chiffre = TestCipher.cryptWithAes(clair.getBytes(), TestCipher.decodeAES_KEY(aes));
		//basicTest
		System.out.println("Test simple >> " + new String(TestCipher.decryptWithAes(chiffre,TestCipher.decodeAES_KEY(aes))));
	
		//avec k1
		///Padding
		byte[] aesK1 = TestCipher.cryptWithRSApadding(aes, TestCipher.decodeRSA_KEYS(key1[0], null).getPublic());
		//loop with non-padded
		byte[] aesK2 =  TestCipher.cryptWithRSA(aesK1, TestCipher.decodeRSA_KEYS(key2[0], null).getPublic());
		byte[] aesK3 =  TestCipher.cryptWithRSA(aesK2, TestCipher.decodeRSA_KEYS(key3[0], null).getPublic());
		
		//String hardtest = Base64.getEncoder().encodeToString(aesK3);
		//Decrypt aesk
		///non padding
		byte[] test3 = TestCipher.decryptWithRSA(aesK3, TestCipher.decodeRSA_KEYS(null, key3[1]).getPrivate());
		byte[] test2  = TestCipher.decryptWithRSA(test3, TestCipher.decodeRSA_KEYS(null, key2[1]).getPrivate());
		byte[] test  = TestCipher.decryptWithRSApadding(test2, TestCipher.decodeRSA_KEYS(null, key1[1]).getPrivate());
		
		System.out.println("Test un poil moins simple >> " + new String(TestCipher.decryptWithAes(chiffre,TestCipher.decodeAES_KEY(test))));
		
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
