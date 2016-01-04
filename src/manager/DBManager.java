package manager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataFormatter.*;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import generalizer.GsaList;
import main.Frontal;
import main.User;
import model.FrontalModel;
import model.ServerModel;
import model.UserModel;

public class DBManager {

	private CoreManager core;

	private static String sql;
	private static boolean isFormatted;
	private static long id;

	private final String PLUGIN = "org.sqlite.JDBC";

	private final String DB_PATH = "jdbc:sqlite:Initialisation/datas/";

	private String DB_INFO = null;
	private String DB_DATA = null;
	
	public String getDB_INFO(){
		return this.DB_INFO;
	}
	public String getDB_PATH(){
		return this.DB_PATH;
	}
	
	public DBManager(CoreManager core) {
		this.core = core;
		sql = "";
		setFormatted(false);
		id = 0;
	}
	
	public void build(DataUtil du, ArrayList<String> policy) {
		switch (this.core.getService()) {
		case "frontal":
			this.buildFrontal(du, policy);
			break;
		case "user":
			this.buildUser(du, policy);
			break;
		default:
			break;
		}
	}

	private void buildFrontal(DataUtil du, ArrayList<String> policy) {
		// TODO : <FIX> no need group here clear goups group in pattern
		Pattern p = Pattern.compile(
				"(/BEG/{1})([0-1]{1})::([0-6]+?)::([0-1]{1})::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::(/END/{1})");
		Matcher m = p.matcher(du.getData());

		/* Starting match */
		if (m.matches()) {
			setFormatted(true);
			/* Action (enum) */
			for (Action action : Action.values()) {
				if (m.group(2).compareTo(action.getKey()) == 0) {
					sql += action.getValue() + " ";
				}
			}

			/* Table (enum) */
			for (Table table : Table.values()) {
				if (m.group(4).compareTo(table.getKey()) == 0) {
					sql += "FROM " + table.getValue() + " ";
				}
			}

			p = Pattern.compile("([0-6]{1})");
			/* data type list matcher */
			Matcher tm = p.matcher(m.group(3));
			boolean flag = false;

			/* Data (enum) */
			sql += "WHERE ( l.RefBD = dc.RefBD AND l.ID_Cred = ct.ID_Cred AND ";

			while (tm.find()) {
				if (flag)
					sql += "OR ";
				for (Type type : Type.values()) {
					if (tm.group().compareTo(type.getKey()) == 0) {
						sql += "dc.Type = '" + type.getValue() + "' ";
						flag = true;
					}

				}
			}
			sql += " ) ";

			p = Pattern.compile("([A-Z]{1}[1-9]{1,})");
			ArrayList<String> grpList = new ArrayList<String>();
			ArrayList<String> statList = new ArrayList<String>();
			ArrayList<String> assignementList = new ArrayList<String>();
			System.out.println(m.group());
			
			//TODO <FIX> no need groups here
			/* group list matcher */
			if (m.start(5) != -1) // check if the 5th group exists
			{
				sql += "AND ";
				Matcher gm = p.matcher(m.group(5));
				while (gm.find()) {
					grpList.add((new GsaList(gm.group(), GeneralizerModel.getGroupTree())).getValue());
				}
			}
			/* status list matcher */
			if (m.start(6) != -1) {
				Matcher sm = p.matcher(m.group(6));
				while (sm.find()) {
					statList.add((new GsaList(sm.group(), GeneralizerModel.getStatusTree())).getValue());
				}
			}
			
			/* assignement list matcher */
			if (m.start(7) != -1) {
				Matcher am = p.matcher(m.group(7));
				while (am.find()) {
					assignementList.add((new GsaList(am.group(), GeneralizerModel.getAssignementTree())).getValue());
				}
			}
			/* format GSA list into sql request */
			build(grpList, statList, assignementList);
			buildPolicyFrontal(policy);
			
		} /* ENDIF */

	}

