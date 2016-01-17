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
	
	private Map<String, String> keysMap; 
	private boolean isInitialized; 
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	public Map<String, String> getKeysMap() {
		return keysMap;
	}
	public void setKeysMap(Map<String, String> keysMap) {
		this.keysMap = keysMap;
	}
	public Set<String> getKeysList()
	{
		return keysMap.keySet();
	}
	
	
}
