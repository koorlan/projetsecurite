package manager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.Connection;

import dataFormatter.*;

public class DBManager {

	private static String sql;
	private static boolean isFormatted; 
	private static long id;

	private final String DB_NAME = "testjava";
	
	private final String LOGIN = "root";
	private final String PASSWD = "operator";

	public DBManager()
	{
		sql = "";
		setFormatted(false); 
		id = 0; 
	}
	
	public void build(DataUtil du)
	{
		Pattern p = Pattern.compile("(/BEG/{1})([0-1]{1})::([0-3]+?)::([0-1]{1})::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::([[A-Z][1-9]{1,}]{1,})?::(/END/{1})");
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
			
			p = Pattern.compile("([0-3]{1})");
			/* data type list matcher */ 
			Matcher tm = p.matcher(m.group(3));
			boolean flag = false;
			
			/* Data (enum) */
			sql += "WHERE ( ";
			
			while (tm.find()) 	
			{	
				if(flag)
					sql += "OR ";
				for(Type type : Type.values())
				{
					if(tm.group().compareTo(type.getKey()) == 0)
					{
							sql += "type = '" + type.getValue() + "' ";
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
					grpList.add(gm.group());
				}
				
			}
			/* status list matcher */
			if(m.start(6) != -1)
			{	
				Matcher sm = p.matcher(m.group(6));
				while(sm.find())
				{
					statList.add(sm.group());
				}
				
			}
			/* assignement list matcher */
			if(m.start(7) != -1)
			{	
				Matcher am = p.matcher(m.group(6));
				while(am.find())
				{
					assignementList.add(am.group());
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
			sql += "( owner_grp = " + "'" + groupList.get(0) +"'";
			for (i = 1 ; i < groupList.size() ; i++) 
			{
				sql += " OR owner_grp = " + "'" + groupList.get(i) + "'";	
			}
			sql += ")"; 
		}
		if(statusList.size() > 0)
		{
			sql += " AND ( owner_stat = " + "'" + statusList.get(0) + "'";
			for (i = 1 ; i < statusList.size
					() ; i++) 
			{
				sql += " OR owner_stat = " + "'"+ statusList.get(i) +"'";	
			}
			
			sql += ")"; 
		}
		if(assignementList.size() > 0)
		{
			sql += " AND ( owner_aff = " + "'" + assignementList.get(0) + "'";
			for (i = 1 ; i < assignementList.size() ; i++) 
			{
				sql += " OR owner_aff = " + "'"+ assignementList.get(i) +"'";	
			}
			
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

	public ArrayList<String> search() throws ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost/" + DB_NAME;
		
		Connection cn = null;
		Statement st = null;
		ArrayList<String> results = new ArrayList<String>();
		try 
		{	
			// load driver
			Class.forName("com.mysql.jdbc.Driver");
			
			// get connection
			cn = (Connection) DriverManager.getConnection(url, LOGIN, PASSWD);
			
			// create statement 
			st = cn.createStatement();
			
			// execute "select" request
			ResultSet rs = st.executeQuery(sql);
			
			// saving some interesting fields
			while(rs.next())
			{
				results.add(rs.getString(1));	// ref
				results.add(rs.getString(2));	// encipher_skey
				results.add(rs.getString(3));	// owner_grp
				results.add(rs.getString(4));	// owner_status
				results.add(rs.getString(5));	// owner_aff
			}
			
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				cn.close();
				st.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		return results;
	}

	public void printResult(ResultSet rs) throws SQLException
	{
		// print some interesting fields
		while(rs.next())
		{
			System.out.println(rs.getString(1));	// ref
			System.out.println(rs.getString(2));	// encipher_skey
			System.out.println(rs.getString(3));	// owner_grp
			System.out.println(rs.getString(4));	// owner_status
			System.out.println(rs.getString(5));	// owner_aff
		}	
	}
	
	public boolean isFormatted() {
		return isFormatted;
	}

	public static void setFormatted(boolean isFormatted) {
		DBManager.isFormatted = isFormatted;
	}
}
