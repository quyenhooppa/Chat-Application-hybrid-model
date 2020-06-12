/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;

/**
 *
 * @author quyenhooppa
 */
public class ListOfUser {
        
    HashMap<String, Echoer> listOfUserConnetion;  

    public ListOfUser() {
        this.listOfUserConnetion = new HashMap<>();
    }

    public HashMap<String, Echoer> getListOfUserConnetion() {
        return listOfUserConnetion;
    }
    
}
