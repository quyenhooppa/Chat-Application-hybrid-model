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
public class Friend {
    
    private String name;
    private String ip;
    private int sentPort; // port to send to friend
    private int status;
    // private MessRecord messRecord;
    private ArrayList<String> messRecord; // stores messages

    public Friend(String name, String ip, int sentPort, int status) {
        this.name = name;
        this.ip = ip;
        this.sentPort = sentPort;
        this.status = status;
//        this.messRecord = new MessRecord();
        this.messRecord = new ArrayList<>();
    }

    // set friend's status from server
    public void setStatus(int status) {
        this.status = status;
    }

    
    public String getName() {
        return name;
    }

    public int getSentPort() {
        return sentPort;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getIP() {
        return ip;
    }

//    public MessRecord getMessRecord() {
//        return messRecord;
//    }
    
    public ArrayList getMessRecord() {
        return messRecord;
    }
    
    // add messagae to the list, type of messages is sent (1) or received (0)
    public void addMess(String mess, int type) {
        String encodedMess = String.valueOf(type) + mess;
        messRecord.add(encodedMess);
    }
    
}
