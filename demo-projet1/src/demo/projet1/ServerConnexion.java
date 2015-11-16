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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allali
 */
public class ServerConnexion extends Process implements Runnable{
    
    private String ipDest;
    private int port;
    private int maxConnexion;
    private ServerSocket sSocket = null;
    private PrintWriter writer = null;         //pour écrire dans une socket
    private BufferedInputStream reader = null; //pour lire une socket
    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    public ServerConnexion(User user, String ipDest, int maxConnexion ,int port){
        this.user = user;
        this.ipDest = ipDest;
        this.maxConnexion = maxConnexion;
        this.port = port;
    }

    public ServerSocket getsSocket() {
        return sSocket;
    }

    public void setsSocket(ServerSocket sSocket) {
        this.sSocket = sSocket;
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
    
    //La méthode que nous utilisons pour lire les requêtes
    public String read() throws IOException{
        String requete = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        requete = new String(b, 0, stream);
        return requete;
    }

    @Override
    public void run() {
        
                
        try {
            sSocket = new ServerSocket(port, maxConnexion, InetAddress.getByName(ipDest));
            sSocket.setReuseAddress(true);
        
                    
                while(isRunning == true)
                {
                //On attend une connexion d'un utilisateur client
                Socket client = sSocket.accept();

                //Une fois reçue, on la traite dans un thread séparé (pour pouvoir répondre à plusieurs utilisateurs à la fois).
                System.out.println("Connexion utilisateur reçue.");
                Thread tTraitementRequest = new Thread(new Runnable(){

                    @Override
                    public void run(){
                        System.out.println("Lancement du traitement de la requete");

                        //tant que la connexion est active, on traite les requetes
                        while(!client.isClosed()){

                            try {

                                writer = new PrintWriter(client.getOutputStream());
                                reader = new BufferedInputStream(client.getInputStream());

                                //On lit la requête du client
                                String request = read();
                                
                                if ("close".equals(request.toLowerCase())){
                                    writer = null;
                                    reader = null;
                                    client.close();
                                    break;
                                }
                                else {
                                    
                                    System.out.println("Requête reçu: " + request );

                                    String reponse = user.getRessources().get(request);
                                    if ( reponse != null){
                                        writer.write(reponse);                                        
                                        writer.flush();
                                    }
                                    else {
                                        writer.write("ressource non trouvée");
                                    }
                                }
                            }
                            catch(SocketException e){
                                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                                break;
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //La méthode que nous utilisons pour lire les requêtes
                    public String read() throws IOException{
                        String requete = "";
                        int stream;
                        byte[] b = new byte[4096];
                        stream = reader.read(b);
                        requete = new String(b, 0, stream);
                        return requete;
                    }

                });
                tTraitementRequest.start();
            }
                sSocket.close();
    }
            catch (IOException e) {
                e.printStackTrace();
            }
        }          
}
    

