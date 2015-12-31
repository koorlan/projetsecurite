package manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import dataFormatter.DataUtil;
import filter.FilterManager;
import filter.FilterModel;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import model.PacketModel;
import model.RequestModel;
import response.ResponseManager;
import response.ResponseModel;

public class RequestManager {
	
	private CoreManager core;
	private RequestModel model;
	
	public RequestManager(RequestModel model,CoreManager core){
		super();
		this.core = core;
		this.model = model;
	}
	
	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP]
	 * Requirements : DBManager which interprets the request formatted content and ensures a database connection 
	 * 
	 * @param request	A de-serialized request, which contains the dataUtil to process
	 * 
	 */
	public byte[] process(RequestModel request) throws ClassNotFoundException, SQLException
	{
		ArrayList<String> results = new ArrayList<String>();
		this.core.getDB().build(request.getDu());
		if(this.core.getDB().isFormatted())
		{

			results = this.core.getDB().search();
			System.out.println(results);
			
			//here forge POST to return...
			this.forgeResponse(results);
			//this.core.getPacket().forge("POST", "ANSWER");
			return null;
		}
		else
			this.core.getLog().err(this, "Non formatted content");
		return null;
	}	

	/**
	 * Method called by : [USER'S LOCAL APP]
	 * Requirements:	ResponseManager which interprets the response formatted content
	 * 				 	FilterManager which filter the answer 
	 * @param reponse	A de-serialized response, which contains the dataUtil to process (filter, trash, print, store...)
	 * 
	 */
	public void processResponse(RequestModel response) throws ClassNotFoundException, SQLException
	{	
		ResponseModel responseM = new ResponseModel();
		ResponseManager rManager = new ResponseManager(responseM, this.core);	
		if(rManager.isResponse(response.getDu()))
		{	
			if(rManager.isEmpty(response.getDu()))
			{
				//TODO : 
					// Decipher E_Cred_Ksec with credential OR trash packet
					// Use Ksec to decipher metadatas + value
					// Use metadatas to filter packet (trash it, or keep it)
					// Print results 
			}
			else
			{
				this.core.getLog().warn(this, "Empty response received");
			}
		}
		else
		{
			this.core.getLog().err(this, "Response received with unexpected format");
			rManager.trash(response);
			return;
		}
		
		//this.core.getFilter().model.setResponse(ArrayList<String> response);
	}	

	/**
	 * Method called by : [USER'S LOCAL APP]
	 * Requirements : a packet manager for serialization
	 * 
	 * @param request	A non-serialized request, which contains the dataUtil to SEND 
	 * @param port 		The port number that is used for sending request 		
	 * @throws IOException 
	 * 
	 */
	private void send(RequestModel request) throws ClassNotFoundException, SQLException, IOException{
		Socket socket;
		socket = this.core.getPacket().sendPacket(this.core.getPacket().forge("GET",request),this.core.getDB().getFrontalIP(),this.core.getDB().getFrontalPort());
		InputStream inputStream = socket.getInputStream();
		int count;
		byte[] packet = new byte[0];
		byte[] buffer = new byte[1024];
		
		long startTime = System.currentTimeMillis(); //fetch starting time
		while(false||(System.currentTimeMillis()-startTime)<3000)
		{
			count = inputStream.read(buffer);
			packet = ArrayUtils.addAll(packet, buffer);
			if(count != -1)
				System.out.println(count);
		}
	}
	
	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP] 
	 * Requirements : a packet manager for serialization
	 * 
	 * @param response	A non-serialized response, which contains the dataUtil to SEND 
	 * 
	 */
	private void sendResponse(RequestModel response) throws ClassNotFoundException, SQLException
	{
		//User case
		this.core.getPacket().sendPacket(this.core.getPacket().forge("POST", response),this.core.getDB().getFrontalIP(),this.core.getDB().getFrontalPort());
	}
	
	/**
	 * Method called by : [USER'S LOCAL APP]
	 * Requirements : 	a Filter to preserve real request
	 * 					a Generalizer to add noise in data 
	 * 					a DataUtil to apply a generic request format 
	 * 
	 * @param type			Search data type (filled by user)
	 * @param group			Search group (filled by user)
	 * @param status		Search status (filled by user)
	 * @param assignement	Search assignement (filled by user) 
	 * @param port			The port number that is used for sending request 
	 * @throws IOException 
	 * 
	 */
	public void forge(Object type, Object group, Object status, Object assignement) throws ClassNotFoundException, SQLException, IOException
	{
		if(!(type instanceof String && group instanceof String && status instanceof String && assignement instanceof String))
		{
			this.core.getLog().err(this, "Wrong fields");
		}
		
		FilterModel filterM = new FilterModel();		
		FilterManager filter = new FilterManager(filterM, this.core);
		
		GeneralizerModel genM = new GeneralizerModel();
		GeneralizerManager genManager = new GeneralizerManager(genM, this.core);			
		
		ArrayList<String> groupList = new ArrayList<String>();
		ArrayList<String> statusList = new ArrayList<String>();
		ArrayList<String> assignementList = new ArrayList<String>();
		
		groupList.add((String) group);
		statusList.add((String) status);
		assignementList.add((String) assignement);	
	
		filterM.setGroupList(genManager.generalize(groupList,"group"));
		filterM.getGroupList().printGsaList();
		filterM.setStatusList(genManager.generalize(statusList,"status"));
		filterM.getStatusList().printGsaList();
		filterM.setAssignementList(genManager.generalize(assignementList,"assignement"));
		filterM.getAssignementList().printGsaList();
		
		DataUtil du = new DataUtil();		
		du.setAction("QUERY");		
		du.setType((String)type);		
		//TODO : <MAJ> dissociate FRONT and USER tables OR unify the tables layout in DB 
		du.setTable("FRONT_TABLE");			
		du.setGSA(filterM.getGroupList().getMainKeyList());
		du.setGSA(filterM.getStatusList().getMainKeyList());
		du.setGSA(filterM.getAssignementList().getMainKeyList());
		du.close();
		//TODO : <DEBUG> clean that later 
		System.out.println(du);
		
		RequestModel tosend = new RequestModel();
		tosend.setDu(du);
		this.send(tosend);
	}

	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP]
	 * Requirements : a DataUtil to apply a generic answer format 
	 * 
	 * @param result	The result from a SQLite request (can be empty)
	 *  		
	 */
	public RequestModel forgeResponse(ArrayList<String> results) throws ClassNotFoundException, SQLException
	{
		DataUtil du = new DataUtil();
		du.setAction("ANSWER");
		if(results.isEmpty())
		{
			du.setContent("TRASH");
		}
		else
		{
			du.setContent("FULL");
			du.setResults(results);
		}
		du.close();
		RequestModel toSend = new RequestModel();
		toSend.setDu(du);
		return toSend;
	}
}
