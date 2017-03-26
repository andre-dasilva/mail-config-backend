package org.dasilva.webapp.mail.rs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.dasilva.webapp.mail.bean.MailConfig;
import org.dasilva.webapp.mail.util.MailConfigMessageHandler;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

/**
 * RESTful webservices to save, update, get and delete mail configurations from a mongo database
 * The libraries used for this project can be found in the pom.xml file
 * 
 * TODO maybe extend with XML support
 * 
 * @author andre
 */
@Path("/providers")
public class MailConfigServiceRS {
	
	@Context UriInfo uriInfo;
	@Context ServletContext context;
		
	// Initialize logger for the class
	private Logger logger = LogManager.getLogger(this.getClass().getName());
	
	/**
	 * Save or update a mail configuration to/in the database
	 * 
	 * @param provider Provider name like gmail, hotmail, gmx, etc... as PathParam
	 * @param mailConfig MailConfig bean object
	 * @return JSON response with http code: 201 (CREATED) or 200 (OK) or 500 (INTERNAL_SERVER_ERROR)
	 */
	@PUT
	@Path("{provider}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveOrUpdateMailConfig(@PathParam("provider") String provider, MailConfig mailConfig) {
		Datastore datastore = (Datastore) context.getAttribute("db");
		mailConfig.setProvider(provider.toLowerCase());
		
		URI createdResource = null;
		try {
			createdResource = new URI(uriInfo.getAbsolutePath().toString().toLowerCase());
		} catch (URISyntaxException ex) {
			logger.error("saveOrUpdateMailConfig() -> Error while creating URI for provider " + provider + ": " + ex.getMessage());
			ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while creating URI for provider " + provider, ex.getMessage()
			);
    		return response.build();
		}
		
    	// Query to check if the provider is already in the database
    	Query<MailConfig> providerQuery = datastore.createQuery(MailConfig.class).field("provider").equal(provider.toLowerCase());
    	
