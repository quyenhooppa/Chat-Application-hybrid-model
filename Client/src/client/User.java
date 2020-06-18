/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.chatGUI;
import clientUI.requestGUI;
import java.io.IOException;
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;

/**
 *
 * @author quyenhooppa
 */
public class User extends Thread {

    private String name; // name of user
    private String pass; // password
    private String clientIp; // user ip
    private int receivedPort; // port connection
    private RequestServer requestServer;
    private ServerSocket serverSocket;
    private chatGUI chatUI;
    private requestGUI requestUI;
    
    // list of user's friends
    LinkedHashMap<String, Friend> friendList; 
    
    // record messages communicate with friends
    HashMap<String, MessRecord> messRecordList; 
    
    // list receive connection with friend
    HashMap<String, ReceiveMess> receiveList;
    
    // list send connection with friend
    HashMap<String, SendMess> sendList;
    
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.friendList = new LinkedHashMap<>();
        this.messRecordList = new HashMap<>();
        this.requestServer = new RequestServer(this);
        this.sendList = new HashMap<>();
        this.receiveList = new HashMap<>();
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

    public RequestServer getRequestServer() {
        return requestServer;
    }
    
    public LinkedHashMap<String, Friend> getFriendList() {
        return friendList;
    }

    public HashMap<String, MessRecord> getMessRecordList() {
        return messRecordList;
    }

    public HashMap<String, ReceiveMess> getReceiveList() {
        return receiveList;
    }

    public HashMap<String, SendMess> getSendList() {
        return sendList;
    }
    
    public chatGUI getChatUI() {
        return chatUI;
    }

    public requestGUI getRequestUI() {
        return requestUI;
    }
    
    
    
    // classify the request sent to server
    public void requestToServer(String requestToServer) {
        int typeOfRequest = 6;

        switch(requestToServer) {
            case "register":
                typeOfRequest = 1;
                break;
            case "login":
                typeOfRequest = 2;
                break;
            case "find":
                typeOfRequest = 3;
                break;
            case "add":
                typeOfRequest = 4;
                break;
            case "logout":
                typeOfRequest = 5;
                break;
            default:
                typeOfRequest = 6;
                break;
        }
        
        requestServer.setTypeOfRequest(typeOfRequest);
    }
    
    public void userNameAdding(String name) {
        requestServer.setNameAdding(name);
    }
    
    private boolean checkSendConnection(String name) {
        return sendList.containsKey(name);
    }
    
    private SendMess startSendConnection(String info) {
        SendMess newSending;
        String name;
        if (friendList.containsKey(info)) {
            newSending = new SendMess(this, friendList.get(info), 0);
            name = info;
        } else {
            String ip;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
            int port;

            int curPos = 0;
            while (info.charAt(curPos) != '-') {
                curPos++;
            }
            name = info.substring(0, curPos);
            int pos = curPos;
            curPos++;
            
            while (info.charAt(curPos) != '-') {
                curPos++;
            }
            ip = info.substring(pos + 1, curPos);
            port = Integer.parseInt(info.substring(curPos + 1));
            
            newSending = new SendMess(this, new Friend(name, ip, port, 1), 0);
        }
//        } else {
//            newSending = new SendMess(this, friendList.get(info), 0);
//            name = info;
//        }
        sendList.put(name, newSending);
        sendList.get(name).start();
        
        return sendList.get(name);
    }
    
    public void sendToFriend(String info, String type) {
        SendMess sending;
        
        if (!checkSendConnection(info)) {
            sending = startSendConnection(info);
        } else {
            sending = sendList.get(info);
        }
           
        int typeSending = 0;
        
        switch(type) {
            case "mess":
                typeSending = 1;
                break;
            case "file":
                typeSending = 2;
                break;
            case "friendRequest":
                typeSending = 3;
                break;
            case "reply":
                typeSending = 4;
                break;
            case "off":
                typeSending = 5;
                break;
            default:
                typeSending = 0;
                break;
        }
        
        sending.setTypeSending(typeSending);
        
    }
    
    
    
    @Override    
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.receivedPort);
            
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new ReceiveMess(this, socket);
                    
                    //System.out.println(name + ": open friend's connection");
                } catch (IOException ex) {
                     System.out.println("Receive socket created: " + ex);
                     break;
                }
            }
        } catch(IOException e) {
                System.out.println("Receive socket exception " + e.getMessage());
        }
    }
            
}
