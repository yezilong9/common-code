package com.yz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailFactory {

    private static final Logger log = LoggerFactory.getLogger(EmailFactory.class);

    private static EmailConfig config = null;

    /**
     * 获取发送邮箱的session
     * @return
     */
    public Session createSenderSession(){
        Session session = null;
        try{
            EmailAuthenticator authenticator = null;
            Properties pro = getConfig().getProperties();
            if (getConfig().isValidate()) {
                authenticator = new EmailAuthenticator(getConfig().getSenderUsername(),
                        getConfig().getSenderPassword());
            }
            session = Session
                    .getDefaultInstance(pro, authenticator);
        }catch (Exception e){
            log.error("获取session出错！",e);
        }
        return session;
    }

    public Store getReceiverStore(String protocol){
        Store store = null;
        try {
            EmailAuthenticator authenticator = null;
            Properties pro = getConfig().getProperties();
            if (getConfig().isValidate()) {
                authenticator = new EmailAuthenticator(getConfig().getReceiverUsername(),
                        getConfig().getReceiverPassword());
            }
            Session session = Session
                    .getDefaultInstance(pro, authenticator);
            store = session.getStore(protocol);
            store.connect(getConfig().getReceivHost(), getConfig().getReceiverUsername(), getConfig().getReceiverPassword());
        } catch (NoSuchProviderException e) {
            log.error("获取store出错！",e);
        } catch (MessagingException e) {
            log.error("获取store出错！", e);
        }
        return store;
    }

    public EmailConfig getConfig(){
        if(config == null){
            config = new EmailConfig();
        }
        return config;
    }
}
