package org.dasilva.webapp.mail.util;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.dasilva.webapp.mail.bean.Message;

/**
 * This class creates the json responses for the webservices
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
public class MailConfigMessageHandler {
	
	/**
	 * Create a response with the Message bean so that the webservice output is in json format
	 * 
	 * @param status HTTP code and HTTP type (e.g code=200 and type=OK) 
	 * @param location Resource location
	 * @param message The message for the user
	 * @param developerMessage The message for the developer
	 * @return The json response for the webservice
	 */
	public static ResponseBuilder createResponse(Response.Status status, String location, String message, String developerMessage) {
		
		Message msg = new Message();
		msg.setStatusCode(status.getStatusCode());
		msg.setStatusType(status.getReasonPhrase());
		msg.setLocation(location);
		msg.setMessage(message);
		msg.setDeveloperMessage(developerMessage);
		
		ResponseBuilder rb = Response.status(status).location(URI.create(location)).entity(msg);
		MailConfigMessageHandler.implementCORS(rb);
		
		return rb;
	}
	
	/**
	 * Create a response with the Message bean so that the webservice output is in json format
	 * 
	 * @param status HTTP code and HTTP type (e.g code=200 and type=OK) 
	 * @param message message The message for the user
	 * @param developerMessage The message for the developer
	 * @return The json response for the webservice
	 */
	public static ResponseBuilder createResponse(Response.Status status, String message, String developerMessage) {
		
		Message msg = new Message();
		msg.setStatusCode(status.getStatusCode());
		msg.setStatusType(status.getReasonPhrase());
		msg.setMessage(message);
		msg.setDeveloperMessage(developerMessage);
		
		ResponseBuilder rb = Response.status(status).entity(msg);
		MailConfigMessageHandler.implementCORS(rb);
		
		return rb;
	}
	
	/**
	 * Create a response with the Message bean so that the webservice output is in json format
	 * 
	 * @param status HTTP code and HTTP type (e.g code=200 and type=OK) 
	 * @param message The message for the user
	 * @return The json response for the webservice
	 */
	public static ResponseBuilder createResponse(Response.Status status, String message) {
		
		Message msg = new Message();
		msg.setStatusCode(status.getStatusCode());
		msg.setStatusType(status.getReasonPhrase());
		msg.setMessage(message);
		
		ResponseBuilder rb = Response.status(status).entity(msg);
		MailConfigMessageHandler.implementCORS(rb);
		
		return rb;
	}
	
	/**
	 * Enable cross-domain requests (CORS)
	 * 
	 * @param response The ResponseBuilder object
	 * @return The ResponseBuilder object with CORS headers
	 */
	public static void implementCORS(ResponseBuilder response) {
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, PUT, DELETE, OPTIONS");
		response.header("Access-Control-Allow-Headers", "Content-Type, Accept");
	}
}
