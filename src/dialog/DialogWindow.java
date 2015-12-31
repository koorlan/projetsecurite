package dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import manager.CoreManager;

public class DialogWindow extends JFrame {

	private CoreManager core; 
	private WindowAdapter l;
	private String port;
	private JSplitPane window;
	private JPanel cGauche;
    private JPanel cDroit;
    private JTextField nom; 
    private JComboBox<String> type;
    private JComboBox<String> affectation;
	private JComboBox<String> statut;
	private JComboBox<String> groupe;
	private JButton bouton; 
    
	public DialogWindow(CoreManager core) throws SQLException{
		// We give a name for the interface
		super("Interface utilisateur");		
		this.core = core;
	}
	
	public void start() throws SQLException {
		this.port = port;
		l = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//System.exit(0);
				setVisible(false);
			}
		};
		
		this.buildComponent();
		this.divide();
		
		this.addWindowListener(l);
		this.setSize(900,170);
		this.setVisible(true);
	}
	
	private void buildComponent() throws SQLException{
		cGauche = new JPanel();
		cGauche.setLayout(new GridLayout(6, 2));
		cGauche.setSize(300, 170);
		this.buildForm();
		
		cDroit = new JPanel();
		cDroit.setLayout(new GridLayout(1, 1));
		cDroit.setSize(600, 170);
		this.buildReponses();
	}
    
    private DialogTabReponse modele = new DialogTabReponse();
    private JTable tableau;
	
	private void buildReponses(){
		this.tableau = new JTable(modele);
		this.cDroit.add(new JScrollPane(tableau), BorderLayout.CENTER);
	}
	
	private void buildForm() throws SQLException{
		this.formNom();
		this.formChampDB(this.type, "Types", "Type");
		this.formChampDB(this.affectation, "Affectations", "Affectation");
		this.formChampDB(this.statut, "Statuts", "Statut");
		this.formChampDB(this.groupe, "Groupes", "Groupe");
		this.formBouton(); 
	}
	
	private void formNom(){
		this.cGauche.add(new JLabel("Nom :"));
		
		nom = new JTextField();
		this.cGauche.add(nom);
	}
	
	private void formChampDB(JComboBox<String> champ, String dbTable, String dbChamp) throws SQLException{
		this.cGauche.add(new JLabel(dbChamp + " :"));
		
		JComboBox<String> nChamp = new JComboBox<String>();
		
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Initialisation/datas/InitDB.sqlite");
		} catch ( Exception e ) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		stmt = c.createStatement();
		String sql = "SELECT " + dbChamp + " FROM " + dbTable + ";";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			nChamp.addItem(rs.getString(dbChamp));
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		this.cGauche.add(nChamp);
	}
	
	private void divide(){
		this.window = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				this.cGauche,
				this.cDroit);
		this.window.setDividerLocation(200);
		this.window.setOneTouchExpandable(true);
		
		this.add(this.window);
	}
	
	private void formBouton(){
		bouton = new JButton("Envoyer"); 
		this.cGauche.add(bouton);
		
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
				
					System.out.println(type.getSelectedItem());
					System.out.println(affectation.getSelectedItem());
					System.out.println(groupe.getSelectedItem());
					System.out.println(statut.getSelectedItem());
					
					try {
						core.getRequest().forge(type.getSelectedItem(), groupe.getSelectedItem(), statut.getSelectedItem(), affectation.getSelectedItem());
					} catch (ClassNotFoundException | SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			} 
		});
	}
}
