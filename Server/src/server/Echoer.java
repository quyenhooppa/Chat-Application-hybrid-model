/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author quyenhooppa
 */

public final class Echoer extends Thread {
    private Socket socket;
    private static String userName;
    private ListOfUser userList;
    private PrintWriter output;

    public Echoer(Socket socket, ListOfUser userList) {
        this.socket = socket;
        this.userList = userList;
        newConnection();
    }
    
    private void newConnection() {
        userName = "";
        this.start();
    }

    public PrintWriter getOutput() {
        return output;
    }

    public String getUserName() {
        return userName;
    }

    
    
    @Override
    public void run() {
        boolean socketOpen = true;
        System.out.println(this.getName());
        while (true) {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            String echoString = input.readLine();
            System.out.println("Received client input: " + echoString);

            char c = echoString.charAt(0);

            switch (c) {
                case '1': // request register

                    register(echoString, output);  
                    socketOpen = false;
                    break;
                case '2': // request login

                    socketOpen = authenticate(echoString, output);
                    notifyStatus();
                    break;
                case '3': // request send add friend
                    
                    String username = echoString.substring(1);
                    findPerson(username, output);
                    break;
                case '4': // request add friend
                   
                    addFriend(echoString, output);
                    break;
                case '5': // request logout
                    
                    logOut(echoString, output);
                    userName = echoString.substring(1);
//                    System.out.println(userName + ": Logout");
                    notifyStatus();
                    userList.getListOfUserConnetion().remove(userName, this);
                    socketOpen = false;
                    break;
                default:
                    break;
            }

            
        } catch (IOException e) {
            System.out.println("Oops: " + e.getMessage());
            if (!userName.equals("")) {
                try {
                    updateStatus(userName, "0");
                } catch (SAXException ex) {
                    Logger.getLogger(Echoer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Echoer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(Echoer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(Echoer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

        } catch (TransformerException | ParserConfigurationException
                | SAXException ex) {
            Logger.getLogger(Echoer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!socketOpen) {
            break;
        }
        
        }
        try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Server close socket: " + e.getMessage());
            }
        
    }
    
    //Notiffy to user friend if status changed
    public void notifyStatus() throws SAXException, ParserConfigurationException, IOException {
        
        String fileName = "src/data/" + userName + ".xml";
        File userFile = new File(fileName);
        
         DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(userFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            Node nNode = nList.item(0);
            Element elem = (Element) nNode;
           
            NodeList fList = doc.getElementsByTagName("friends");
            Node fNode = fList.item(0);
            Element friendList = (Element) fNode;
            
            Node n1 = elem.getElementsByTagName("status").item(0);
            String status = n1.getTextContent();
            
            int numberOfFriend = friendList.getElementsByTagName("friendname").getLength();

            Node friend;
            String friendName;            
            String friendStatus;
            for (int i = 0; i < numberOfFriend; i++) {
                String message;
                friend = elem.getElementsByTagName("friendname").item(i);
                friendName = friend.getTextContent();
                friendStatus = getFriendStatus(friendName);
                message = status + userName;
                //System.out.println(status + friendName);
                if (friendStatus.equals("1")) {
                    
                    Echoer echoer = userList.getListOfUserConnetion().get(friendName);
//                    System.out.println(friendName + " " + message);
                    echoer.getOutput().println(message);
                }
            }
                
    }
    
//REGISTER HERE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private void register(String echoString, PrintWriter output)
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        String name;
        String pass;

        int curPos = 1;

        while (echoString.charAt(curPos) != '-') {
            curPos++;
        }
        if ((curPos - 1) == 1) {
            name = String.valueOf(echoString.charAt(curPos - 1));
        } else {
            name = echoString.substring(1, curPos);
        }
        curPos++;
        pass = echoString.substring(curPos);
        String fileName = "src/data/" + name + ".xml";
        File myFile = new File(fileName);
        if (myFile.isFile()) {
            output.println("0");
        } else {
            output.println("1"); 
            int port;
            port = getPort(name);
            // write to xml file 
            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElementNS("Information", "info");
            doc.appendChild(root);

            //int port = (Integer.parseInt(ID) % 1000) + 3000;
            Element user = doc.createElement("user");
            user.setAttribute("id", String.valueOf(port - 6000));
            user.appendChild(
                    newElement(doc, "username", name)
            );
            user.appendChild(
                    newElement(doc, "pass", pass)
            );
            user.appendChild(
                    newElement(doc, "port", String.valueOf(port))
            );
            user.appendChild(
                    newElement(doc, "friends", null)
            );
            user.appendChild(
                    newElement(doc, "status", "0")
            );

            root.appendChild(user);

            TransformerFactory transformerFactory
                    = TransformerFactory.newInstance();
            Transformer transf = transformerFactory.newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(myFile);

            transf.transform(source, file);
            
        }
    }
    
//END OF REGISTER%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
///////////////////////////////////////////////////////////////////////////////////////////////////////////////    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////  
//LOGIN HERE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private boolean authenticate(String echoString, PrintWriter output)
            throws SAXException, ParserConfigurationException, IOException, TransformerException {
        String name;
        String pass;
        String ip;
        int curPos = 1;

        while (echoString.charAt(curPos) != '-') {
            curPos++;
        }
        if ((curPos - 1) == 1) {
            name = String.valueOf(echoString.charAt(curPos - 1));
        } else {
            name = echoString.substring(1, curPos);
        }
        curPos++;
        int tracePos = curPos;
        while (echoString.charAt(curPos) != '-') {
            curPos++;
        }

        if ((curPos - 1) == 1) {
            pass = String.valueOf(echoString.charAt(curPos - 1));
        } else {
            pass = echoString.substring(tracePos, curPos);
        }
        curPos++;
        ip = echoString.substring(curPos);
//        System.out.println(name);
//        System.out.println(pass);
//        System.out.println(ip);

        addNewUserIP(ip, name);

        String fileName = "src/data/" + name + ".xml";
        File userFile = new File(fileName);

        if (!userFile.isFile()) {
            System.out.println("Rejected!!!");

            output.println("0");
        } else {

            userName = name;
            userList.getListOfUserConnetion().put(userName, this);
            
            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(userFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            Node nNode = nList.item(0);
            Element elem = (Element) nNode;
            // get password
            Node node1 = elem.getElementsByTagName("pass").item(0);
            String passFile = node1.getTextContent();
            // get listen port of user
            Node node2 = elem.getElementsByTagName("port").item(0);
           
            NodeList fList = doc.getElementsByTagName("friends");
            Node fNode = fList.item(0);
            Element friendList = (Element) fNode;
            
            int numberOfFriend = friendList.getElementsByTagName("friendname").getLength();
            String message = node2.getTextContent();
            // login successfully, send back its listen port
            if (passFile.equals(pass)) {

                System.out.println("Login!!!");
                updateStatus(name, "1");
                message = message + "%" + numberOfFriend + "%";
                Node friend;
                String friendName;
                String friendIP;
                String friendPort;                
                String status;
                for (int i = 0; i < numberOfFriend; i++) {
                    friend = elem.getElementsByTagName("friendname").item(i);
                    friendName = friend.getTextContent();
                    status = getFriendStatus(friendName);
                    friendIP = getFriendIP(friendName);
                    friendPort = getFriendPort(friendName);
                    message = message+ status + friendName + "-" + friendIP + "-" + friendPort + "%";
                }
                output.println(message);
                return true;

            } else {

                System.out.println("Rejected!!!");

                output.println("0");
            }

        }
        return false;

    }
    
    
    public void addNewUserIP(String ip, String name) throws SAXException, ParserConfigurationException, IOException, TransformerException {
        String fileName = "src/data/ip.xml";
        File userFile = new File(fileName);
        boolean flag = true;
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(userFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("ip");
        //Get first element
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        NodeList nE = elem.getElementsByTagName("user");
        int length = nE.getLength();
        for (int i = 0; i < length; i++){
            Node n1 = nE.item(i);
            Element x = (Element) n1;
            Node n2 = x.getElementsByTagName("username").item(0);
            String nameField = n2.getTextContent();
            //System.out.println(nameField);
            if (nameField.equals(name)){
                //System.out.println("Occur");
                flag = false;
            }
        }
        
        if (!flag) {
        } else {
            Element user = doc.createElement("user");

            user.setAttribute("name", name);
            user.appendChild(
                    newElement(doc, "ip", ip)
            );
            user.appendChild(
                    newElement(doc, "username", name)
            );
//        System.out.println("Step 3");

            elem.appendChild(user);

            TransformerFactory transformerFactory
                    = TransformerFactory.newInstance();
            Transformer transf = transformerFactory.newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(userFile);

            transf.transform(source, file);
        }
    }
    
    private String getFriendStatus(String username) throws ParserConfigurationException, SAXException, IOException{
        String result = null;
         String fileName = "src/data/" + username + ".xml";
        File userFile = new File(fileName);
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(userFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("user");
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        // get length
        Node n1 = elem.getElementsByTagName("status").item(0);
        result = n1.getTextContent();
        return result;
    }    
        
    private String getFriendIP(String username) throws SAXException, ParserConfigurationException, IOException{
        String result = null;
        String fileName = "src/data/ip.xml";
        File userFile = new File(fileName);
        boolean flag = true;
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(userFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("ip");
        //Get first element
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        NodeList nE = elem.getElementsByTagName("user");
        int length = nE.getLength();
        for (int i = 0; i < length; i++){
            Node n1 = nE.item(i);
            Element x = (Element) n1;
            Node n2 = x.getElementsByTagName("username").item(0);
            String nameField = n2.getTextContent();
            //System.out.println(nameField);
            if (nameField.equals(username)){
                Node n3 = x.getElementsByTagName("ip").item(0);
                result = n3.getTextContent();
                break;
            }
        }
        return result;
        
    }
    
    public static String getFriendPort(String username) throws SAXException, IOException, ParserConfigurationException{
        String result = null;
         String fileName = "src/data/" + username + ".xml";
        File userFile = new File(fileName);
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(userFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("user");
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        // get length
        Node n1 = elem.getElementsByTagName("port").item(0);
        result = n1.getTextContent();
        return result;
        
    }
    //END OF LOGIN%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //BEGIN OF FINDPERSON%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    public void findPerson(String username, PrintWriter output) throws SAXException, IOException, ParserConfigurationException{
        String fileName = "src/data/" + username + ".xml";
//        System.out.println(username);
        File personFile = new File(fileName);
        if (!personFile.isFile()){
            output.println("0");
            System.out.println("No result");
        }
        else {
            String message = null;
            String status;
            String port;
            String ip;
            status = getFriendStatus(username);
            port = getFriendPort(username);
            ip = getFriendIP(username);
            
            if (status.equals("0")) {
                output.println("0");
            }
            else {
                message = ip + "-" + port;
                System.out.println(message);
                output.println(message);
            }
            
        }
    }
    //END OF FINDPERSON%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    //BEGIN OF ADDFRIEND%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    public void addFriend(String message, PrintWriter output) throws SAXException, IOException, ParserConfigurationException, TransformerException{
        String host;
        String friend;

        int curPos = 1;

        while (message.charAt(curPos) != '-') {
            curPos++;
        }
        if ((curPos - 1) == 1) {
            host = String.valueOf(message.charAt(curPos - 1));
        } else {
            host = message.substring(1, curPos);
        }
        curPos++;
        friend = message.substring(curPos);
        addFriendtoFile(host, friend,output);
        
        
    }
    public void addFriendtoFile(String filename, String friendname, PrintWriter output) throws SAXException, IOException, ParserConfigurationException, TransformerException{
        String fName = "src/data/" + filename + ".xml";
        File hostFile = new File(fName);
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(hostFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("user");
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        
        NodeList fList = elem.getElementsByTagName("friends");
        Node fNode = fList.item(0);
        Element friendlist = (Element) fNode;
        friendlist.appendChild(
                    newElement(doc, "friendname", friendname)
            );
        elem.appendChild(friendlist);

        TransformerFactory transformerFactory
                = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);

        StreamResult file = new StreamResult(hostFile);

        transf.transform(source, file);

        String message = null;
        String ip = getFriendIP(friendname);
        String port = getFriendPort(friendname);
        String status = getFriendStatus(friendname);
        message = status + ip + "%" + port;
        output.println(message);
            
    }
    //END OF ADDFRIEND%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private static int getPort(String name) {

        int port = 0;
        String fileName = "src/data/" + name + ".xml";
        File newFile = new File(fileName);

        if (!newFile.isFile()) {
            File directory = new File("src/data/");
            int userCount = directory.list().length;

            port = userCount + 6000;
        }

        return port;
    }

    private static Node newElement(Document doc, String name, String value) {

        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }
    
     private static void logOut(String echoString, PrintWriter output) throws IOException, ParserConfigurationException, SAXException, TransformerException{
        String name;
        name = echoString.substring(1);
        updateStatus(name, "0");
        output.println("You are logout");
        
     }
    public static void updateStatus(String name, String status) throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
        String fName = "src/data/" + name + ".xml";
        File hostFile = new File(fName);
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(hostFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("user");
        Node nNode = nList.item(0);
        Element elem = (Element) nNode;
        
        NodeList sList = elem.getElementsByTagName("status");
        Node sNode = sList.item(0);
        //System.out.println(status);
        Node newNode = newElement(doc, "status", status);
        elem.replaceChild(newNode, sNode);
        TransformerFactory transformerFactory
                = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);

        StreamResult file = new StreamResult(hostFile);

        transf.transform(source, file);
    }
}

