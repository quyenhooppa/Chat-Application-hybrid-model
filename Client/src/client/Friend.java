/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author quyenhooppa
 */
public class Friend {
    
    private String name;
    private String ip;
    private int sentPort; // port to send to friend
    private int status;

    public Friend(String name, String ip, int sentPort, int status) {
        this.name = name;
        this.ip = ip;
        this.sentPort = sentPort;
        this.status = status;
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
    
}
