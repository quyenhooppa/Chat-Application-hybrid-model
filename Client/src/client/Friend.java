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
    private int receivedPort;
    private boolean status;

    public Friend(String name, int receivedPort) {
        this.name = name;
        this.receivedPort = receivedPort;
        this.status = false;
    }

    // set friend's status from server
    public void setStatus(boolean status) {
        
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
    
}
