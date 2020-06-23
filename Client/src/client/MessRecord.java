/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;


/**
 *
 * @author quyenhooppa
 */
public class MessRecord {
    
    private ArrayList<String> messList; // stores messages

    
    public MessRecord() {
        this.messList = new ArrayList<>();
    }
  

    //--------------- GETTER ---------------
    public ArrayList<String> getMessList() {
        return messList;
    }   
        
    
    // add messagae to the list, type of messages is sent (1) or received (0)
    public void addMess(String mess, int type) {
        String encodedMess = String.valueOf(type) + mess;
        messList.add(encodedMess);
    }
    
}