    	MailConfig dbMailConfig = null;
    	try {
    		dbMailConfig = providerQuery.get();
    	} catch(Exception ex) {
    		logger.error("getMailConfig() -> Error while getting provider " + provider + " from database: " + ex.getMessage());
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while getting provider " + provider + " from database", ex.getMessage()
			);
    		return response.build();
    	}
    	
    	// If the provider is already in database replace the whole document with the new object
    	if (dbMailConfig != null) { 
    		try {
    			ObjectId currentObjectId = dbMailConfig.getId();
        		mailConfig.setId(currentObjectId);
        		
        		logger.debug("saveOrUpdateMailConfig() -> Updating provider " + provider + " with content: " + mailConfig.toString());
        		datastore.save(mailConfig);
    		} catch(Exception ex) {
    			logger.error("saveOrUpdateMailConfig() -> Error while updating provider " + provider + ": " + ex.getMessage());
    			ResponseBuilder response = MailConfigMessageHandler.createResponse(
					Response.Status.INTERNAL_SERVER_ERROR, "Error while updating provider " + provider + " to the database", ex.getMessage()
				);
        		return response.build();
    		}
    		
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.OK, "Updating provider " + provider + " in the database was successful", mailConfig.toString()
			);
    		return response.build();
    	}
    	
		// Save the MailConfig object to the database
		try {
			logger.debug("saveOrUpdateMailConfig() -> Saving new provider " + provider + " with content: " + mailConfig.toString());
        	datastore.save(mailConfig);
        } catch(Exception ex) {
        	logger.error("saveOrUpdateMailConfig() -> Error while saving provider " + provider + " to the database: " + ex.getMessage());
        	ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while saving provider " + provider + " to the database", ex.getMessage()
			);
    		return response.build();
        }
		
		ResponseBuilder response = MailConfigMessageHandler.createResponse(
			Response.Status.CREATED, createdResource.toString(), "Saving provider " + provider + " to the database was successful", mailConfig.toString()
		);
		return response.build();
    }
	
	/**
	 * Get a mail configuration from the database
	 * 
	 * @param provider Provider name like gmail, hotmail, gmx, etc... as PathParam
	 * @return JSON response with http code: 200 (OK) or 404 (NOT_FOUND) 
	 */
	@GET
	@Path("{provider}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getMailConfig(@PathParam("provider") String provider) {
		Datastore datastore = (Datastore) context.getAttribute("db");
		
    	// Query to search for the provider in the database
    	Query<MailConfig> providerQuery = datastore.createQuery(MailConfig.class).field("provider").equal(provider.toLowerCase());
		
    	MailConfig mailConfig = null;
    	try {
    		mailConfig = providerQuery.get();
    	} catch(Exception ex) {
    		logger.error("getMailConfig() -> Error while getting provider " + provider + " from database: " + ex.getMessage());
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while getting provider " + provider + " from database", ex.getMessage()
			);
    		return response.build();
    	}

    	// Return 404 if provider does not exist in the database
    	if (mailConfig == null) {
    		logger.debug("getMailConfig() -> There is no document with the provider " + provider + " in the database");
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.NOT_FOUND, "There is no mail config for the provider " + provider
			);
    		return response.build();
    	}
		
    	logger.debug("getMailConfig() -> Provider " + provider + " was found in the database with content: " + mailConfig.toString());
    	ResponseBuilder response = Response.status(Response.Status.OK).entity(mailConfig);
    	MailConfigMessageHandler.implementCORS(response);
    	
		return response.build();
    }
    
	/**
	 * Get all mail configurations from the database
	 * 
	 * @return JSON response with http code: 200 (OK)
	 */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMailConfigs() {    	
    	Datastore datastore = (Datastore) context.getAttribute("db");    	
    	List<MailConfig> mailConfigs = null;

    	// Query to get all mail configs from the database
    	Query<MailConfig> providerQuery = datastore.createQuery(MailConfig.class);
    	
    	try {
    		mailConfigs = providerQuery.asList();
    	} catch(Exception ex) {
			logger.error("getAllMailConfigs() -> Error while getting all providers from database: " + ex.getMessage());
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while getting all providers from database", ex.getMessage()
			);
    		return response.build();
    	}
    		
    	logger.debug("getAllMailConfigs() -> Returning all providers from database");
        ResponseBuilder response = Response.status(Response.Status.OK).entity(mailConfigs);
        MailConfigMessageHandler.implementCORS(response);
        
        return response.build();
    }
    
    /**
     * Delete a mail configuration from the database
     * 
     * @param provider Provider name like gmail, hotmail, gmx, etc... as PathParam
     * @return JSON response with http code: 200 (OK) or 404 (NOT_FOUND)
     */
    @DELETE
    @Path("{provider}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMailConfig(@PathParam("provider") String provider) {
    	Datastore datastore = (Datastore) context.getAttribute("db");
    	
    	// Query to search for the provider in the database
    	Query<MailConfig> providerQuery = datastore.createQuery(MailConfig.class).field("provider").equal(provider.toLowerCase());
    	
    	MailConfig mailConfig = null;
    	try {
    		mailConfig = providerQuery.get();
    	} catch(Exception ex) {
    		logger.error("getMailConfig() -> Error while getting provider " + provider + " from database: " + ex.getMessage());
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while getting provider " + provider + " from database", ex.getMessage()
			);
    		return response.build();
    	}
    	
    	// Return 404 if provider does not exist in the database
    	if (mailConfig == null) {
    		logger.debug("deleteMailConfig() -> There is no document with the provider " + provider + " in the database");
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.NOT_FOUND, "There is no mail config for the provider " + provider
			);
    		return response.build();
    	}
    	
    	// Delete document with the provider form the database
    	try {	
    		logger.debug("deleteMailConfig() -> Deleting provider " + provider + " from the database");
	    	datastore.delete(mailConfig);
    	} catch(Exception ex) {
    		logger.error("deleteMailConfig() -> Error while deleting provider " + provider + ": " + ex);
    		ResponseBuilder response = MailConfigMessageHandler.createResponse(
				Response.Status.INTERNAL_SERVER_ERROR, "Error while deleting provider " + provider
			);
    		return response.build();
    	}

    	ResponseBuilder response = MailConfigMessageHandler.createResponse(
			Response.Status.OK, "Deleting provider " + provider + " from the database was successful"
		);
		return response.build();
    }
    
    /**
     * Needed for preflightRequest (OPTIONS Requests in Chrome, Firefox, ... not IE) to use cross-domain requests
     * Chrome and Firefox first makes a OPTIONS preflight request when you call PUT, POST or DELETE
     * 
     * @return The response for the preflight request
     */
    @OPTIONS
    @Path("{provider}")
	public Response preflightRequestCORS() {     
		ResponseBuilder response = Response.status(200);
		MailConfigMessageHandler.implementCORS(response);
        return response.build();		
	}
}


