package crypto;

import java.util.Map;

public class CryptoUtilsModel {

	/**
	 * @attribute 
	 * Current user private keys : 
	 * 	statusKey 
	 * 	assignementKey
	 */
	
	private Map<String, byte[]> keysMap; 
	private boolean isInitialized; 
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	public Map<String, byte[]> getKeysMap() {
		return keysMap;
	}
	public void setKeysMap(Map<String, byte[]> keysMap) {
		this.keysMap = keysMap;
	}
	
	
}
