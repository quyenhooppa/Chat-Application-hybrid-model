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
    private Friend friend;
    private String mess;
    private boolean isSend;

    public SendMess(User userSend, Friend friend, boolean isSend) {
        this.userSend = userSend;
        this.friend = friend;
        this.isSend = isSend;
        this.mess = "";
    }
    
    
    //--------------- SETTER ---------------
    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    //--------------- GETTER ---------------
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
            //if (isIsSend() == true) {
            
                try (Socket socket = new Socket(friend.getIP(), friend.getSentPort())) {
                    while (true) {
                        if (isSend == true) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);
                    
                    
                    String messSent = userSend.getUserName() + "%" + mess; 
                    output.println(messSent);
                    
                    MessRecord record = userSend.messRecordList.get(friend.getName());
                    record.addNumOfMess(1); 
                    record.addMess(messSent, 1);
                    
                    System.out.println(mess);
                    
                    mess = "";  
                    isSend = false;
                    
//                    try {
//                        socket.close();      
//                    } catch(IOException e) {
//                        System.out.println("Send close socket: " 
//                                + e.getMessage());
//                        break;
//                    } 
                    }
                    }
                } catch (IOException e) {
                    // Logger.getLogger(
                    //    sendMess.class.getName()).log(Level.SEVERE, null, e);

                    System.out.println("Send " + friend.getName() + " " 
                            + e.getMessage());
                    isSend = false;
                    
                //}
            }
        }
    }
       
}

