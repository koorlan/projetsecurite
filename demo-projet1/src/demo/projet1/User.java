package demo.projet1;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class User extends Process {
    private String name;
    private String status;
    private String affectation;
    private ArrayList<String> groupe = new ArrayList<String>();
    private HashMap<String,String> ressources = new HashMap<String,String>(); //pour stocker les ressources j'ai choisi de les stocker dans une HashMap avec une clé de type String (par exemple image) et la valeur qui sera le chemin du fichier contenant la ressource.
    
    public String getNom() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAffectation() {
        return affectation;
    }

    public void setAffectation(String affectation) {
        this.affectation = affectation;
    }

    public ArrayList<String> getGroupe() {
        return groupe;
    }

    public void addGroupe(String newGroupe) {
        this.groupe.add(newGroupe);
    }

    public void exitGroupe(String groupe){
        this.groupe.remove(groupe);
    }
    
    public HashMap<String, String> getRessources() {
        return ressources;
    }

    public void addRessources(String typeRessources, String ressources) {
        this.ressources.put(typeRessources, ressources);
    }
        
    public User(String name, String status, String affectation){
        
        this.name = name;
        this.status = status;
        this.affectation = affectation;
    }
    
    public User(String name, String status, String affectation , ArrayList<String> groupe){
        this.name = name;
        this.status = status;
        this.affectation = affectation;
        this.groupe = groupe;
    }
    
    public User(String name, String status, String affectation , HashMap<String ,String> ressources){
        this.name = name;
        this.status = status;
        this.affectation = affectation;
        this.ressources = ressources;
    }
    public User(String name, String status, String affectation , ArrayList<String> groupe, HashMap<String ,String> ressources){
        this.name = name;
        this.status = status;
        this.affectation = affectation;
        this.groupe = groupe;
        this.ressources = ressources;
    }
}
