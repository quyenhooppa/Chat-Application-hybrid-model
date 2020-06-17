/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

// import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author quyenhooppa
 */
public class MessRecord {
    
    private int numOfMess; // number of mess between peer
    private String peerName; // partner name
    private ArrayList<String> messList; // stores messages

    
    public MessRecord(String peerName) {
        this.peerName = peerName;
        this.numOfMess = 0;
        this.messList = new ArrayList<>();
    }
    
    
    //--------------- SETTER ---------------
    public void setNumOfMess(int numOfMess) {
        this.numOfMess = numOfMess;
    }
    

    //--------------- GETTER ---------------
    public int getNumOfMess() {
        return numOfMess;
    }

    public String getPeerName() {
        return peerName;
    }

    public ArrayList<String> getMessList() {
        return messList;
    }
    
    
    // increase total number of mess when new mess added
    public void addNumOfMess(int amount) {
        this.numOfMess = this.numOfMess + amount;
    }
        
    // add messagae to the list, type of messages is sent (1) or received (0)
    public void addMess(String mess, int type) {
        
        String encodeMess = String.valueOf(type) + mess;
        messList.add(encodeMess);
        
    }
    
}
