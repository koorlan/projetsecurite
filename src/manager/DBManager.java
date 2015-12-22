package manager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataFormatter.*;
import generalizer.GeneralizerManager;
import generalizer.GeneralizerModel;
import generalizer.GsaList;

public class DBManager {

	private CoreManager core;
	
	private static String sql;
	private static boolean isFormatted; 
	private static long id;

	private final String PLUGIN = "org.sqlite.JDBC";
	
	private final String DB_PATH = "jdbc:sqlite:Initialisation/datas/";
	private final String DB_NAME = "F1.sqlite";

	public DBManager(CoreManager core)
	{
		this.core = core;
		sql = "";
		setFormatted(false); 
		id = 0; 
	}
	
	// no need group here 
	public void build(DataUtil du)
	{
		Pattern p = Pattern.compile("(/BEG/{1})([0-1]{1})::([0-6]+?)::([0-1]{1})::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::(/END/{1})");
		Matcher m = p.matcher(du.getData());
		
		/* Starting match */
		if(m.matches())
		{	
			setFormatted(true);
			/* Action (enum) */
			for(Action action : Action.values())
			{
				if(m.group(2).compareTo(action.getKey()) == 0)
				{
						sql += action.getValue() + " ";
				}
			}
			
			/* Table (enum) */
			for(Table table : Table.values())
			{
				if(m.group(4).compareTo(table.getKey()) == 0)
				{
						sql += "FROM " + table.getValue() + " ";
				}
			}
			
			p = Pattern.compile("([0-6]{1})");
			/* data type list matcher */ 
			Matcher tm = p.matcher(m.group(3));
			boolean flag = false;
			
			/* Data (enum) */
			sql += "WHERE ( l.RefBD = dc.RefBD AND l.ID_Cred = ct.ID_Cred AND ";
			
			while (tm.find()) 	
			{	
				if(flag)
					sql += "OR ";
				for(Type type : Type.values())
				{
					if(tm.group().compareTo(type.getKey()) == 0)
					{
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
		
			/* group list matcher */
			if(m.start(5) != -1) // check if the 5th group exists
			{
				sql += "AND ";
				Matcher gm = p.matcher(m.group(5));
				while(gm.find())
				{
					grpList.add((new GsaList(gm.group(), GeneralizerModel.getGroupTree())).getValue());
				}
			}
			/* status list matcher */
			if(m.start(6) != -1)
			{	
				Matcher sm = p.matcher(m.group(6));
				while(sm.find())
				{
					statList.add((new GsaList(sm.group(), GeneralizerModel.getStatusTree())).getValue());
				}
			}
			/* assignement list matcher */
			if(m.start(7) != -1)
			{	
				Matcher am = p.matcher(m.group(7));
				while(am.find())
				{
					assignementList.add((new GsaList(am.group(), GeneralizerModel.getAssignementTree())).getValue());
				}
			}
			/* format GSA list into sql request */
			build(grpList, statList, assignementList);
		
		} /* ENDIF */
	}
	
	public void build(ArrayList<String> groupList, ArrayList<String> statusList, ArrayList<String> assignementList)
	{	
		int i = 0;
		if(groupList.size() > 0)
		{	
			sql += "( dc.Affect_Gen = " + "'" + assignementList.get(0) +"'";
			for (i = 1 ; i < assignementList.size() ; i++) 
				sql += " OR dc.Affect_Gen = " + "'" + assignementList.get(i) + "'";	
			sql += ")"; 
		}
		if(statusList.size() > 0)
		{
			sql += " AND ( dc.Statut_Gen = " + "'" + statusList.get(0) + "'";
			for (i = 1 ; i < statusList.size() ; i++) 
				sql += " OR dc.Statut_Gen = " + "'"+ statusList.get(i) +"'";	
			sql += ")"; 
		} 
	}
	
	public String getSql()
	{
		return sql;
	}
	
	public long getId()
	{
		return id;
	}

	public ArrayList<String> search() throws ClassNotFoundException, SQLException
	{
		String url = DB_PATH+DB_NAME;
		
		Statement st = null;
		ArrayList<String> results = new ArrayList<String>();

		// load driver
		Class.forName(PLUGIN);
		
		// get connection
		java.sql.Connection cn = DriverManager.getConnection(url);
		
		// create statement 
		st = cn.createStatement();
		
		// execute "select" request
		System.out.println(sql+";");
		ResultSet rs = st.executeQuery(sql);
		
		// saving some interesting fields
		while(rs.next())
		{
			results.add(rs.getString(1));	// E_Cred_Ksec
			results.add(rs.getString(2));	// Cred_Auto_Ref
			results.add(rs.getString(3));	// Metadonnees (chiffrees)
			results.add(rs.getString(4));	// Valeur (chiffree)
		}
		
		cn.close();
		st.close();
	
		return results;
	}

	public void printResult(ResultSet rs) throws SQLException
	{
		// print some interesting fields
		while(rs.next())
		{
			System.out.println(rs.getString(1));	// E_Cred_Ksec
			System.out.println(rs.getString(2));	// Cred_Auto_Ref
			System.out.println(rs.getString(3));	// Meta_Chiffree
			System.out.println(rs.getString(4));	// Valeur_Chiffree
		}	
	}
	
	public boolean isFormatted() {
		return isFormatted;
	}

	public static void setFormatted(boolean isFormatted) {
		DBManager.isFormatted = isFormatted;
	}
}