	private void buildUser(DataUtil du, ArrayList<String> policy) {
		//TODO : <FIX> No need groups group
		Pattern p = Pattern.compile(
				"(/BEG/{1})([0-1]{1})::([0-6]+?)::([0-1]{1})::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::(/END/{1})");
		Matcher m = p.matcher(du.getData());
		/* Starting match */
		if (m.matches()) {
			setFormatted(true);
			sql += " ct.E_Cred_Ksec, ct.Cred_Auto_Ref, Types.Meta_Chiffrees, dc.Valeur_Chiffree "
					+ "FROM Donnees_Chiffrees AS dc, Cles_Types AS ct, Utilisateur as u, Types ";
			
			p = Pattern.compile("([0-6]{1})");
			
			Matcher tm = p.matcher(m.group(3));
			boolean flag = false;

			sql += "WHERE ( dc.Type = ct.Type ";

			while (tm.find()) {
				if (flag)
					sql += "OR ";
				for (Type type : Type.values()) {
					if (tm.group().compareTo(type.getKey()) == 0) {
						sql += "dc.Type = '" + type.getValue() + "' ";
						flag = true;
					}

				}
			}
			sql += " ) ";

			p = Pattern.compile("([A-Z]{1}[1-9]{1,})");
			ArrayList<String> grpList = new ArrayList<String>();
			ArrayList<String> statList = new ArrayList<String>();
			ArrayList<String> assignementList = new ArrayList<String>();
			System.out.println(m.group());
			
			//TODO <FIX> no need groups here
			/* group list matcher */
			if (m.start(5) != -1) // check if the 5th group exists
			{
				sql += "AND ";
				Matcher gm = p.matcher(m.group(5));
				while (gm.find()) {
					grpList.add((new GsaList(gm.group(), GeneralizerModel.getGroupTree())).getValue());
				}
			}
			/* status list matcher */
			if (m.start(6) != -1) {
				Matcher sm = p.matcher(m.group(6));
				while (sm.find()) {
					statList.add((new GsaList(sm.group(), GeneralizerModel.getStatusTree())).getValue());
				}
			}
			
			/* assignement list matcher */
			if (m.start(7) != -1) {
				Matcher am = p.matcher(m.group(7));
				while (am.find()) {
					assignementList.add((new GsaList(am.group(), GeneralizerModel.getAssignementTree())).getValue());
				}
			}
			/* format GSA list into sql request */
			int i = 0;
			if (assignementList.size() > 0) {
				sql += " AND ( u.Affectation_Gen = " + "'" + assignementList.get(0) + "'";
				for (i = 1; i < assignementList.size(); i++)
					sql += " OR u.Affectation_Gen = " + "'" + assignementList.get(i) + "'";
				sql += ")";
			}
			if (statList.size() > 0) {
				sql += " AND ( u.Statut_Gen = " + "'" + statList.get(0) + "'";
				for (i = 1; i < statList.size(); i++)
					sql += " OR u.Statut_Gen = " + "'" + statList.get(i) + "'";
				sql += ")";
			}
			buildPolicyUser(policy);
		} /* ENDIF */
	}
	
	public void buildPolicyUser(ArrayList<String> policy)
	{	
		if(policy.size() > 0)
		{
			sql += "AND ( dc.Type = Types.Type AND ( ";
			for(int i = 0; i < policy.size(); i++)
			{
				sql += "ct.Cred_Auto_Ref = '" + policy.get(i) + "' ";
				sql += (i == policy.size() - 1 ? "" : "OR ");
			}
			sql += ") )";
		}
	}

	public void buildPolicyFrontal(ArrayList<String> policy)
	{	
		if(policy.size() > 0)
		{
			sql += "AND ( ";
			for(int i = 0; i < policy.size(); i++)
			{
				sql += "ct.Cred_Auto_Ref = '" + policy.get(i) + "' ";
				sql += (i == policy.size() - 1 ? "" : "OR ");
			}
			sql += ") ";
		}
	}
	
	public void build(ArrayList<String> groupList, ArrayList<String> statusList, ArrayList<String> assignementList) {
		int i = 0;
		if (assignementList.size() > 0) {
			sql += " AND ( dc.Affect_Gen = " + "'" + assignementList.get(0) + "'";
			for (i = 1; i < assignementList.size(); i++)
				sql += " OR dc.Affect_Gen = " + "'" + assignementList.get(i) + "'";
			sql += ")";
		}
		if (statusList.size() > 0) {
			sql += " AND ( dc.Statut_Gen = " + "'" + statusList.get(0) + "'";
			for (i = 1; i < statusList.size(); i++)
				sql += " OR dc.Statut_Gen = " + "'" + statusList.get(i) + "'";
			sql += ")";
		}
	}

	public String getSql() {
		return sql;
	}

	public long getId() {
		return id;
	}

	public ArrayList<String> search() throws ClassNotFoundException, SQLException {
		switch (this.core.getService()) {
		case "frontal":
			return this.searchFrontal();
		case "user":
			return this.searchUser();
		default:
			return null;
		}
	}

	// TODO not implemented
	public ArrayList<String> searchUser() throws ClassNotFoundException, SQLException {
		return null;
	};

