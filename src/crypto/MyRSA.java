package crypto;


import java.io.*;
import java.math.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.util.Base64;
import java.util.*;
public class MyRSA {
	  public final static int KEY_SIZE = 2048;//[512..2048]
	  //TODO REMETTRE 1024 ou 2048

	  private RSAPublicKey publicKey;
	  private RSAPrivateKey privateKey;
	  
	  
	  public MyRSA() {
	  }

	 
	  public RSAPublicKey getPublicKey() {
	    return publicKey;
	  }
	  
	  
	  public byte[] getPublicKeyInBytes() {
	    return publicKey.getEncoded();
	  }
	  
	  
	  public RSAPrivateKey getPrivateKey() {
	    return privateKey;
	  }
	  

	  public byte[] getPrivateKeyInBytes() {
	    return privateKey.getEncoded();
	  }  
	  
	  
	  public void setPublicKey(RSAPublicKey publicKey) {
	    this.publicKey = publicKey;
	  }
	  
	  
	  public void setPublicKey(byte[] publicKeyData) {
	    try {
	      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
	      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	      publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
	    }
	    catch (Exception e) {System.out.println(e);} 
	  }
	  
	  
	  public void setPrivateKey(RSAPrivateKey privateKey) {
	    this.privateKey = privateKey;
	  }
	  
	  
	  public void setPrivateKey(byte[] privateKeyData) {
	    try {
	      PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
	      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	      privateKey = (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
	    }
	    catch (Exception e) {System.out.println(e);} 
	  }    
	    
	  
	  public void generateKeyPair() {
	    try {
	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
	      keyPairGen.initialize(KEY_SIZE, new SecureRandom());
	      KeyPair kp = keyPairGen.generateKeyPair();
	      publicKey = (RSAPublicKey)kp.getPublic();
	      privateKey = (RSAPrivateKey)kp.getPrivate();
	    }
	    catch (Exception e) {System.out.println(e);} 
	  }


	  public byte[] crypt(byte[] plaintext) {
	    return crypt(new BigInteger(addOneByte(plaintext))).toByteArray();
	  }
	  
	    
	  public byte[] crypt(String plaintext) {
	    return crypt(plaintext.getBytes());
	  }
	 
	 
	  public byte[] decryptInBytes(byte[] ciphertext) {
	    return removeOneByte(decrypt(new BigInteger(ciphertext)).toByteArray());
	  }    
	  
	  
	  public String decryptInString(byte[] ciphertext) {
	    return new String(decryptInBytes(ciphertext));
	  }

	  private BigInteger crypt(BigInteger plaintext) {
		    return plaintext.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
		  }
		  
		  
		  private BigInteger decrypt(BigInteger ciphertext) {
		    return ciphertext.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());
		  }            
		  

		  /**
		   * Ajoute un byte de valeur 1 au début du message afin d'éviter que ce dernier
		   * ne corresponde pas à un nombre négatif lorsqu'il sera transformé en
		   * BigInteger.
		   */
		  private  byte[] addOneByte(byte[] input) {
		    byte[] result = new byte[input.length+1];
		    result[0] = 1;
		    for (int i = 0; i < input.length; i++) {
		      result[i+1] = input[i];
		    }
		    return result;
		  }
		  
		  
		  /**
		   * Retire le byte ajouté par la méthode addOneByte.
		   */
		  private  byte[] removeOneByte(byte[] input) {
		    byte[] result = new byte[input.length-1];
		    for (int i = 0; i < result.length; i++) {
		      result[i] = input[i+1];
		    }
		    return result;
		  }
		

		  public byte[] dechiffrementRSAInByte(String key, String ciphertext)
		  {
				 byte[] keyInByte = Base64.getDecoder().decode(key);
				 byte[] ciphertextInByte = Base64.getDecoder().decode(ciphertext);
				 MyRSA rsa = new MyRSA();
				 rsa.setPrivateKey(keyInByte);
				 return rsa.decryptInBytes(ciphertextInByte);	
				// System.out.println(new String(rsa.decryptInBytes(ciphertextInByte))+"valeur en clair ");
		  }
		  
		  public String dechiffrementRSAInString(String key, String ciphertext)
		  {
				 byte[] keyInByte = Base64.getDecoder().decode(key);
				 byte[] ciphertextInByte = Base64.getDecoder().decode(ciphertext);
				 MyRSA rsa = new MyRSA();
				 rsa.setPrivateKey(keyInByte);
				 return rsa.decryptInString(ciphertextInByte);	
			}
}

