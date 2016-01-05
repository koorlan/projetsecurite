package crypto;

import java.sql.SQLException;
import java.util.Map;

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
		this.core.getDB().setKeys();
		this.model.setInitialized(true); 
	}
}