	public ArrayList<String> searchFrontal() throws ClassNotFoundException, SQLException {
		String url = DB_PATH + DB_INFO;

		Statement st = null;
		ArrayList<String> results = new ArrayList<String>();

		// load driver
		Class.forName(PLUGIN);

		// get connection
		java.sql.Connection cn = DriverManager.getConnection(url);
		// create statement
		st = cn.createStatement();

		// execute "select" request
		System.out.println(sql + ";");
		ResultSet rs = st.executeQuery(sql);

		// saving some interesting fields
		while (rs.next()) {
			results.add(rs.getString(1)); // E_Cred_Ksec
			results.add(rs.getString(2)); // Cred_Auto_Ref
			results.add(rs.getString(3)); // Metadonnees (chiffrees)
			results.add(rs.getString(4)); // Valeur (chiffree)
		}

		sql = "";
		cn.close();
		st.close();

		return results;
	}

	public void printResult(ResultSet rs) throws SQLException {
		// print some interesting fields
		while (rs.next()) {
			System.out.println(rs.getString(1)); // E_Cred_Ksec
			System.out.println(rs.getString(2)); // Cred_Auto_Ref
			System.out.println(rs.getString(3)); // Meta_Chiffree
			System.out.println(rs.getString(4)); // Valeur_Chiffree
		}
	}
	
	public boolean isFormatted() {
		return isFormatted;
	}

	public static void setFormatted(boolean isFormatted) {
		DBManager.isFormatted = isFormatted;
	}
	 

	public void setDB_INFO(String dB_INFO) {
		this.DB_INFO = dB_INFO;
	}

	public void setDB_DATA(String dB_DATA) {
		DB_DATA = dB_DATA;
	}

	public void initialize() throws ClassNotFoundException, SQLException {
		// load driver
		Class.forName(PLUGIN);
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		// create statement
		Statement st = cn.createStatement();
		ResultSet rs = null;
		switch (this.core.getService()) {
		case "central":
			// read ip/port from DB
			rs = st.executeQuery("SELECT IP,Port FROM Server");
			this.core.getServer().setIp(rs.getString("IP"));
			this.core.getServer().setPort(rs.getInt("Port"));
			// populate Fronts to broadcast
			rs = st.executeQuery("SELECT * FROM Frontales");
			while (rs.next()) {
				ServerModel srv = new ServerModel();
				srv.setIpDest(rs.getString("IP"));
				srv.setPort(rs.getInt("externalPort"));
				ServerManager srvMan = new ServerManager(srv, null);

				// Dirty 2nd argument
				FrontalModel frontal = new FrontalModel(rs.getString("Famille"), rs.getString("Frontale"), srvMan,
						srvMan);
				this.core.getBroadcast().addFrontal(frontal);
			}
			break;
		case "user":
			// read name/ip/port from DB
			rs = st.executeQuery("SELECT * FROM Utilisateurs");
			this.core.getServer().setIp(rs.getString("IP"));
			this.core.getServer().setPort(rs.getInt("Port"));
			this.core.getUser().setName(rs.getString("Login"));
			break;
		case "frontal":
			// set frontal info
			rs = st.executeQuery("SELECT * FROM Frontale");
			this.core.getFrontal().setFamilly(rs.getString("Famille"));
			this.core.getFrontal().setName(rs.getString("Frontale"));
			// this.core.getFrontal.setIp(rs.getString("IP"));
			this.core.getFrontal().setInternalPort(rs.getInt("internalPort"));
			this.core.getFrontal().setExternalPort(rs.getInt("externalPort"));
			break;

		}
	}

	public String getFrontalIP() throws ClassNotFoundException, SQLException {
		Class.forName(PLUGIN);

		ResultSet rs = null;
		java.sql.Connection cn = null;
		Statement st = null;
		switch (this.core.getService()) {
		case "frontal":
			cn = DriverManager.getConnection(DB_PATH + DB_INFO);
			st = cn.createStatement();
			rs = st.executeQuery("SELECT * FROM Frontale");
			return rs.getString("IP");
		case "user":
			cn = DriverManager.getConnection(DB_PATH + DB_INFO);
			st = cn.createStatement();
			rs = st.executeQuery("SELECT * FROM Frontales");
			return rs.getString("IP");
		default:
			break;
		}
		return null;
	}

	public int getFrontalPort() throws ClassNotFoundException, SQLException {
		Class.forName(PLUGIN);

		ResultSet rs = null;
		java.sql.Connection cn = null;
		Statement st = null;
		switch (this.core.getService()) {
		case "user":
			cn = DriverManager.getConnection(DB_PATH + DB_INFO);
			st = cn.createStatement();
			rs = st.executeQuery("SELECT * FROM Frontales");
			return rs.getInt("internalPort");
		default:
			break;
		}
		return -1;
	}

