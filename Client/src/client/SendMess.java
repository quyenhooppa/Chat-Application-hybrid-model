/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.chatGUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

/**
 *
 * @author quyenhooppa
 */
public class SendMess extends Thread {
    
    private User userSend;
    private Friend friend;
    private String mess;
    private boolean isSend;
    private chatGUI chatUI;

    public SendMess(User userSend, Friend friend, boolean isSend) {// throws IOException {
        this.userSend = userSend;
        this.friend = friend;
        this.isSend = isSend;
        this.mess = "";
        //this.socket = new Socket(friend.getIP(), friend.getSentPort()); 
    }
    
    
    //--------------- SETTER ---------------
    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public void setChatUI(chatGUI chatUI) {
        this.chatUI = chatUI;
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
                try (Socket socket = new Socket(friend.getIP(), friend.getSentPort())) {
                            BufferedReader input = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
                   
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);
                    
                    
                    String messSent = userSend.getUserName() + "%" + mess; 
                    output.println(messSent);
                    
                    MessRecord record = userSend.getMessRecordList().get(friend.getName());
                    record.addNumOfMess(1); 
                    record.addMess(mess, 1);
                    
                    chatUI.displayMess(friend.getName());
                    
                    System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                    
                    mess = "";  
                    
                    try {
                        socket.close();      
                    } catch(IOException e) {
                        System.out.println("Send close socket: " 
                                + e.getMessage());
                    } 
                
                
                } catch (IOException e) {

                    System.out.println("Send " + friend.getName() + " " 
                            + e.getMessage());
                    isSend = false;
                    
                } catch (BadLocationException ex) {
            Logger.getLogger(SendMess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}

