package org.dasilva.webapp.mail.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dasilva.webapp.mail.bean.MailConfig;

/**
 * JAXB MessageBodyReader that implements the MailConfig.xsd file
 * This allows JAXB to use the MailConfig schema file
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MailConfigSchemaValidator implements MessageBodyReader<MailConfig> {
	
	@Context
    protected Providers providers;
    private Schema schema;
    
    /**
     * Constructor of the class that reads the MailConfig.xsd file for the schema instance
     */
    public MailConfigSchemaValidator() {
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = sf.newSchema(this.getClass().getResource("MailConfig.xsd"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Overrides the isReadable() so that it return the jaxb MailConfig type
     */
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotation, MediaType mediaType) {
		return type == MailConfig.class;
	}

	/**
	 * Overrides the readForm() so that it uses the jaxb MailConfig type
	 */
	@Override
	public MailConfig readFrom(Class<MailConfig> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		
		try {
			JAXBContext jaxbContext = null;
			ContextResolver<JAXBContext> resolver = providers.getContextResolver(JAXBContext.class, mediaType);
			if(null != resolver) {
				jaxbContext = resolver.getContext(type);
			}
			if(null == jaxbContext) {
				jaxbContext = JAXBContext.newInstance(type);
			}
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(schema);
			return (MailConfig) unmarshaller.unmarshal(entityStream);
		} catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
