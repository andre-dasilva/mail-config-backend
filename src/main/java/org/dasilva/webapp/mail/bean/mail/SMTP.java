package org.dasilva.webapp.mail.bean.mail;

import org.mongodb.morphia.annotations.Embedded;

/**
 * This bean class is for the smtp configuration
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@Embedded
public class SMTP extends Setting {
    public SMTP() {
        super();
    }

    public SMTP(String mail, String secure, int port) {
        super(mail, secure, port);
    }
}
