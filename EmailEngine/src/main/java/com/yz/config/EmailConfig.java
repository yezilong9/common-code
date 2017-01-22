package com.yz.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailConfig {

    private String senderHost;
    private String senderPort;
    private String receivHost;
    private String receivPort;
    private String senderUsername;
    private String senderPassword;
    private String receiverUsername;
    private String receiverPassword;
    private boolean validate;
    private boolean needSSL;

    public EmailConfig() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/email.properties"));
            this.senderHost = properties.getProperty("mail.sender.host");
            this.senderPort = properties.getProperty("mail.sender.port");
            this.receivHost = properties.getProperty("mail.receiver.host");
            this.receivPort = properties.getProperty("mail.receiver.port");
            this.validate = Boolean.parseBoolean(properties.getProperty("mail.validate"));
            this.senderUsername = properties.getProperty("mail.send.username");
            this.senderPassword = properties.getProperty("mail.send.password");
            this.receiverUsername = properties.getProperty("mail.receiver.username");
            this.receiverPassword = properties.getProperty("mail.receiver.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.host", senderHost);
        props.put("mail.smtp.port", senderPort);
        props.put("mail.smtp.auth", validate ? "true" : "false");

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true"); // If you need to authenticate
        // Use the following if you need SSL
        if(needSSL){
            props.put("mail.smtp.socketFactory.port", this.senderPort);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
        }
        return props;
    }

    public String getSenderHost() {
        return senderHost;
    }

    public void setSenderHost(String senderHost) {
        this.senderHost = senderHost;
    }

    public String getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(String senderPort) {
        this.senderPort = senderPort;
    }

    public String getReceivHost() {
        return receivHost;
    }

    public void setReceivHost(String receivHost) {
        this.receivHost = receivHost;
    }

    public String getReceivPort() {
        return receivPort;
    }

    public void setReceivPort(String receivPort) {
        this.receivPort = receivPort;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverPassword() {
        return receiverPassword;
    }

    public void setReceiverPassword(String receiverPassword) {
        this.receiverPassword = receiverPassword;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public boolean isNeedSSL() {
        return needSSL;
    }

    public void setNeedSSL(boolean needSSL) {
        this.needSSL = needSSL;
    }
}
