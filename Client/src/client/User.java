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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
/**
 *
 * @author quyenhooppa
 */
public class User extends Thread {

    private String name; // name of user
    private String pass; // password
    private int receivedPort; // port connection
    private String mess; 
    private chatGUI chatUI;
    
    // list of user's friends
    HashMap<String, Friend> friendList; 
    
    // record messages communicate with friends
    HashMap<String, MessRecord> messRecordList; 
    
    
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.friendList = new HashMap<>();
        this.messRecordList = new HashMap<>();
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
    
    
    //--------------- SETTER ---------------
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
    
    public void setChatUI(chatGUI chatUI) {
        this.chatUI = chatUI;
    }

    
    //--------------- GETTER ---------------
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

    public HashMap<String, Friend> getFriendList() {
        return friendList;
    }

    public HashMap<String, MessRecord> getMessRecordList() {
        return messRecordList;
    }
    
    
    
    
    // register header is 1
    public boolean register() throws ClassNotFoundException, InterruptedException {
        
        try (Socket socket = new Socket("192.168.1.178", 5000)){
            
            InetAddress host = InetAddress.getLocalHost();
            
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);
            
            //ObjectOutputStream output = 
                    //new ObjectOutputStream(socket.getOutputStream()); 
            
            String send = "1" + this.name + "-" + this.pass +
                        "-" + host.getHostAddress();
            stringToEcho.println(send);
            
            String response = echoes.readLine();
            System.out.println(response);
            
            // output.writeObject(send);
                
            //ObjectInputStream input = 
                        // new ObjectInputStream(socket.getInputStream()); 
            //response = (String) input.readObject();
                
        
 
            try {
                
                socket.close(); 
                if (response.equals("1")) {
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
        
        try (Socket socket = new Socket("192.168.1.178", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);

            stringToEcho.println("2" + this.name + "-" + this.pass); // send request
            
            String response = echoes.readLine();
            
            System.out.println(response);
            
            // login failed
            if (response.charAt(0) == '0') {
                return false;
            }
            
            //setUpFriendInfo(response);
            
            //********* TESTING ********
            this.receivedPort = Integer.parseInt(response);
            
            String friendName = "quithu98";
            Friend newFriend = new Friend(friendName, "192.168.1.58", 3001, 1); // testing
            MessRecord newMessRecord = new MessRecord(friendName);
                
            friendList.put(friendName, newFriend); // add friend
            messRecordList.put(friendName, newMessRecord); // add mess record

            
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
 
    
    // get friend's information from the server when login successfully
    private void setUpFriendInfo(String response) {
        /**
        *                           RESPONSE FORMAT
        * " lister port of user % number of friends % 
        *  friend's status friend's name - IP address - listen port of friend % ...." 
        **/
            
            int curPos = 0;            
            while (response.charAt(curPos) != '%') {
                curPos++;
            }
            // curPos is at %
            // get the received mess port
            receivedPort = Integer.parseInt(response.substring(0, curPos));
            
            int friendCount = 0;
            int pos = curPos; // pos is at %
            curPos++; // next character
            
            while (response.charAt(curPos) != '%') {
                curPos++;
            }
            // curPos is at %
            // get the number of friends
            if (pos + 1 == curPos - 1) { // number smaller than 10
                friendCount = Character.getNumericValue(response.charAt(pos + 1));
            } else { // number larger than 10
                friendCount = Integer.parseInt(response.substring(pos + 1, curPos));
            }
            
            pos = curPos; // pos is at %
            curPos++; // next character
            
            // add friends to the list
            for (int i = 0; i < friendCount; i++) {
                String friendName;
                String ip;
                int sentPort;
                
                // get current status of friend
                int status = Character.getNumericValue(response.charAt(curPos));
                
                curPos++; // next character
                pos++; // pos is status index
                 
                while (response.charAt(curPos) != '-') {
                    curPos++;
                }
                // cusPor is at -
                // get friend name
                friendName = response.substring(pos + 1, curPos);
                
                pos = curPos; // pos is at -
                curPos++; // next character
                
                while (response.charAt(curPos) != '-') {
                    curPos++;
                }
                // cusPor is at -
                // get ip
                ip= response.substring(pos + 1, curPos);
                
                pos = curPos; // pos is at -
                curPos++; // next character
                
                if (i == friendCount - 1) {
                    sentPort = Integer.parseInt(response.substring(curPos + 1));
                } else {
                    while (response.charAt(curPos) != '%') {
                        curPos++;
                    }
                    // curPos is at -
                    // get port to send
                    sentPort = Integer.parseInt(response.substring(pos + 1, curPos));
                    
                    pos = curPos; // pos is at -
                    curPos++; // next character
                }
                
                // Friend newFriend = new Friend(friendName, sentPort, status);
                Friend newFriend = new Friend(friendName, ip, sentPort, status);
                MessRecord newMessRecord = new MessRecord(friendName);
                
               friendList.put(friendName, newFriend); // add friend
               messRecordList.put(friendName, newMessRecord); // add mess record
                
            }
    }
    
    
    // find user header is 3
    public boolean findUser(String name) {
        
        try (Socket socket = new Socket("192.168.1.192", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);
            
            stringToEcho.println("3" + this.name);
            
            String response = echoes.readLine();
            
            try {
                socket.close();
                
                if (response.equals("1")) {
                    return true;
                }
            } catch(IOException e) {
                System.out.println("Find close socket: " 
                        + e.getMessage());
            }
            
        } catch (IOException e) {
            System.out.println("Find Error: " 
                    + e.getMessage());
        }
        
        return false;
    }

    
    // add new friend header is 4
    public void addFriend(String name)
    {
        
        try (Socket socket = new Socket("192.168.1.192", 5000)) {
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);

            stringToEcho.println("4" + this.name + "-" + name);
            
            String response = echoes.readLine();
            System.out.println(response);
            
            
            String ip;
            int port;
            int status;
            
            int curPos = 0;
            // get current status of friend
            status = Character.getNumericValue(response.charAt(curPos));
                 
            while (response.charAt(curPos) != '%') {
                curPos++;
            }
            // cusPor is at %
            // get friend ip
            ip = response.substring(1, curPos);
                
            // get friend listen port
            port = Integer.parseInt(response.substring(curPos + 1));
                
                 
            //Friend newFriend = new Friend(name, ip, port, status);        
            friendList.put(name, new Friend(name, ip, port, status));
            messRecordList.put(name, new MessRecord(name));
            
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
        
    }
    
    
    
    @Override    
    public void run()
    {
        
        try(ServerSocket serverSocket = new ServerSocket(this.receivedPort)) {
            while (true) {
                Socket socket = serverSocket.accept();

                try {

                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = 
                            new PrintWriter(socket.getOutputStream(), true);

                    String receivedMess = input.readLine();
                    
                    this.setMess(receivedMess);
                    //System.out.println(this.getMess());
                    
                    int curPos = 0;
                    while (receivedMess.charAt(curPos) != '%') {
                        curPos++;
                    }
                    String friendName = receivedMess.substring(0, curPos);
                    
                    MessRecord record = messRecordList.get(friendName);
                    record.addNumOfMess(1);
                    record.addMess(receivedMess.substring(curPos + 1), 0);
                    
                    chatUI.displayMess(friendName);
                    
                    System.out.println(record.getMessList().get(record.getNumOfMess()-1));

                    output.println("Received");

                } catch(IOException e) {
                    System.out.println("Oops: " + e.getMessage());
                } catch (BadLocationException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println("Server exception " + e.getMessage());
            }
        //}
    }
            
}
