package org.dasilva.webapp.mail.db;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.mongodb.morphia.Datastore;

/**
 * Application Lifecycle Listener implementation class MorphiaServletContext
 * This class creates and destroys the morphia database connection
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
public class MorphiaServletContext implements ServletContextListener {

	/**
	 * Destroys the morphia database connection
	 * 
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent)  { 
    	ServletContext sc = servletContextEvent.getServletContext();
		Datastore morphiaConnector = (Datastore) sc.getAttribute("db");
		morphiaConnector.getMongo().close();
    }

	/**
	 * Initializes the morhphia database connection
	 * 
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
    	ServletContext sc = servletContextEvent.getServletContext();		
		sc.setAttribute("db", MorphiaConnector.getInstance().getConnection());
    }
	
}
