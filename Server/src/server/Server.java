/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author quyenhooppa
 */
public class Server {

    
    public static void main(String[] args) throws ParserConfigurationException, TransformerException{
        ListOfUser userList = new ListOfUser();
        
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            String fileName = "src/data/ip.xml";
            File myFile = new File(fileName);
            if (!myFile.isFile()) {
                DocumentBuilderFactory factory
                        = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                Element root = doc.createElementNS("USER IP", "ip");
                doc.appendChild(root);

                TransformerFactory transformerFactory
                        = TransformerFactory.newInstance();
                Transformer transf = transformerFactory.newTransformer();

                transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transf.setOutputProperty(OutputKeys.INDENT, "yes");

                DOMSource source = new DOMSource(doc);

                StreamResult file = new StreamResult(myFile);

                transf.transform(source, file);
            }
            //InetAddress host = InetAddress.getLocalHost();
            // System.out.println(host.toString() + "\n");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Echoer(socket, userList);
                    System.out.println("New client connected");
                } catch (IOException ex) {
                     System.out.println("Socket created: " + ex);
                }
                
                //System.out.println("OK");

            }

        } catch (IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    } 
}
