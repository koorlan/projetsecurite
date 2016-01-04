package dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.awt.Insets;

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
		if(l != null){
			this.setVisible(true);
			return;
		}
		l = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				//System.exit(0);
				setVisible(false);
			}
		};
		
		this.buildComponent();
		this.divide();
		
		this.addWindowListener(l);
		this.setSize(900,120);
		pack();
		this.setVisible(true);
	}
	
	private void buildComponent() throws SQLException{
	    type = new JComboBox<String>();
	    affectation = new JComboBox<String>();
		statut = new JComboBox<String>();
		groupe = new JComboBox<String>();
		
		cGauche = new JPanel();
		cGauche.setLayout(new BoxLayout(cGauche, BoxLayout.Y_AXIS));
		//cGauche.setSize(300, 170);
		this.buildForm();
		
		cDroit = new JPanel();
		
		cDroit.setLayout(new BoxLayout(cDroit, BoxLayout.Y_AXIS));
		//cDroit.setSize(600, 170);
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
		//nom.setMaximumSize( nom.getPreferredSize() );

		nom.setMaximumSize(new Dimension(Integer.MAX_VALUE, nom.getPreferredSize().height));
		this.cGauche.add(nom);
	}
	
	private void formChampDB(JComboBox<String> champ, String dbTable, String dbChamp) throws SQLException{
		this.cGauche.add(new JLabel(dbChamp + " :"));
		
		
		
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(this.core.getDB().getDB_PATH() + this.core.getDB().getDB_INFO());
		} catch ( Exception e ) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		stmt = c.createStatement();
		String sql = "SELECT " + dbChamp + " FROM " + dbTable + ";";
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			champ.addItem(rs.getString(dbChamp));
		}
		
		rs.close();
		stmt.close();
		c.close();
		
		//JPanel wrapper = new JPanel();
		//wrapper.add(champ);
		//champ.setSize(champ.getPreferredSize());
		//champ.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	
		this.cGauche.add(champ);
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
					try {
						core.getRequest().forge(type.getSelectedItem(), groupe.getSelectedItem(), statut.getSelectedItem(), affectation.getSelectedItem());
					} catch (ClassNotFoundException | SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
			} 
		});
	}
	public void addResponse(String nom, String type, String affectation, String statut, String groupe, String data){
		this.modele.addReponse(new DialogDataReponse(nom,type,affectation,statut,groupe,data));
	}
	public void refresh(){
		this.modele.refresh();
	}
}
