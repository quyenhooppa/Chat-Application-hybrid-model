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
    private chatGUI chatUI;

    
    public SendMess(User userSend, Friend friend, int typeSending) {
        this.userSend = userSend;
        this.friend = friend;
        this.typeSending = typeSending;
        this.mess = "";
    }
    
    
    //--------------- SETTER ---------------
    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setChatUI(chatGUI chatUI) {
        this.chatUI = chatUI;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setTypeSending(int typeSending) {
        this.typeSending = typeSending;
    }
    
    

    //--------------- GETTER ---------------
    public String getMess() {
        return mess;
    }

    public int getTypeSending() {
        return typeSending;
    }

    
    // Thread sending to friend
    @Override
    public void run() {
        boolean off = false;
        
        try (Socket socket = new Socket(friend.getIP(), friend.getSentPort())) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                   
            PrintWriter output = 
                    new PrintWriter(socket.getOutputStream(), true);
                    
            String messSent;
            
            while (typeSending == 0) {
            }
            
            while (true) {    
                
                if (typeSending != 0) {
                    messSent = typeSending + userSend.getUserName() + "%" + mess;
                    output.println(messSent);
                    System.out.println(userSend.getUserName() + " sent: " + messSent);
                }
                    
                switch (typeSending) {
                    case 0:
                        
                        int newType = typeSending;
                        if (typeSending != newType) {
                            typeSending = newType;
                        } else {
                            System.out.print("");
                        }
                        break;
                        
                    case 1: // send a message
                        
                        MessRecord record = userSend.getMessRecordList().get(friend.getName());
                        record.addNumOfMess(1);
                        record.addMess(mess, 1);
                        if (chatUI.getFriendName().equals(friend.getName())) {
                                chatUI.displayMess(friend.getName());
                        }
                        System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                        
                        typeSending = 0;
                        break;
                
                    case 2: // send a file
                        
                        try (
                            InputStream in = new FileInputStream(file);
                            OutputStream out = socket.getOutputStream();
                        ) {

                            byte[] buffer = new byte[16 * 1024];
                            
                            int count;
                            //int current = 0;
                            //JProgressBar progress = fileUI.getProgressBar();
                            while ((count = in.read(buffer)) > 0) {
                                out.write(buffer, 0, count);

                                String s = file.length() + "";
//                                current += buffer.length / Integer.valueOf(s);
//                                progress.setValue(30);
                            }
                            //progress.setVisible(false);
                            
                            out.close();
                            in.close();

                            
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        typeSending = 0;
                        break;
                        
                    case 3: // send friend request
                         
                        JOptionPane.showMessageDialog(null, "Request sent");
                        off = true;
                        break;
                        
                    case 4: // send request reply
                        
                        JOptionPane.showMessageDialog(null, "You and " + 
                                friend.getName() + " are friends now");
                        
                        typeSending = 0;
                        break;
                        
                    case 5: // offline
                        
                        //userSend.getSendList().remove(friend.getName());
                        off = true;
                        break;
                        
                    default:
                        break;
                }
                
                if (off == true) {
                    break;
                } 
                
            }
            
            try {
                socket.close();
                System.out.println("send socket close");
            } catch(IOException e) {
                System.out.println("Send close socket: " 
                        + e.getMessage());
            } 
        } catch (IOException e) {
            System.out.println("Send " + friend.getName() + " " 
                 + e.getMessage());
        }
    }
       
}

