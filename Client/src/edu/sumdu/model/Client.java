package edu.sumdu.model;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.log4j.Logger;

import edu.sumdu.exception.*;
import edu.sumdu.model.*;


public class Client {
	/** The logger. */
    private static final Logger log = Logger.getLogger(Client.class);
    private DataOutputStream out;
    private DataInputStream in;
   
    private String serverTest = "Server testing"; 
    
    /** Answer from server side. */
    private String xmlResult;   

    /**
     * Send message to server
     */
    private void sendMessage(String message) throws ClientException {
        if (log.isDebugEnabled()){
            log.debug("Called send message");
        }
        try { 
            out = new DataOutputStream(SocketSingleton.getSocket().getOutputStream());
            out.writeUTF(message);
        } catch (IOException e) {
            throw new ClientException(e);
        } 
    }

    /**
     * Create new message according to ACTION
     */
    private String createMessage(String ACTION, String testMessage) {
        if (log.isDebugEnabled()){
            log.debug("Creating SOAP message called");
        }
        StringBuilder message = new StringBuilder();
        message.append("<envelope><header><action>");
        message.append(ACTION);
        message.append("</action></header><body>");
        if ("TEST".equals(ACTION)) {
            message.append("<message>");
            message.append(testMessage);   
            message.append("</message>");
        }     
        message.append("</body></envelope>");
        return message.toString();
    }

    /**
     * Reading answer from server
     */
    private String reading() throws ServerException {
        if (log.isDebugEnabled()){
            log.debug("Reading stream called");
        }
        try {
            in = new DataInputStream(SocketSingleton.getSocket().getInputStream());
            xmlResult = in.readUTF();
        } catch (Exception e) {
            throw new ServerException(e);
        }
    return xmlResult;
}

    /**
     * Parsing server answer according to ACTION
    */
    private void parsingAnswer(String xmlResult) throws ServerException {
        if (log.isDebugEnabled()){
            log.debug("Parsing answer called");
        }
        try{
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlResult));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);    
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            XPathExpression expr = xPath.compile("//envelope/*");
        
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList xDoc = (NodeList) result;
        
            NodeList xHeader = (NodeList) xDoc.item(0);
            NodeList xBody = (NodeList) xDoc.item(1).getFirstChild();
            String action = xPath.evaluate("//action", xHeader);
        
            if ("TEST".equals(action)) {
            	serverTest = xPath.evaluate("//message", xBody);
            } 
        }catch(XPathExpressionException e){
            throw new ServerException(e);
        }catch(ParserConfigurationException e){
            throw new ServerException(e);
        }catch(SAXException e){
            throw new ServerException(e);
        }catch(IOException e){
        throw new ServerException(e);
        }
    }
    
    /**
     * Return list of groups
     */
    public String getTest() throws ServerException,ClientException {
        if (log.isDebugEnabled()){
            log.debug("Called get update");
        }
        sendMessage(createMessage("TEST", serverTest));
        parsingAnswer(reading());
        return serverTest;
    }
}