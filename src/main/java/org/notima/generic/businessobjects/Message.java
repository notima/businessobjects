package org.notima.generic.businessobjects;

import java.io.File;
import java.util.List;

public class Message {
    private String body;
    private String subject;
    private String contentType = "text/plain;charset=utf-8";
    private Person sender;
    private Person recipient;
    private File recipientPublicKey;
    private boolean encrypted;
    private boolean signed;
    private List<File> attachemnts;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Person getSender() {
		return sender;
	}

	public void setSender(Person sender) {
		this.sender = sender;
	}

	public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public File getRecipientPublicKey() {
        return recipientPublicKey;
    }
    
    public void setRecipientPublicKey(File recipientPublicKey) {
        this.recipientPublicKey = recipientPublicKey;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public List<File> getAttachemnts() {
        return attachemnts;
    }

    public void setAttachemnts(List<File> attachemnts) {
        this.attachemnts = attachemnts;
    }
}
