/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.projet1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DemoProjet1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        User alice = new User("Alice","Etudiant","INSACVL");
        // on crée automatiquement un thread qui représente alice comme étant un serveur.
        Thread tServerAlice = new Thread(new ServerConnexion(alice, "127.0.0.1", 100, 2345));
        tServerAlice.start();
        // lorsque alice souhaite envoyer une requête on crée un thread 
        //avec comme argument du constructeur ClientConnexion la ressources qu'on souhaite obtenir ainsi que l'adresse ip du destination et le numero du port à utiliser
        Thread tClientAlice = new Thread(new ClientConnexion("email", "127.0.0.1", 2346)); //le numero de port à spécifier pour le client alice est le même que celui sur lequel écoute le serveur bob
        tClientAlice.start();

        User bob = new User("bob","Etudiant","INSACVL");
        bob.getRessources().put("email", "bob@insa-cvl.com");
        Thread tServerBob = new Thread(new ServerConnexion(bob, "127.0.0.1", 100, 2346));
        tServerBob.start();
    }
    
}
