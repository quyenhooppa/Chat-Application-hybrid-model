/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.chatGUI;
import clientUI.fileGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author quyenhooppa
 */
public class SendMess extends Thread {
    
    private User userSend;
    private Friend friend; 
    private String mess;
    private File file;
    private int typeSending; // 1:message - 2:file - 3:friend request - 4:request reply

    public SendMess(User userSend, Friend friend, int typeSending) {
        this.userSend = userSend;
        this.friend = friend;
        this.typeSending = typeSending;
        this.mess = "";
    }
    
    public SendMess(User userSend, Friend friend, String mess, int typeSending) {
        this.userSend = userSend;
        this.friend = friend;
        this.typeSending = typeSending;
        this.mess = mess;
        //newSending();
    }
    
    public SendMess(User userSend, Friend friend, 
            String mess, File file, int typeSending) {
        this.userSend = userSend;
        this.friend = friend;
        this.typeSending = typeSending;
        this.mess = mess;
        this.file = file;
        //newSending();
    }
    
    
    private void newSending() {
        this.start();
    }
    
    //--------------- SETTER ---------------
    public void setFile(File file) {
        this.file = file;
    }
    
    
    // Thread sending to friend
    @Override
    public void run() {
            try (Socket socket = new Socket(friend.getIP(), friend.getSentPort())) {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                   
                PrintWriter output = 
                        new PrintWriter(socket.getOutputStream(), true);
                    
                String messSent;
                
                messSent = typeSending + userSend.getUserName() + "%" + mess;
                output.println(messSent);
                System.out.println(userSend.getUserName() + " sent: " + messSent);
                    
                switch (typeSending) {
                    case 1: // send a message
                        
                        MessRecord record = userSend.getMessRecordList().get(friend.getName());
                        record.addNumOfMess(1);
                        record.addMess(mess, 1);
                        if (userSend.getChatUI().getCurFriendName().equals(friend.getName())) {
                                userSend.getChatUI().displayMess(friend.getName());
                        }
                        
                        //System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                        break;
                
                    case 2: // send a file
                        
                        try (
                            FileInputStream in = new FileInputStream(file);
                            OutputStream out = socket.getOutputStream();
                        ) {
                            new SendMess(userSend, friend, 
                                    "Sending" + file.getName(), 1).start(); 
                            
                            byte[] buffer = new byte[1024 * 1024];
                            
                            int count;
                            while ((count = in.read(buffer)) > -1) {
                                out.write(buffer, 0, count);
                            }
                            
                            out.close();
                            in.close();
                            
                            new SendMess(userSend, friend, 
                                    file.getName() + " done", 1).start();
                            
                            userSend.getFileUI().dispose();
                            JOptionPane.showMessageDialog(null, 
                                file.getName() + " sent done!");
     
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        break;
                        
                    case 3: // send friend request
                         
                        JOptionPane.showMessageDialog(null, "Request sent");
                        break;
                        
                    case 4: // send request reply
                        
                        if (mess.equals("accept")) {
                            JOptionPane.showMessageDialog(null, "You and " + 
                                    friend.getName() + " are friends now");
                        } else if (mess.equals("reject")) {
                            JOptionPane.showMessageDialog(null, "You don't want to add " + 
                                    friend.getName());
                        }
                        break;
                    
                    case 5: //  send status to friend

                        break;
                        
                    default:
                        break;
                }
                   
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
    }
}

