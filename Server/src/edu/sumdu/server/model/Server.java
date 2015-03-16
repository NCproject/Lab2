package edu.sumdu.server.model;

import java.sql.*;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;

/**
 * The Class Server, using XML as DB.
 */
public class Server implements ServerModel {
	/** The logger. */
	private static final Logger log = Logger.getLogger(Server.class);

	/** The document. */
	private Document document = null;

	/** The xml path. */
	private String xmlPath;

	/** The dtd path. */
	private String dtdPath;
	
	private String host = "localhost";
	
	private String dbName = "students";
	
	private String user = "root";
	
	private String password = "";
	
	private Connection conn;

	/**
	 * Sets the dtd path.
	 * 
	 * @param dtdPath
	 *            the new dtd path
	 */
	public void setDtdPath(String dtdPath) {
		if (log.isDebugEnabled())
			log.debug("Method call");
		this.dtdPath = dtdPath;
	}

	/**
	 * Instantiates a new server.
	 * 
	 * @param xmlPath
	 *            the path to xml file
	 * @param dtdPath
	 *            the path to dtd file
	 * @throws ServerException
	 *             if can not read xml file
	 */
	private Server(String xmlPath, String dtdPath) throws ServerException {
		if (log.isDebugEnabled())
			log.debug("Construktor call. Arguments: " + xmlPath + " " + dtdPath);
		setXmlPath(xmlPath);
		setDtdPath(dtdPath);
	}

	/**
	 * Instantiates a new server.
	 * 
	 * @throws ServerException
	 *             the server exception
	 */
	public Server() throws ServerException {
		if (log.isDebugEnabled())
			log.debug("Construktor call");
	}

	/**
	 * Sets the xml path.
	 * 
	 * @param xmlPath
	 *            the new xml path
	 */
	public void setXmlPath(String xmlPath) {
		if (log.isDebugEnabled())
			log.debug("Method call");
		this.xmlPath = xmlPath;

	}

	/**
	 * Sets the document.
	 * 
	 * @param document
	 *            the new document
	 */
	private void setDocument(Document document) {
		if (log.isDebugEnabled())
			log.debug("Method call");
		this.document = document;
	}
	
	public void connectToDB(){
		if (log.isDebugEnabled())
			log.debug("Connection to DB");
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Exception", e);        
		}
	}
}
