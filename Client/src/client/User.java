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
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;
/**
 *
 * @author quyenhooppa
 */
public class User extends Thread {

    private String name; // name of user
    private String pass; // passwprd
    private int receivedPort; // port connection
    private String mess; 
    
    // list of user's friends
    HashMap<String, Friend> friendList; 
    // record messages communicate with friends
    HashMap<String, MessRecord> messRecordList; 
    
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        //friendList = new HashMap<>();
        //messRecordList = new HashMap<>();
    }

    
    public User(String name, String pass, int receivedPort, 
            HashMap<String, Friend> friendList, 
            HashMap<String, MessRecord> messRecordList) {
        this.name = name;
        this.pass = pass;
        this.receivedPort = receivedPort;
        this.friendList = friendList;
        this.messRecordList = messRecordList;
    }
    
    

    public void setUserName(String name) 
    {
        this.name = name;
    }
    
    public void setMess(String mess) 
    {
        this.mess = mess;
    }
    
    public void setPass(String pass) 
    {
        this.pass = pass;
    }

    
    public String getUserName() 
    {
        return this.name;
    }
    
    public String getMess() 
    {
        return this.mess;
    }
    
    public String getPass() 
    {
        return this.pass;
    }
    
    
    // register header is 1
    public boolean register() {
        
        try (Socket socket = new Socket("localhost", 5000)){
            
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = 
                    new PrintWriter(socket.getOutputStream(), true);
            
            output.println("1" + this.name + "-" + this.pass);
            
            String response = input.readLine();
 
            try {
                
                socket.close(); 
                if (Integer.parseInt(response) == 1) {
                    return true;
                }
                
            } catch(IOException e) {
                System.out.println("Register close socket: " 
                        + e.getMessage());
            }
            
        } catch (IOException e) {
            System.out.println("Error from " + this.name + " " 
                    + e.getMessage());
        }
        
        return false;
    }
    
    
    // login header is 2
    public boolean login() {
        
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);

            stringToEcho.println("2" + this.name + "-" + this.pass);
            
            String response = echoes.readLine();
            // login failed
            if (response.charAt(0) == '0') {
                return false;
            }
            
            int curPos = 0;            
            while (response.charAt(curPos) != '%') {
                curPos++;
            }
            // get the received mess port
            receivedPort = Integer.parseInt(response.substring(0, curPos));
            
            int friendCount = 0;
            int pos = curPos;
            curPos++;
            
            while (response.charAt(curPos) != '%') {
                curPos++;
            }
            // get the number of friends
            friendCount = Integer.parseInt(response.substring(pos + 1, curPos));
            
            pos = curPos;
            curPos++;
            
            // add friends to the list
            for (int i = 0; i < friendCount - 1; i++) {
                String friendName;
                int sentPort;
                
                int status =  Character.getNumericValue(response.charAt(curPos));
                curPos++;
                pos++;
                
                while (response.charAt(curPos) != '-') {
                    curPos++;
                }
                // get friend name
                friendName = response.substring(pos + 1, curPos);
                
                pos = curPos;
                curPos++;
                
                if (i == friendCount - 1) {
                    sentPort = Integer.parseInt(response.substring(curPos + 1));
                } else {
                    while (response.charAt(curPos) != '%') {
                        curPos++;
                    }
                    // get port to send
                    sentPort = Integer.parseInt(response.substring(pos + 1, curPos));
                    
                    pos = curPos;
                    curPos++;
                }
                
                Friend newFriend = new Friend(friendName, sentPort, status);
                MessRecord newMessRecord = new MessRecord(friendName);
                
                friendList.put(friendName, newFriend); // add friend
                messRecordList.put(friendName, newMessRecord); // add mess record
                
            }

            
            try {
                
                socket.close();
                return true;
                
            } catch(IOException e) {
                System.out.println("Login close socket: " 
                        + e.getMessage());
            } 
        } catch (IOException e) {
            System.out.println("Login Error " 
                    + e.getMessage());
        }
        
        return false;
    }
 
    
        // add new friend header is 4
    public int addFriend(String name)
    {
        int info = -1; // not found friend
        
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);

            stringToEcho.println("4" + this.name + "-" + name);
            
            String response = echoes.readLine();
            System.out.println(response);
            
            int friendStatus = Integer.parseInt(response); 
            
            // already friend
            if (friendStatus > 1) {
                info = 0;
            
            // add new friend
            } else if (friendStatus > 0) {
//                User newFriend = new User(name, friendStatus);
//                friendList.put(name, newFriend);
//                info = 1;
            }
            
            try {
                socket.close();
            } catch(IOException e) {
                System.out.println("Add close socket: " 
                        + e.getMessage());
            } 
        } catch (IOException e) {
            System.out.println("Add Error " 
                    + e.getMessage());
        }
        
        return info;
    }
    
    
    
    @Override    
    public void run()
    {
        while (true) {
            try(ServerSocket serverSocket = new ServerSocket(this.receivedPort)) {

                Socket socket = serverSocket.accept();

                try {

                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);

                    String receivedMess = input.readLine();
                    
                    this.setMess(receivedMess);
                    System.out.println("Mess received: " + this.getMess());
                    
                    int curPos = 0;
                    while (receivedMess.charAt(curPos) != '%') {
                        curPos++;
                    }
                    String friendName = receivedMess.substring(0, curPos);
                    
                    MessRecord record = messRecordList.get(friendName);
                    record.addNumOfMess();
                    record.addMess(receivedMess.substring(curPos + 1), 0);

                    output.println("Received");

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

            } catch(IOException e) {
                System.out.println("Server exception " + e.getMessage());
            }
        }
    }
            
}
