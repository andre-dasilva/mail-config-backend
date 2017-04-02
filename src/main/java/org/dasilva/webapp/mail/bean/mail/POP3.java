package org.dasilva.webapp.mail.bean.mail;

import org.mongodb.morphia.annotations.Embedded;

/**
 * This bean class is for the pop3 configuration
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@Embedded
public class POP3 extends Setting {
    public POP3() {
        super();
    }

    public POP3(String mail, String secure, int port) {
        super(mail, secure, port);
    }
}
