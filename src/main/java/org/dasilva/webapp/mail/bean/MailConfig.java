package org.dasilva.webapp.mail.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.bson.types.ObjectId;
import org.dasilva.webapp.mail.bean.mail.IMAP;
import org.dasilva.webapp.mail.bean.mail.POP3;
import org.dasilva.webapp.mail.bean.mail.SMTP;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * This bean class creates the MailConfig object and handles 
 * all jaxb and morphia logic
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@Entity(value="providers", noClassnameStored=true)
@XmlType(propOrder = {"provider", "smtp", "imap", "pop3"})
@XmlRootElement(name="mail_config")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MailConfig {
	
    @Id
    private ObjectId id;
    private String provider;
    @Embedded
    private IMAP imap;
    @Embedded
    private SMTP smtp;
    @Embedded
    private POP3 pop3;
    
    public MailConfig() {
	}
    
    @XmlTransient
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
    	this.id = id;
    }

    @XmlElement(name="provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    @XmlElement(name="imap")
    public IMAP getImap() {
        return imap;
    }

    public void setImap(IMAP imap) {
        this.imap = imap;
    }

    @XmlElement(name="smtp")
    public SMTP getSmtp() {
        return smtp;
    }

    public void setSmtp(SMTP smtp) {
        this.smtp = smtp;
    }

    @XmlElement(name="pop3")
    public POP3 getPop3() {
        return pop3;
    }

    public void setPop3(POP3 pop3) {
        this.pop3 = pop3;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Object Id=" + getId());
		sb.append(" | ");
		sb.append("Provider=" + getProvider());
		sb.append(" | ");
		sb.append("IMAP=" + getImap().getMail() + ", " + getImap().getSecure() + ", " + getImap().getPort());
		sb.append(" | ");
		sb.append("SMTP=" + getSmtp().getMail() + ", " + getSmtp().getSecure() + ", " + getSmtp().getPort());
		sb.append(" | ");
		sb.append("POP3=" + getPop3().getMail() + ", " + getPop3().getSecure() + ", " + getPop3().getPort());
		sb.append("]");
		
		return sb.toString();
	}    
}
