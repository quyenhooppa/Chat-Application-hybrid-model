/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source_code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class server {
    //private ArrayList<dataPeer> dataPeer = null;	
    //private static ServerSocket server;						
    //private static Socket connection;	
    //private int port = 12345;
    public boolean isStop = false;
    private ServerSocket server;						
    private Socket connection;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public void createNewServer(int port) throws IOException{
        server = new ServerSocket(port);
        (new WaitMessageArrive()).start();
       
    }
    public boolean checkUserInf(String username, String password){
       //search inf here
        
       
        return true;   
    }
    
    public boolean findUser(String name){
        //Find here
        return true;
    }
    public boolean checkExistance(String username){
        //Check if the user already exist or not
        return true;
    }
    public class WaitMessageArrive extends Thread{
        @Override
        public void run(){
            try {     
                getMessage();
            } catch (IOException ex) {
                Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                 System.out.println("Waiting for the client request 1 ");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                 System.out.println("Waiting for the client request 3");
            }
            }
        }
        public void getMessage() throws IOException, ClassNotFoundException{
            while(true){
                while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String messageReturn = "";
            
            int sign = 0;
            String username="";
            String password="";
            String name="";
            switch(message.charAt(0)){
                    case '1':
                        
                        for (int i = 1; i < message.length(); i++){
                            if (message.charAt(i) == '-') 
                                sign = i;
                        }
                        username = message.substring(1, sign);
                        password = message.substring(sign + 1, message.length()); 
                        if (checkUserInf(username, password)){
                            //begin send return message here
                        }
                        else {
                            messageReturn = "0";
                        }
                        System.out.println(username + "\n");
                        System.out.println(password);
                        break;
                    case '2':
                        sign = 0;
                        for (int i = 1; i < message.length(); i++){
                            if (message.charAt(i) == '-') 
                                sign = i;
                        }
                        username = message.substring(1, sign);
                        password = message.substring(sign + 1, message.length()); 
                        if (checkExistance(username)){
                            //Add new user here
                            messageReturn = "1";
                        }
                        else 
                            messageReturn = "0";
                        break;
                    case '3':
                        name = message.substring(1, message.length());
                        if (findUser(name)) 
                            messageReturn = "1";
                        else 
                            messageReturn = "0";
                        break;
                    case '4':
                        break;
                    default:
                        break;
                    
                }
            oos.writeObject(messageReturn);
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) {
                System.out.println("Shutting down Socket server!!");
                //close the ServerSocket object
                server.close();
                break;
            }
        }
       
        }
    }
        
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server newConnection = new server();
        server newPeer = new server();
        newPeer.createNewServer(5000);
        newConnection.createNewServer(9876);
        
        
    }
}
