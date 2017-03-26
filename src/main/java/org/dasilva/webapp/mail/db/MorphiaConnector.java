package org.dasilva.webapp.mail.db;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * This singleton class handles the database connection with morphia
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
public class MorphiaConnector {
	
	// Initialize logger for the class
	private Logger logger = LogManager.getLogger(this.getClass().getName());
	
	private static MorphiaConnector instance = null;
    private Properties mongodbProperties = new Properties();
	
    /**
     * Constructor of the class that loads the properties file
     */
    private MorphiaConnector() {
    	// Turn off MongoDB Logging to the Console
    	java.util.logging.Logger mongoLogger = java.util.logging.Logger.getLogger("org.mongodb.driver");
    	mongoLogger.setLevel(Level.SEVERE);

        try {
        	mongodbProperties.load(this.getClass().getResourceAsStream("/mongodb.properties"));
        } catch (IOException ex) {
        	logger.error("Error while loading properties file for the mongodb: " + ex);
        }
    }
    
    /**
     * Return the MorphiaConnector instance or instantiate a new instance
     * 
     * @return The MorphiaConnector instance
     */
    public static MorphiaConnector getInstance() {
        if (instance == null) {
            return new MorphiaConnector();
        }
        return instance;
    }
    
    /**
     * Creates the datastore for the database, which can then be used to 
     * call the morhpia CRUD methods
     * 
     * @return The datastore for the database
     */
    public Datastore getConnection() {
        final Morphia morphia = new Morphia();

        morphia.mapPackage("org.dasilva.webapp.mail.bean");
        
        MongoClientURI mongoURI = new MongoClientURI(buildMongoURI());
        MongoClient mongoClient = new MongoClient(mongoURI);
        
        final Datastore datastore = morphia.createDatastore(mongoClient, mongoURI.getDatabase());
        datastore.ensureIndexes();
        
        return datastore;
    }
    
    /**
     * Builds the mongodb uri with the properties from the mongodb.properties file
     * 
     * @return The mongodb uri
     */
    private String buildMongoURI() {
    	return String.format("mongodb://%s:%s@%s:%s/%s", 
    			mongodbProperties.getProperty("mongodb.user"),
    			mongodbProperties.getProperty("mongodb.password"),
    			mongodbProperties.getProperty("mongodb.host"),
    			mongodbProperties.getProperty("mongodb.port"),
    			mongodbProperties.getProperty("mongodb.database"));
    }
}


