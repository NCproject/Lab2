package edu.sumdu.model;

import java.io.*;
import edu.sumdu.exception.*;
import java.net.*;

public class SocketSingleton {
    private static int serverPort = 7070;
    private static String address = "127.0.0.1";
    private static Socket socket;
    
    private SocketSingleton() {
    }

    /**
     * Create socket if it doesnt exist yet and return it
     */
    public static Socket getSocket() throws ClientException {
        if (socket==null) {
            try{
                InetAddress ipAddress = InetAddress.getByName(address);
                socket = new Socket(ipAddress, serverPort);
            } catch (IOException e) {
                throw new ClientException("Somesthing wrong with socket",e);
            }
        }
        return socket;
    }
}