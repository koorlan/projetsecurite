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
			c = DriverManager.getConnection("jdbc:sqlite:datas/Alice.sqlite");
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
		
		this.getContentPane().add(affectation);
	}
	
	public void formStatut(){
		this.getContentPane().add(new JLabel("Statut :"));
		
		statut = new JComboBox<String>();
		statut.addItem("---");
		statut.addItem("Étudiant");
		statut.addItem("Professeur");
		statut.addItem("Administratif");
		this.getContentPane().add(statut);
	}
	
	public void formGroupe(){
		this.getContentPane().add(new JLabel("Groupe :"));
		
		groupe = new JComboBox<String>();
		groupe.addItem("---");
		groupe.addItem("Étudiant");
		groupe.addItem("Professeur");
		groupe.addItem("Administratif");
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
