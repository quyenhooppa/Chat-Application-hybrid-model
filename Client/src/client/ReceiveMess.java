/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.RequestFriendUI;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author quyenhooppa
 */
public class ReceiveMess extends Thread {
    
    private User user;
    private Socket socket;
    

    public ReceiveMess(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
        newConnection();
    }
    
    private void newConnection() {
        this.start();
    }

    public Socket getSocket() {
        return socket;
    }
    

    @Override    
    public void run() {
        int typeOfMess = 0;
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            // output = new PrintWriter(socket.getOutputStream(), true);
            
            String receivedMess = input.readLine();
                    
            System.out.println(user.getUserName() + " received: " + receivedMess);
                    
            typeOfMess = Character.getNumericValue(receivedMess.charAt(0));
                    
            int curPos = 1;
            while (receivedMess.charAt(curPos) != '%') {
                curPos++;
            }  
            // get sender name
            String senderName = receivedMess.substring(1, curPos);  
            String senderInfo;
                    
            switch (typeOfMess) {
                case 1: // receive message
                            
                    // curPos at %
                    MessRecord record = user.messRecordList.get(senderName);
                    record.addMess(receivedMess.substring(curPos + 1), 0);
                    if (user.getChatUI().getCurFriendName().equals(senderName)) {
                        user.getChatUI().displayMess(senderName);
                    } else {
                        user.getChatUI().newMess(senderName, 1);
                    }
                            
                    //System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                    break;
                    
                            
                case 2: // receive a file
 
                    // curPos at %
                    int pos = curPos; // pos at %
                    curPos++; // char after %
                            
                    while (receivedMess.charAt(curPos) != '%') {
                         curPos++;
                    }
                    String fileName = receivedMess.substring(pos + 1, curPos);
                    long fileLength = Long.parseLong(receivedMess.substring(curPos + 1));
                            
                    String home = System.getProperty("user.home");
                    File file = new File(home + "/Downloads/" + fileName);
                    file.createNewFile();
                    // create an empty file with given size
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    raf.setLength(fileLength);
                    raf.close();
                            
                    try (
                        FileOutputStream out = new FileOutputStream(file);
                        InputStream in = socket.getInputStream();
                    ) {
                    
                            byte[] buffer = new byte[1024 * 1024];
                                
                            int count;
                            while ((count = in.read(buffer)) > -1) {
                                out.write(buffer, 0, count);
                            }

                            out.close();
                            in.close();
                              
                            JOptionPane.showMessageDialog(null, 
                                fileName + " received done!");

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                            
                    case 3: // receive friend request 
                            
                        // curPos at %
                        curPos++;
            
                        senderInfo = receivedMess.substring(curPos);
                        //System.out.println(senderInfo);
                        new RequestFriendUI(user, senderName, senderInfo, 0).setVisible(true);
                        break;
                            
                    case 4: // request accpeted
                            
                        // curPos at %
                        curPos++;
                        String reply = receivedMess.substring(curPos);

                        //System.out.println("Sender: " + senderName);
                        if (reply.equals("accept")) {
                            user.requestToServer("add");
                            user.setUserNameAdding(senderName);

                            JOptionPane.showMessageDialog(null, "You and " + 
                                senderName + " are friends now");
                        } else if (reply.equals("reject")) {
                            JOptionPane.showMessageDialog(null, senderName + 
                                    " doesn't want to add you");
                        }             
                        break;
                          
                    case 5: // friend logout
                        
                        // curPos at %
                        curPos++;
                        String status = receivedMess.substring(curPos);
                        
                    if (status.equals("off")) {
                        
                        if (user.getFriendList().containsKey(senderName)) {
                            user.getFriendList().get(senderName).setStatus(0);
                            user.getChatUI().addName(senderName, 0);
                            user.getChatUI().removeName(senderName, 1);
                            user.getMessRecordList().get(senderName).getMessList().clear();
                            if (user.getChatUI().getCurFriendName().equals(
                                    senderName)) {
                                user.getChatUI().updateTextArea();
                                user.getChatUI().setCurFriendName("");
                            }
                        }
                        
                        for (int i = 0; i < user.getGroupChatList().size(); i++) {
                            GroupChat groupChat = (new ArrayList<>(
                                    user.getGroupChatList().values())).get(i);
                            if (groupChat.getMemberList().containsKey(senderName)) {
                                user.getChatUI().updateGroup(groupChat.getGroupName(), 0);
                                user.getGroupChatList().remove(groupChat.getGroupName());
                            }
                            if (user.getChatUI().getCurGroupName().equals(
                                    groupChat.getGroupName())) {
                                user.getChatUI().updateTextArea();
                                user.getChatUI().setCurGroupName("");
                            }
                        }
                    } 
                        break;
                    
                case 6:
                    
                    // curPos at %
                    curPos++; 
                    pos = curPos;
                    
                    String groupName;
                    String membersInfo;
                    Friend friendCreate = user.getFriendList().get(senderName);
                    
                    while (receivedMess.charAt(curPos) != '%') {
                        curPos++;
                    }
                    groupName = receivedMess.substring(pos, curPos);
                    membersInfo = receivedMess.substring(curPos + 1)
                            + friendCreate.getName() + "-"
                            + friendCreate.getIP() + "-" 
                            + friendCreate.getSentPort() + "%";
                    
                    user.getGroupChatList().put(groupName, new GroupChat(groupName, membersInfo));
                    user.getChatUI().updateGroup(groupName, 1);
                    //System.out.println(groupName + "\t" + membersInfo);
                    break;
                    
                case 7:
                    
                    // curPos at %
                    curPos++;
                    pos = curPos;
                    
                    while (receivedMess.charAt(curPos) != '%') {
                        curPos++;
                    }
                    groupName = receivedMess.substring(pos, curPos);
                    String mess = receivedMess.substring(curPos + 1);
                    
                    user.getGroupChatList().get(groupName).addMess(
                            senderName + ":  " + mess, 0);
                    
                    if (user.getChatUI().getCurGroupName().equals(groupName)) {
                        user.getChatUI().displayGroupMess(groupName);
                    } else {
                        user.getChatUI().newMess(groupName, 2);
                    }
                    break;
                        
                default:     
                    break;
            }

        } catch(IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } //finally { 
            try {
                socket.close();
            } catch(IOException e) {
                System.out.println("Receive close socket: " 
                    + e.getMessage());
            }
        //}
    }   
}
    

