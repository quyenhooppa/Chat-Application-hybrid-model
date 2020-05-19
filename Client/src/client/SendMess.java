/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author quyenhooppa
 */
public class SendMess extends Thread {
    
    private User userSend;
    private int sentPort;
    private String peerName;
    private String mess;
    private boolean isSend;

    public SendMess(User userSend, int sentPort, String peerName, boolean isSend) {
        this.userSend = userSend;
        this.sentPort = sentPort;
        this.peerName = peerName;
        this.isSend = isSend;
        this.mess = "";
    }
    
    
    //--------------- SETTER ---------------
    public void setSentPort(int sentPort) {
        this.sentPort = sentPort;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    //--------------- GETTER ---------------
    public int getSentPort() {
        return sentPort;
    }

    public String getPeerName() {
        return peerName;
    }

    public String getMess() {
        return mess;
    }

    public boolean isIsSend() {
        return isSend;
    }

    
    // Thread to send messages to each friend
    @Override
    public void run() {
        while (true) {
            if (isSend = true) {
                try (Socket socket = new Socket("local host", sentPort)) {
                    
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);
                    
                    
                    String messSent = userSend.getUserName() + "%" + mess;
                    output.println(messSent);
                    
                    MessRecord record = userSend.messRecordList.get(peerName);
                    record.addNumOfMess(); 
                    record.addMess(messSent, 1);
                    
                    mess = "";     
                    isSend = false;
                    
                    try {
                        socket.close();      
                    } catch(IOException e) {
                        System.out.println("Send close socket: " 
                                + e.getMessage());
                    } 

                } catch (IOException e) {
                    // Logger.getLogger(
                    //    sendMess.class.getName()).log(Level.SEVERE, null, e);

                    System.out.println("Send " + peerName + " " 
                            + e.getMessage());
                    isSend = false;
                    break;
                }
            }
        }
    }
       
}

