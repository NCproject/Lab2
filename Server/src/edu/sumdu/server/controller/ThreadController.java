package edu.sumdu.server.controller;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.*;

import edu.sumdu.server.model.*;

public class ThreadController extends Thread {
	private boolean connection;
    private ActionListener controller;
    private ServerModel model;
    private Socket socket;
    private static final Logger log = Logger.getLogger(ThreadController.class);
    private String xmlMessage;
        
    /**
     * Set model
     */
    public void setModel(ServerModel model) {
        if (log.isDebugEnabled())
            log.debug("Method call");
        this.model = model;
    }

    /**
     * Set controller
     */
    public void setController(ActionListener controller) {
        if (log.isDebugEnabled())
            log.debug("Method call");
        this.controller = controller;
    }
    
    /**
     * Starting new thread for every client
     */
    public ThreadController(Socket s, ActionListener controller, ServerModel model) throws IOException, ServerException {
        if (log.isDebugEnabled())
                log.debug("Method call");
        setController(controller);
        setModel(model);
        socket = s;
        start();
    }
    
    @Override
    public void run() {
        try {
            connection = true;
            while(connection) {
                reading();
                parsing(xmlMessage);
            }
        } catch (Exception exc) {
            DataOutputStream out = null;
            try {
                log.error("Exception", exc);
                out = new DataOutputStream(socket.getOutputStream());
                exceptionHandling(exc);
                out.writeUTF(createMessage("Server was closed"));
            } catch (IOException e) {
                log.error("Exception", e);
            } finally {
                if (!(out == null)) {
                    try {
                        out.flush();
                    } catch (IOException e) {
                        log.error("Exception", e);
                    }
                }
            }
        }
    }
    
    /**
     * Creating exception message to answer
     */
    public void exceptionHandling(Exception ex) {
        if (log.isDebugEnabled())
            log.debug("Method call. Arguments: " + ex);
    }

    /**
     * Getting message from client throw InputStream exception
     */
    private void reading() throws IOException {
        if (log.isDebugEnabled())
            log.debug("Method call");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        try {
            xmlMessage = in.readUTF();
        } catch (IOException e) {
            log.error("Exception", e);
            throw new IOException(e);
        }
    }

    /**
     * Parsing client message according to action
     *
     * @throws ServerException
     */
    private void parsing(String xmlMessage)
            throws ParserConfigurationException, IOException, SAXException,
            ServerException {
        if (log.isDebugEnabled())
            log.debug("Method call. Arguments: " + xmlMessage);
        DataOutputStream out = null;
        try {
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlMessage));
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is);

            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            XPathExpression expr2 = xPath.compile("//envelope/*");

            Object result = expr2.evaluate(doc, XPathConstants.NODESET);
            NodeList xDoc = (NodeList) result;
            Element xHeader = (Element) xDoc.item(0);           
            String action = xPath.evaluate("//action", xHeader);
            
            out = new DataOutputStream(socket.getOutputStream());

            if ("TEST".equals(action)) {
                out.writeUTF(createMessage("TEST IS OK!"));
            } else if ("EXIT".equals(action)) {
                connection = false;
            }             
        } catch (Exception e) {
            log.error("Exception", e);
            throw new ServerException(e);
        } finally {
            if (!(out == null)) {
                out.flush();
            }
        }
    }    

    /**
     * Creating request for update command
     */
    private String createMessage(String message) {
        if (log.isDebugEnabled())
            log.debug("Method call");
        StringBuilder builder = new StringBuilder();
        builder.append("<envelope><header><action>TEST</action></header><body><message>");     
        builder.append(message);        
        builder.append("</message></body></envelope>");        

        return builder.toString();
    }
}
