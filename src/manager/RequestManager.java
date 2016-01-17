package manager;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import anonymizer.DataHeaderModel;
import anonymizer.DataHeaderManager;
import dataFormatter.DataUtil;
import dialog.DialogWindow;
import filter.FilterManager;
import filter.FilterModel;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import initialisation_BD.TestCipher;
import model.PacketModel;
import model.RequestModel;
import response.ResponseManager;
import response.ResponseModel;

public class RequestManager {

	private CoreManager core;
	private RequestModel model;

	public RequestManager(RequestModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}

	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP] Requirements : a
	 * DBManager which interprets the request formatted content, extracts public
	 * keys, displays combination and ensures a database connection
	 * 
	 * @param request
	 *            A de-serialized request, which contains the dataUtil to
	 *            process
	 * 
	 */
	public RequestModel process(RequestModel request) throws ClassNotFoundException, SQLException {
		this.core.getLog().log(this, "New request in request to process");
		ArrayList<String> policy = this.core.getDataHeader().combines(request.getHeader());
		System.out.println("process combined list " + policy);
		this.core.getDB().build(request.getDu(), policy);
		if (this.core.getDB().isFormatted()) {
			HashMap<String, byte[][]> results = this.core.getDB().search();
			// TODO : clean this
			System.out.println(results);
			ArrayList<String> resultsRef = new ArrayList<String>();
			ArrayList<byte[]> resultsCipher = new ArrayList<byte[]>();
			for(Entry<String, byte[][]> result: results.entrySet()){
				resultsRef.add(result.getKey());
				resultsCipher.add(result.getValue()[0]);
				resultsCipher.add(result.getValue()[1]);
				resultsCipher.add(result.getValue()[2]);
				
			}
			return this.forgeResponse(resultsRef,resultsCipher);
			// this.core.getPacket().forge("POST", "ANSWER");
		} else
			this.core.getLog().err(this, "Non formatted content, answer will not be sent at this point");
		return null;
	}

	/**
	 * Method called by : [USER'S LOCAL APP] Requirements: a ResponseManager
	 * which interprets the response formatted content a FilterManager which
	 * filter the answer
	 * 
	 * @param reponse
	 *            A de-serialized response, which contains the dataUtil to
	 *            process (filter, trash, print, store...)
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * 
	 */
	public void processResponse(RequestModel response)
			throws ClassNotFoundException, SQLException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		ResponseModel responseM = new ResponseModel();
		ResponseManager rManager = new ResponseManager(responseM, this.core);
		if (rManager.checkFormat(response.getDu()) == true) {
			this.core.getLog().log(this, "New response accepted");
			// Apply first filter on credentials
			boolean[] tmpResRef = this.core.getDataHeader().checkPolicy(response.getResultRef());

			if (tmpResRef == null) {
				this.core.getLog().log(this, "First filter failed");
				rManager.trash(response);
				return;
			}

			byte[] plainK = null;
			String plainMeta = null;
			//System.out.println(response.getResultCipher());
			for (int i = 0; i < tmpResRef.length; i++) {
				if (!tmpResRef[i])
					continue;
				// TODO wait for decipher function
				plainK = this.core.getCryptoUtils().decipher(response.getResultCipher().get(i*3),
						response.getResultRef().get(i));
				if (plainK != null) {
					// plainMeta = this.core.getCryptoUtils()
					// .decipher(tmpRes.get(i + 2), plainK);

					SecretKey myKey = TestCipher.decodeAES_KEY(plainK);

					// System.out.println(myKey.getEncoded().length);
					 plainMeta =new String(TestCipher.decryptWithAes(response.getResultCipher().get(i*3+1),myKey) );
					 System.out.println(plainMeta);
						List<String> responseList = Arrays.asList(plainMeta.split(","));
						ArrayList<String> responseFilter = new ArrayList<String>();
						responseFilter.addAll(responseList); 
					
						this.core.getFilter().getModel().setResponse(responseFilter);
						if(this.core.getFilter().isSuitable()){
							String plainData = new String(TestCipher.decryptWithAes(response.getResultCipher().get(i*3+2),myKey) );
							System.out.println(plainData);
						}		
					// TODO : continue here
					// à ce stade on est sensés avoir une clé secrète dans
					// plainK

					// et les méta déchiffrées dans plain Meta
					// filtrer les réponses sur ces métadonnées
				} else {
					this.core.getLog().err(this, "Public Key deciphering failed");
				}
			}
		} else {
			this.core.getLog().warn(this, "Response received with unexpected format (could be empty)");
			rManager.trash(response);
			return;

		}

		// TODO :
		// Decipher E_Cred_Ksec with credential OR trash packet
		// Use Ksec to decipher metadatas + value
		// Use metadatas to filter packet (trash it, or keep it)
		// Print results
		System.out.println("Processing..(to be implemented in RequestManager ~line91");
		System.out.println("DEBUG >> " + response.getDu().getData());
		/**
		 * TODO : later this.core.getDialog().addResponse("nom", "type",
		 * "affectation", "statut", "groupe",response.getDu().getData());
		 */

		// this.core.getFilter().model.setResponse(ArrayList<String> response);
	}

	/**
	 * Method called by : [USER'S LOCAL APP] Requirements : a packet manager for
	 * serialization
	 * 
	 * @param request
	 *            A non-serialized request, which contains the dataUtil to SEND
	 * @param port
	 *            The port number that is used for sending request
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * 
	 */
	private void send(RequestModel request)
			throws ClassNotFoundException, SQLException, IOException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		// TODO clean debug
		System.out.println("Request forged " + request.getDu() + request.getHeader());
		Socket socket;
		socket = this.core.getPacket().sendPacket(this.core.getPacket().forge("GET", request),
				this.core.getDB().getFrontalIP(), this.core.getDB().getFrontalPort());
		if (socket == null)
			return;
		// Clear window
		DialogWindow dialog = this.core.getDialog();
		if (dialog != null) {
			dialog.refresh();
		}

		socket.setSoTimeout(1000);
		InputStream inputStream = socket.getInputStream();
		DataInputStream dis = new DataInputStream(inputStream);

		int len;

		long startTime = System.currentTimeMillis(); // fetch starting time
		while ((false || (System.currentTimeMillis() - startTime) < 10000)) {
			try {
				len = dis.readInt();
				if (len > 0) {
					System.out.println(len);
					byte[] packet = new byte[len];
					dis.readFully(packet, 0, packet.length);
					PacketModel Dpacket = (PacketModel) SerializationUtils.deserialize(packet);
					RequestModel response = (RequestModel) SerializationUtils.deserialize(Dpacket.getContent());
					this.processResponse(response);
				}
			} catch (EOFException e) {
				// System.out.print("wait>");
			} catch (SocketTimeoutException e) {
				continue;
			}
		}
		System.out.println("Finished Waiting answer");
	}

	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP] Requirements : a packet
	 * manager for serialization
	 * 
	 * @param response
	 *            A non-serialized response, which contains the dataUtil to SEND
	 * 
	 */
	private void sendResponse(RequestModel response) throws ClassNotFoundException, SQLException {
		// User case
		this.core.getPacket().sendPacket(this.core.getPacket().forge("POST", response),
				this.core.getDB().getFrontalIP(), this.core.getDB().getFrontalPort());
	}

	/**
	 * Method called by : [USER'S LOCAL APP] Requirements : a Filter to preserve
	 * real request a Generalizer to add noise in data a DataUtil to apply
	 * generic request format
	 * 
	 * @param type
	 *            Search data type (filled by user)
	 * @param group
	 *            Search group (filled by user)
	 * @param status
	 *            Search status (filled by user)
	 * @param assignement
	 *            Search assignement (filled by user)
	 * @param port
	 *            The port number that is used for sending request
	 * @throws Exception
	 * 
	 */
	public void forge(Object type, Object group, Object status, Object assignement) throws Exception {
		if (!(type instanceof String && group instanceof String && status instanceof String
				&& assignement instanceof String)) {
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

		this.core.getFilter().getModel().setGroupList(genManager.generalize(groupList, "group"));
		// filterM.getGroupList().printGsaList();
		this.core.getFilter().getModel().setStatusList(genManager.generalize(statusList, "status"));
		// filterM.getStatusList().printGsaList();
		this.core.getFilter().getModel().setAssignementList(genManager.generalize(assignementList, "assignement"));
		// filterM.getAssignementList().printGsaList();

		DataUtil du = new DataUtil();
		du.setAction("QUERY");
		du.setType((String) type);
		du.setTable("FRONT_TABLE");
		du.setGSA(this.core.getFilter().getModel().getGroupList().getMainKeyList());
		du.setGSA(this.core.getFilter().getModel().getStatusList().getMainKeyList());
		du.setGSA(this.core.getFilter().getModel().getAssignementList().getMainKeyList());
		du.close();
		// TODO : <DEBUG> clean that later
		System.out.println("Data Util forged : " + du);

		RequestModel tosend = new RequestModel();
		tosend.setDu(du);
		this.addHeader(tosend);
	}

	/**
	 * Method called by : [USER'S LOCAL APP] Requirements : DataHeaderManager,
	 * DataHeaderModel to build and store header
	 * 
	 * @param request
	 *            a RequestModel with DataUtil completed
	 * @throws Exception
	 */
	public void addHeader(RequestModel request) throws Exception {
		this.core.getLog().warn(this, "Building subheader");

		// getting a list of public keys (true and false)
		if (this.core.getDataHeader().getKeysList() == 0) {
			// creating and set formatted header to request model
			request.setHeader(this.core.getDataHeader().getDataM());
		} else
			this.core.getLog().err(this, "Public keys list builder failed");
		// TODO : clean
		System.out.println("Header forged :" + request.getHeader());
		this.send(request);
	}

	/**
	 * Method called by : [FRONTAL] [USER'S LOCAL APP] Requirements : a DataUtil
	 * to apply a generic answer format
	 * 
	 * @param result
	 *            The result from a SQLite request (can be empty)
	 * 
	 */
	public RequestModel forgeResponse(ArrayList<String> resultsRef,ArrayList<byte[]> resultsCipher) throws ClassNotFoundException, SQLException {
		System.out.println("Forging response");
		DataUtil du = new DataUtil();
		du.setAction("ANSWER");
		if (resultsRef.isEmpty()) {
			du.setContent("TRASH");
		} else {
			du.setContent("FULL");
		}
		du.close();
		RequestModel toSend = new RequestModel();
		toSend.setDu(du);
		toSend.setResultRef(resultsRef);
		toSend.setResultCipher(resultsCipher);
		System.out.println("du sended: " + toSend.getDu());
		// OK
		// System.out.println("result sended: "+ toSend.getResult());
		return toSend;
	}
}
