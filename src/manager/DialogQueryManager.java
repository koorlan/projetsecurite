package manager;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import java.awt.GridLayout;

public class DialogQueryManager extends JFrame {
	WindowListener l;
	
	private CoreManager core; 
	JTextField nom;
	JComboBox<String> type;
	JComboBox<String> affectation;
	JComboBox<String> statut;
	JComboBox<String> groupe;
	JButton bouton;
	
	public DialogQueryManager(CoreManager core){
		// We give a name for the interface
		super("Envoyer une requête");		
		this.core = core;
	}
	

	public void start() throws SQLException {
		l = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//System.exit(0);
			}
		};
		
		this.setLayout(new GridLayout(6, 2));
		
		// We print inputs in the dialog
		this.formNom();
		this.formType();
		this.formAffectation();
		this.formStatut();
		this.formGroupe();
		this.formBouton();
		
		addWindowListener(l);
		setSize(300,160);
		setVisible(true);
	}
	
	public void formNom(){
		this.getContentPane().add(new JLabel("Nom :"));
		
		nom = new JTextField();
		this.getContentPane().add(nom);
	}
	
	
	
	public void formType() throws SQLException{
		this.getContentPane().add(new JLabel("Type :"));
		
		type = new JComboBox<String>();
		
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:datas/InitDB.sqlite");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		
		stmt = c.createStatement();
		String sql = "SELECT * FROM Types;";
		
		ResultSet rs = stmt.executeQuery( sql );
		
		type.addItem("---");
		while (rs.next()) {
			String champ = rs.getString("Type");
			type.addItem(champ);
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		this.getContentPane().add(type);
	}
	
	public void formAffectation() throws SQLException{
		this.getContentPane().add(new JLabel("Affectation : "));
		
		affectation = new JComboBox<String>();
		
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:datas/InitDB.sqlite");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		
		stmt = c.createStatement();
		String sql = "SELECT Affectation FROM Affectations;";
		
		ResultSet rs = stmt.executeQuery( sql );
		
		affectation.addItem("---");
		while (rs.next()) {
			String champ = rs.getString("Affectation");
			affectation.addItem(champ);
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		this.getContentPane().add(affectation);
	}
	
	public void formStatut() throws SQLException{
		this.getContentPane().add(new JLabel("Statut :"));
		
		statut = new JComboBox<String>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:datas/InitDB.sqlite");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		
		stmt = c.createStatement();
		String sql = "SELECT Statut FROM Statuts;";
		
		ResultSet rs = stmt.executeQuery( sql );
		
		statut.addItem("---");
		while (rs.next()) {
			String champ = rs.getString("Statut");
			statut.addItem(champ);
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		this.getContentPane().add(statut);
	}
	
	public void formGroupe() throws SQLException{
		this.getContentPane().add(new JLabel("Groupe :"));
		
		groupe = new JComboBox<String>();
		
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:datas/InitDB.sqlite");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		
		stmt = c.createStatement();
		String sql = "SELECT Groupe FROM Groupes;";
		
		ResultSet rs = stmt.executeQuery( sql );
		
		groupe.addItem("---");
		while (rs.next()) {
			String champ = rs.getString("Groupe");
			groupe.addItem(champ);
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		this.getContentPane().add(groupe);
	}
	
	public void formBouton(){
		bouton = new JButton("Envoyer"); 
		this.getContentPane().add(bouton);
		
		bouton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				int poids = type.getSelectedIndex() + 
						affectation.getSelectedIndex() +
						statut.getSelectedIndex() +
						groupe.getSelectedIndex();
				
				if(poids < 4){
					System.out.println("QueryError : "
							+ "Merci de remplir l'ensemble des champs.");
				}
				else {
					// send a new query on the network
				}
			} 
		});
	}
}
