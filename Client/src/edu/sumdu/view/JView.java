package edu.sumdu.view;

import org.apache.log4j.Logger;
import javax.swing.table.DefaultTableModel;

import edu.sumdu.model.*;
import edu.sumdu.exception.*;

/**
 * The Class JView.
 */
public class JView extends DefaultTableModel {

    /** The client. */
    private static Client client;
    private String test;

    Logger logger = Logger.getLogger(JView.class.getName());
    
    /**
    * main method, which creates logger, and starts program
    */
    public static void main (String[] args) throws ClientException{
        JView run = new JView();
        run.createClient();
        run.testConnection();        
    }
    
    /**
     * gets data from server while first run
     */
    public void testConnection(){
        if (logger.isDebugEnabled()){
            logger.debug("Called method to get data from server");
        }
        try{    
        	test = client.getTest();
        	System.out.println(test);
        }catch (ClientException e){
            logger.error("Can't update data form server",e);           
        }catch (ServerException e){
            logger.error("Can't update data form server",e);            
        }
    }
    
    /**
     * creates new Client
     */
    public void createClient(){
        if (logger.isDebugEnabled()){
            logger.debug("Called client creation");
        }
        client = new Client();        
    }
}