	public ArrayList<User> getUsers() throws Exception {
		// load driver
		Class.forName(PLUGIN);
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		// create statement
		Statement st = cn.createStatement();
		ResultSet rs = null;
		switch (this.core.getService()) {
		case "frontal":
			rs = st.executeQuery("SELECT * FROM Utilisateurs");
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()) {
				User user = new User();
				user.getCore().getServer().setIp(rs.getString("IP"));
				user.getCore().getServer().setPort(rs.getInt("Port"));
				user.getCore().getUser().setStatus(rs.getString("Statut_Gen"));
				user.getCore().getUser().setAssignement(rs.getString("Affect_Gen"));
				users.add(user);
			}
			return users;
		default:
			break;
		}
		return null;
	}

	public String getCentralIP() throws ClassNotFoundException, SQLException {
		// load driver
		Class.forName(PLUGIN);
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		// create statement
		Statement st = cn.createStatement();
		ResultSet rs = null;
		switch (this.core.getService()) {
		case "frontal":
			rs = st.executeQuery("SELECT * FROM Server");
			return rs.getString("IP");
		default:
			break;
		}
		return null;
	}

	public int getCentralPort() throws SQLException, ClassNotFoundException {
		// load driver
		Class.forName(PLUGIN);
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		// create statement
		Statement st = cn.createStatement();
		ResultSet rs = null;
		switch (this.core.getService()) {
		case "frontal":
			rs = st.executeQuery("SELECT * FROM Server");
			return rs.getInt("Port");
		default:
			break;
		}
		return -1;
	}

	public ArrayList<Frontal> getFrontals() throws Exception {
		// load driver
		Class.forName(PLUGIN);
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		// create statement
		Statement st = cn.createStatement();
		ResultSet rs = null;
		switch (this.core.getService()) {
		case "frontal":
			rs = st.executeQuery("SELECT * FROM Frontales");
			ArrayList<Frontal> frontals = new ArrayList<Frontal>();
			while(rs.next()){
				String ip = rs.getString("IP");
				int intP = rs.getInt("internalPort");
				int extP = rs.getInt("externalPort");
				
				Frontal frontal = new Frontal();
				frontal.getCore().getFrontal().setFamilly(rs.getString("Famille"));
				frontal.getCore().getFrontal().setName(rs.getString("Frontale"));
				frontal.getCore().getFrontal().getExternalserverManager().setIp(ip);
				frontal.getCore().getFrontal().getInternalserverManager().setIp(ip);
				frontal.getCore().getFrontal().getInternalserverManager().setPort(intP);
				frontal.getCore().getFrontal().getExternalserverManager().setPort(extP);
				
				frontals.add(frontal);
			}
			return frontals;
			
		default:
			break;
		}
		return null;
	}
	
	public List<String> buildKeysList() throws Exception
	{
		List<String> result = new ArrayList<String>();
		List<String> status = new ArrayList<String>();
		List<String> assignement = new ArrayList<String>();
		int statID = -1;
		int assignID = -1;

		Class.forName(PLUGIN);
		java.sql.Connection cn = DriverManager.getConnection(DB_PATH + DB_INFO);
		Statement st = cn.createStatement();
		ResultSet rs = null;
		
		if(this.core.getService() != "user")
		{
			this.core.getLog().warn(this, "Acces to database refused");
			return result;
		}
		
		rs = st.executeQuery("SELECT Statuts.ID_Cle, Utilisateurs.ID_Statut, "
				+ "Affectations.ID_Cle, Utilisateurs.ID_Affectation "
				+ "FROM Statuts, Affectations, Utilisateurs "
				+ "WHERE Utilisateurs.ID_Statut = Statuts.ID_Statut "
				+ "AND Utilisateurs.ID_Affectation = Affectations.ID_Affectation");
		while(rs.next())
		{
			status.add(rs.getString("Statuts.ID_Cle"));
			assignement.add(rs.getString("Affectations.ID_Cle"));
			statID = rs.getInt("Utilisateurs.ID_Statut");
			assignID = rs.getInt("Utilisateurs.ID_Affectation");
		}
		rs.close();
		rs = null;
		
		// adding random credentials 
		Random rand = new Random();
		int n, m;
		do{
			n = rand.nextInt(17 - 1 + 1) + 1;
		}while(n == statID);
		do{
			m = rand.nextInt(8 - 1 + 1) + 1;
		}while(m == assignID);
		
		rs = st.executeQuery("SELECT Statuts.ID_Cle, Affectations.ID_Cle "
				+ "FROM Statuts, Affectations "
				+ "WHERE Statuts.ID_Statut = " +  n
				+ " AND Affectations.ID_Affectation = " + m);
		while(rs.next())
		{
			status.add(rs.getString("Statuts.ID_Cle"));
			assignement.add(rs.getString("Affectation.ID_Cle"));
		}
		rs.close();
		st.close();
		
		Collections.sort(status);
		Collections.sort(assignement);
		result.addAll(status);
		result.addAll(2,assignement);
		
		// TODO : <DEBUG> clean this soon
		System.out.println(result);
		
		return result;
	}
}
