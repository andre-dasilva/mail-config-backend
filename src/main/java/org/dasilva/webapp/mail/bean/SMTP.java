package org.dasilva.webapp.mail.bean;

import org.mongodb.morphia.annotations.Embedded;

/**
 * This bean class is for the smtp configuration
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@Embedded
public class SMTP {
	
	private String mail = "";
    private String secure = "";
    private int port = 0;
	
	public SMTP() {
	}
	
	public SMTP(String mail, String secure, int port) {
        this.mail = mail;
        this.secure = secure;
        this.port = port;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
