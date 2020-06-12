/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.chatGUI;
import clientUI.requestGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;
import javax.swing.JOptionPane;
/**
 *
 * @author quyenhooppa
 */
public class User extends Thread {

    private String name; // name of user
    private String pass; // password
    private String clientIp; // user ip
    private int receivedPort; // port connection
    private RequestServer request;
    private ServerSocket serverSocket;
    private chatGUI chatUI;
    private requestGUI requestUI;
    
    // list of user's friends
    LinkedHashMap<String, Friend> friendList; 
    
    // record messages communicate with friends
    HashMap<String, MessRecord> messRecordList; 
    
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.friendList = new LinkedHashMap<>();
        this.messRecordList = new HashMap<>();
        this.request = new RequestServer(this);
    }
    
    
    //--------------- SETTER ---------------
    public void setReceivedPort(int receivedPort) {
        this.receivedPort = receivedPort;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
        
    public void setChatUI(chatGUI chatUI) {
        this.chatUI = chatUI;
    }

    public void setRequestUI(requestGUI requestUI) {
        this.requestUI = requestUI;
    }


    
    //--------------- GETTER ---------------
    public String getUserName() 
    {
        return this.name;
    }
    
    public String getPass() 
    {
        return this.pass;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getReceivedPort() {
        return receivedPort;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public RequestServer getRequest() {
        return request;
    }
    
    public LinkedHashMap<String, Friend> getFriendList() {
        return friendList;
    }

    public HashMap<String, MessRecord> getMessRecordList() {
        return messRecordList;
    }

    public chatGUI getChatUI() {
        return chatUI;
    }

    public requestGUI getRequestUI() {
        return requestUI;
    }
    
    
    
    public void requestToServer(int typeOfRequest) {
        request.setTypeOfRequest(typeOfRequest);
    }
    
    public void userNameAdding(String name) {
        request.setNameAdding(name);
    }
    
    
    @Override    
    public void run()
    {
        
        try {
            this.serverSocket = new ServerSocket(this.receivedPort);
            
            while (true) {
                Socket socket = serverSocket.accept();

                try {

                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);

                    String receivedMess = input.readLine();
                    
                    System.out.println(name + " received: " + receivedMess);
                    
                    int curPos = 1;
                    String sender;
                    String senderInfo;
                    
                    switch (receivedMess.charAt(0)) {
                        case '1': // receive message
                            curPos = 1;
                            while (receivedMess.charAt(curPos) != '%') {
                                curPos++;
                            }   
                            String friendName = receivedMess.substring(1, curPos);
                            
                            MessRecord record = messRecordList.get(friendName);
                            record.addNumOfMess(1);
                            record.addMess(receivedMess.substring(curPos + 1), 0);
                            if (chatUI.getFriendName().equals(friendName)) {
                                chatUI.displayMess(friendName);
                            } else {
                                chatUI.newMess(friendName);
                            }
                            
                            System.out.println(record.getMessList().get(record.getNumOfMess()-1));
                            output.println("Received");
                            
                            break;
                            
                        case '2': // receive a file
                            
                            curPos = 1;
                            while (receivedMess.charAt(curPos) != '%') {
                                curPos++;
                            }   
                            friendName = receivedMess.substring(1, curPos);
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
                                OutputStream out = new FileOutputStream(file);
                                InputStream in = socket.getInputStream();
                            ) {
                                
                                byte[] buffer = new byte[16 * 1024];
                                
                                int count;
                                while ((count = in.read(buffer)) > 0) {
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
                            
                            
                        case '3': // receive friend request 
                            
                            curPos = 1;
                            while (receivedMess.charAt(curPos) != '%') {
                                curPos++;
                            } 
                            curPos++;
                            
                            sender = receivedMess.substring(1, curPos - 1);
                            senderInfo = receivedMess.substring(curPos);
                            new requestGUI(this, sender, senderInfo, 0).setVisible(true);
                              
                            break;
                            
                        case '4':
                            
                            curPos = 1;
                            while (receivedMess.charAt(curPos) != '%') {
                                curPos++;
                            } 
                            curPos++;
                            
                            sender = receivedMess.substring(1, curPos - 1);
                            System.out.println("Sender: " + sender);
                            //addFriend(sender);
                            requestToServer(4);
                            userNameAdding(sender);
                            
                            JOptionPane.showMessageDialog(null, "You and " + 
                                sender + " are friends now");
                            
                            break;
                            
                        default: 
                            
                            break;
                    }


                } catch(IOException e) {
                    System.out.println("Oops: " + e.getMessage());
                } finally {
                    try {
                        socket.close();
                    } catch(IOException e) {
                        System.out.println("Receive close socket: " 
                                + e.getMessage());
                    }
                }
                }
            } catch(IOException e) {
                System.out.println("Receive exception " + e.getMessage());
            }
        //}
    }
            
}
