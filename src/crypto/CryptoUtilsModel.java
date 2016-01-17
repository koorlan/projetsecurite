package crypto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public Set<String> getKeysList()
	{
		return keysMap.keySet();
	}
	
	
}
