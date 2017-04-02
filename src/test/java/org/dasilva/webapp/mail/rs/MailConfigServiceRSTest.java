package org.dasilva.webapp.mail.rs;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.dasilva.webapp.mail.bean.mail.IMAP;
import org.dasilva.webapp.mail.bean.MailConfig;
import org.dasilva.webapp.mail.bean.mail.POP3;
import org.dasilva.webapp.mail.bean.mail.SMTP;
import org.dasilva.webapp.mail.db.MorphiaServletContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Jersey test class to test the MailConfigServiceRS webservices
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
public class MailConfigServiceRSTest extends JerseyTest {
	
	private MailConfig mailConfig = null;
	
	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
		return new GrizzlyWebTestContainerFactory();
	}
	
	@Override
	protected DeploymentContext configureDeployment() {
		 ResourceConfig config = new ResourceConfig(MailConfigServiceRS.class);
		 return ServletDeploymentContext.forServlet(new ServletContainer(config))
                 .addListener(MorphiaServletContext.class)
                 .build();
	}
	
	@Before
	public void setupTestEntity() {
		Response response = target("provider/gmail").request(MediaType.APPLICATION_JSON)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.delete();
		System.out.println("Cleaned Up Database for tests " + response.getStatusInfo());
		
		MailConfig gmail = new MailConfig();
		gmail.setId(new ObjectId("5682bff9b882cc0d70ceced7"));
		gmail.setProvider("gmail");
		gmail.setSmtp(new SMTP("smtp.gmail.com", "tls", 587));
		gmail.setImap(new IMAP("imap.gmail.com", "ssl", 993));
		gmail.setPop3(new POP3("pop.gmail.com", "ssl", 995));
		
		mailConfig = gmail;
	}
	
	@Test
	public void testSuccessfullSaveMapping() {
		Entity<MailConfig> mailEntity = Entity.entity(mailConfig, MediaType.APPLICATION_JSON);
		
		Response response = target("provider/gmail").request(MediaType.APPLICATION_JSON)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.put(mailEntity);
		
		Assert.assertEquals("Response Code must be 201", 201, response.getStatus());
		System.out.println(response.getLocation().toString());
		Assert.assertEquals("http://localhost:9998/provider/gmail", response.getLocation().toString());
		//Assert.assertEquals("{\"statusCode\":201,\"statusType\":\"Created\",\"message\":\"Saving provider gmail to the database was successful\",\"developerMessage\":\"[Object Id=5682bff9b882cc0d70ceced7 | Provider=gmail | IMAP=imap.gmail.com, ssl, 993 | SMTP=smtp.gmail.com, tls, 587 | POP3=pop.gmail.com, ssl, 995]\",\"location\":\"http://localhost:9998/provider/gmail\"}", response.readEntity(String.class));
	}
	 
	@After
	public void cleanUpTests() {
		Response response = target("provider/gmail").request(MediaType.APPLICATION_JSON)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.delete();
		System.out.println("Cleaned Up Database after tests " + response.getStatusInfo());
	}
}
