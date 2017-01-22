package com.yz.config;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailAuthenticator extends Authenticator {

    private String userName;
    private String password;

    public EmailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName,password);
    }
}
