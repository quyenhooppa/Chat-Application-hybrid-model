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
public final class User extends Thread {

    private String name; // name of user
    private String pass; // passwprd
    private int receivedPort; // port connection
    private String mess; 
    
    // list of user's friends
    HashMap<String, Friend> friendList; 
    // record messages communicate with friends
    HashMap<String, MessRecord> messRecord; 
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        friendList = new HashMap<>();
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
    
    // register header is 1
    public int register() {
        try (Socket socket = new Socket("localhost", 5000)){
            
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);
            
            stringToEcho.println("1" + this.name + "-" + 
                    this.pass);
            
            String response = echoes.readLine();
           
            //System.out.println("Id is: " + response);
            
            //this.setID(response);
            //this.setPort((Integer.parseInt(response) % 1000) + 3000);
 
            try {
                
                socket.close(); 
                return Integer.parseInt(response);
                
            } catch(IOException e) {
                System.out.println("Register close socket: " 
                        + e.getMessage());
            }
            
        } catch (IOException e) {
            System.out.println("Error from " + this.name + " " 
                    + e.getMessage());
        }
        
        return 0;
    }
    
    // login header is 2
    public int login() {
        int login = 0;
        
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);

            stringToEcho.println("2" + this.name + "-" + this.pass);
            
            String response = echoes.readLine();
            //System.out.println(response);

            login = Integer.parseInt(response);

            try {
                socket.close();
            } catch(IOException e) {
                System.out.println("Login close socket: " 
                        + e.getMessage());
            } 
        } catch (IOException e) {
            System.out.println("Login Error " 
                    + e.getMessage());
        }
        
        return login;
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

                    String echoString = input.readLine();
                    
                    this.setMess(echoString);
                    System.out.println("Mess received: " + this.getMess());

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
