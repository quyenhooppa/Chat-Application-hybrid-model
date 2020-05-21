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
    public class WaitMessageArrive extends Thread{
        @Override
        public void run(){
            try {     
                getMessage();
            } catch (IOException ex) {
                Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
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
            //write object to Socket
            oos.writeObject("Hi Client "+message);
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
        }
    }
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server newConnection = new server();
        server newPeer = new server();
        newPeer.createNewServer(1234);
        newConnection.createNewServer(9876);
        
    }
}
