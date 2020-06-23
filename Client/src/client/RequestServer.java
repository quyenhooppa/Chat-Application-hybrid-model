/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientUI.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author quyenhooppa
 */
public class RequestServer extends Thread{
    private User user;
    private String nameAdding;
    private String serverIp; // server ip
    private Socket socket;
    private int typeOfRequest;
    private RegisterUI registerUI;
    private LoginUI loginUI;

    public RequestServer(User user) {
        this.user = user;
        this.serverIp = "172.20.10.8";
    }
    

    //--------------- SETTER ---------------
    public void setTypeOfRequest(int typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }

    public void setNameAdding(String nameAdding) {
        this.nameAdding = nameAdding;
    }

    public void setRegisterUI(RegisterUI registerUI) {
        this.registerUI = registerUI;
    }

    public void setLoginUI(LoginUI loginUI) {
        this.loginUI = loginUI;
    }
    
    
    
    
    //--------------- GETTER ---------------

    public int getTypeOfRequest() {
        return typeOfRequest;
    }
    
    

    
    @Override
    public void run() {
        try {
            
            socket = new Socket(serverIp, 5000); 
            boolean online = true;
            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = 
                    new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println(this.getName());
            
            while(true) {
                
                switch (typeOfRequest) {
                    case 1: // register
                        if (register(echoes, stringToEcho)) {
                            registerUI.dispose();
                        } else {
                            registerUI.getUserName().setText("");
                            registerUI.getPassword().setText("");
                            registerUI.getRePassword().setText("");
                        }
                        online = false;
                        break;
                    case 2: // login
                        online = login(echoes, stringToEcho);
                        typeOfRequest = 6;
                        break;
                    case 3: // find user
                        findUser(nameAdding, echoes, stringToEcho);
                        typeOfRequest = 6;
                        break;
                    case 4: // add friend
                        addFriend(nameAdding, echoes, stringToEcho);
                        typeOfRequest = 6;
                        break;
                    case 5: // logout
                        logOut(echoes, stringToEcho);
                        online = false;
                        break;
                    case 6: // receive notification from server    
                        int newRequest = typeOfRequest;
                        if (typeOfRequest != newRequest) {
                            typeOfRequest = newRequest;
                        } else {
                            if (echoes.ready()) {
                                updateFriendStatus(echoes);
                            } else {
                                System.out.print("");
                            }
                        }
                        break;
                    default:
                        break;
                }
                
                if (online == false) {
                    System.out.println("OFF");
                    break;
                }
            }
            
            try {
                socket.close();
            } catch(IOException e) {
                System.out.println("Connect Server close socket: " 
                        + e.getMessage());
            } 
            
        } catch (IOException ex) {
            System.out.println("Connect Server: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    
    
    private boolean register(BufferedReader echoes, PrintWriter stringToEcho) {
        
        try {
            
            String send = "1" + user.getUserName() + "-" + user.getPass();
                    
            stringToEcho.println(send);
            
            String response = echoes.readLine();
            System.out.println("Register: " + response);
            
            if (response.equals("1")) {
                JOptionPane.showMessageDialog(null, "Register successfully!");
                return true;
            }
        } catch(IOException ex) {
            System.out.println("Register: " + ex.getMessage());
        }
           
        JOptionPane.showMessageDialog(null, "Register failed! "
                + "Username may has existed or "
                + "Fail to connect to server.");
        return false;
    }
    
    

    public boolean login(BufferedReader echoes, PrintWriter stringToEcho) {
        
        try {
            
            InetAddress host = InetAddress.getLocalHost();
                  
            stringToEcho.println("2" + user.getUserName() + "-" + user.getPass() + 
                    "-" + host.getHostAddress()); // send request

            String response = echoes.readLine();
            System.out.println("Login: " + response + " " + this.getName());
            
            // login failed
            if (response.length() > 1) {
                
                user.setUpData();
                setUpFriendInfo(response);
                user.setClientIp(host.getHostAddress());
                
                new ChatUI(user).setVisible(true); 
                loginUI.dispose();
                
                return true;
            }
            
            if (response.equals("0")) {
                JOptionPane.showMessageDialog(null,
                     "Login failed. Please try again.");
            } else if (response.equals("3")) {
                JOptionPane.showMessageDialog(null,
                     "User has already login. Please try again.");
            } else if (response.equals("2")) {
                JOptionPane.showMessageDialog(null,
                     "User not found. Please try again.");
            }
                   
        } catch (IOException e) {
            System.out.println("Login Error " 
                    + e.getMessage());
        }
        
        loginUI.getUserName().setText("");
        loginUI.getPassword().setText("");

        return false;
    }
    
    

    public void findUser(String name, 
            BufferedReader echoes, PrintWriter stringToEcho) {
        
        try {

            stringToEcho.println("3" + name);
            
            String response = echoes.readLine();
            System.out.println("Find user: " + response + " " + this.getName());
            
            if (!response.equals("0")) { // user found or online
                //chatUI.setReceiverInfo(response);
                RequestFriendUI addfriend = new RequestFriendUI(user, 
                        nameAdding, response, 1);
                addfriend.setVisible(true);
            } else { // user not found
                JOptionPane.showMessageDialog(null, "User not found or is offline");
            }
                   
        } catch (IOException e) {
            System.out.println("Find Error: " 
                    + e.getMessage());
        }
    }

    

    public void addFriend(String name, 
            BufferedReader echoes, PrintWriter stringToEcho) {
        
        try {

            stringToEcho.println("4" + user.getUserName() + "-" + name);
            
            String response = echoes.readLine();
            System.out.println("Add friend: " + response + " " + this.getName());
            
            
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
                 
            user.getFriendList().put(name, new Friend(name, ip, port, status));
            user.getMessRecordList().put(name, new MessRecord());
            System.out.println(name + status);
            user.getChatUI().addName(name, status);
            
        } catch (IOException e) {
            System.out.println("Add Error " 
                    + e.getMessage());
        }
    }
    
    
    public void logOut (BufferedReader echoes, PrintWriter stringToEcho) {
        
        try { 

            stringToEcho.println("5" + user.getUserName());
            
            String response = echoes.readLine();
            System.out.println("Logout: " + response + " " + this.getName());
 
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
    }
    
    
    // update when a friend changes his/her status
    private void updateFriendStatus(BufferedReader echoes) {
        
        try {
            
            String response = echoes.readLine();
            System.out.println(response);
            String friendName = response.substring(1);
            int status = Character.getNumericValue(response.charAt(0));
            
            if (status == 0) {
                user.getMessRecordList().get(friendName).getMessList().clear();
            } 

            user.getFriendList().get(friendName).setStatus(status);
            user.getChatUI().addName(friendName, status);
            status = (status + 1) % 2;
            user.getChatUI().removeName(friendName, status);
            user.getChatUI().updateTextArea();
            
        } catch (IOException ex) {
            System.out.println("Update friend status: " + ex);
        }
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
            user.setReceivedPort(Integer.parseInt(response.substring(0, curPos)));
            
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
                
                 while (response.charAt(curPos) != '%') {
                    curPos++;
                }
                // curPos is at -
                // get port to send
                sentPort = Integer.parseInt(response.substring(pos + 1, curPos));
                    
                pos = curPos; // pos is at -
                curPos++; // next character
                
                // Friend newFriend = new Friend(friendName, sentPort, status);
                Friend newFriend = new Friend(friendName, ip, sentPort, status);
                MessRecord newMessRecord = new MessRecord();
                
               user.getFriendList().put(friendName, newFriend); // add friend
               user.getMessRecordList().put(friendName, newMessRecord); // add mess record
                
            }
    }
}
