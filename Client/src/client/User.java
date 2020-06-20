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
    
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.friendList = new LinkedHashMap<>();
        this.messRecordList = new HashMap<>();
        this.requestServer = new RequestServer(this);
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
    
    public void setUserNameAdding(String name) {
        requestServer.setNameAdding(name);
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
