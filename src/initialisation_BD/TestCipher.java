package initialisation_BD;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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
	
	public static String generateAES_KEY() throws NoSuchAlgorithmException{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        byte[] aeskb = key.getEncoded();
        String aesks = Base64.getEncoder().encodeToString(aeskb);
        return aesks;
    }
    
    public String[] generateRSA_KEYS() throws NoSuchAlgorithmException{
         KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("RSA");
         KeyGen.initialize(2048);
         KeyPair pair = KeyGen.generateKeyPair();
         String[] pairs = new String[2];
         pairs[0] = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
         pairs[1] = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
         
         return pairs;
    }
    
    public SecretKey decodeAES_KEY(String aesks){
        byte[] aeskb = Base64.getDecoder().decode(aesks);
        SecretKey aesk = new SecretKeySpec(aeskb,0,aeskb.length,"AES");
        return aesk;
    }
    
    public KeyPair decodeRSA_KEYS(String kprivs, String kpubs) throws InvalidKeySpecException, NoSuchAlgorithmException{
        byte[] kpubb = Base64.getDecoder().decode(kpubs);
        PublicKey kpub =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(kpubb));
        byte[] kprivb = Base64.getDecoder().decode(kprivs);
        PrivateKey kpriv =  KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(kprivb));
        KeyPair pair = new KeyPair(kpub,kpriv);
        return pair;
    }
    

    
    public byte[] cryptWithAes(byte[] data, SecretKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public String cryptWithAes(String data, SecretKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString( cipher.doFinal(Base64.getDecoder().decode(data)));
    }
    
    public byte[] decryptWithAes(byte[] data, SecretKey key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public String decryptWithAes(String data, SecretKey key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return Base64.getEncoder().encodeToString( cipher.doFinal(Base64.getDecoder().decode(data)));
    }
       
    public byte[] cryptWithRSA(byte[] data, PublicKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    	 Cipher cipher = Cipher.getInstance("RSA");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         return cipher.doFinal(data);
    }
    
    public String cryptWithRSA(String data, PublicKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
   	 Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(Base64.getDecoder().decode(data)));
   }
    public byte[] decryptWithRSA(byte[] data, PrivateKey key)throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
   	 Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
   }
   
    public String decryptWithAes(String data, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return Base64.getEncoder().encodeToString( cipher.doFinal(Base64.getDecoder().decode(data)));
    }


}
