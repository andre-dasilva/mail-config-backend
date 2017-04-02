package org.dasilva.webapp.mail.bean.mail;

public abstract class Setting {
    protected String mail;
    protected String secure;
    protected int port;

    public Setting() {
        this.mail = "";
        this.secure = "";
        this.port = 0;
    }

    public Setting(String mail, String secure, int port) {
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
