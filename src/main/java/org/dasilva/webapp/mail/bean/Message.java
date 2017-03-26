package org.dasilva.webapp.mail.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This bean class creates the Message object for the MailconfigMessageHandler class
 * 
 * @author	André da Silva &lt;andi.dasilva@hotmail.com&gt;
 * @version	1.0
 * @since	1.0
 */
@XmlType(propOrder = {"statusCode", "statusType", "message", "developerMessage", "location"})
@XmlRootElement(name="mail_config")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Message {
	
	private int statusCode = 0;
	private String statusType = "";
	private String location = "";
	private String message = "";
	private String developerMessage = "";
	
	public Message() {
	}

	@XmlElement(name="statusCode")
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int status) {
		this.statusCode = status;
	}
	
	@XmlElement(name="statusType")
	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	@XmlElement(name="location")
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlElement(name="developerMessage")
	public String getDeveloperMessage() {
		return developerMessage;
	}
	
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	
}
