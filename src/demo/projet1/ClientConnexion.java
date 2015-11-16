/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.projet1;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientConnexion extends Process implements Runnable{
    
    private String ipDest;
    private int port;
    private Socket cSocket = null;
    private PrintWriter writer = null;         //pour écrire dans une socket
    private BufferedInputStream reader = null; //pour lire une socket
    private String type; //type de la resssources qu'on souhaite récupérer.

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Socket getcSocket() {
        return cSocket;
    }

    public void setcSocket(Socket cSocket) {
        this.cSocket = cSocket;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public BufferedInputStream getReader() {
        return reader;
    }

    public void setReader(BufferedInputStream reader) {
        this.reader = reader;
    }
        

    
    public ClientConnexion(String type, String ipDest, int port){
        this.type = type;
        this.ipDest = ipDest;
        this.port = port;
    }
    
    //Méthode pour lire les réponses du serveur
    public String read() throws IOException{      
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);      
        return response;
    } 

    @Override
    public void run() {
                
                try {
                    
                    
                        cSocket = new Socket(InetAddress.getByName(ipDest), port);
                        cSocket.setReuseAddress(true);
                        writer = new PrintWriter(cSocket.getOutputStream(), true);
                        reader = new BufferedInputStream(cSocket.getInputStream());
                        //On envoie requete au serveur
                        writer.write(type);
                        //flush() POUR ENVOYER RÉELLEMENT DES INFOS AU SERVEUR
                        writer.flush();
                        
                        System.out.println("\t Requête " + type + " envoyée au serveur");
                        
                        //On attend la réponse
                        String response = read();
                        System.out.println("\t * Réponse reçue " + response);
                    
                    writer.write("CLOSE");
                    writer.flush();
                    writer.close();
                    cSocket.close();
                } 
                catch (IOException e1) {
                        e1.printStackTrace();
                    }
    }
    }
    

