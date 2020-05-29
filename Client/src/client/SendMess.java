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
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author quyenhooppa
 */
public class SendMess extends Thread {
    
    private User userSend;
    private Friend friend; 
    private String mess;
    private boolean isAdd; // the mess is the add friend request
    private chatGUI chatUI;

    
    public SendMess(User userSend, Friend friend, boolean isAdd) {
        this.userSend = userSend;
        this.friend = friend;
        
        this.isAdd = false;
        this.mess = "";
    }
    
    
    //--------------- SETTER ---------------
    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public void setChatUI(chatGUI chatUI) {
        this.chatUI = chatUI;
    }

    
    //--------------- GETTER ---------------
    public String getMess() {
        return mess;
    }

    public boolean isIsAdd() {
        return isAdd;
    }

    
    // Thread to send messages to each friend
    @Override
    public void run() {
            try (Socket socket = new Socket(friend.getIP(), friend.getSentPort())) {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                   
                PrintWriter output = 
                        new PrintWriter(socket.getOutputStream(), true);
                    
                String messSent;
                
                if (isAdd == false) {
                    messSent = "1" + userSend.getUserName() + "%" + mess;
                    output.println(messSent);
                    
                    MessRecord record = userSend.getMessRecordList().get(friend.getName());
                    record.addNumOfMess(1); 
                    record.addMess(mess, 1);
                    
                    chatUI.displayMess(friend.getName());
                    
                    System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                    
                    mess = "";
                    } else {
                        messSent = userSend.getUserName();
                        
                        if (input.readLine().equals("Accpeted")) {
                            userSend.addFriend(friend.getName());
                            userSend.getChatUI().friendClassify();
                            
                            JOptionPane.showMessageDialog(null, 
                                    "You and " + friend.getName() + "are now friends!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Request rejected");
                        }
                        
                        mess = "";
                    }
                   
                    //System.out.println("Received!"); 
                    
                try {
                    socket.close();      
                } catch(IOException e) {
                    System.out.println("Send close socket: " 
                            + e.getMessage());
                } 
            }   
                
                catch (IOException e) {

                    System.out.println("Send " + friend.getName() + " " 
                            + e.getMessage());
                }
                    
//                } catch (BadLocationException ex) {
//            Logger.getLogger(SendMess.class.getName()).log(Level.SEVERE, null, ex);
                
    }
       
}

