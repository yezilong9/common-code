package com.yz.app;

import com.yz.config.EmailFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailSender {

    private static final int DEFAULT_COUNT = 3;

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);


    public static boolean send(String title, String content, List<String> toList){
        return send(title,content,toList,null,null,DEFAULT_COUNT);
    }


    public static boolean send(String title, String content, List<String> toList, List<String> cc, List<String> files,int repeatCount) {
        if(toList == null || toList.isEmpty()){
            return false;
        }
        try {
            EmailFactory factory = new EmailFactory();
            Session sendMailSession = factory.createSenderSession();
            Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(factory.getConfig().getSenderUsername());
            mailMessage.setFrom(from);
            List<Address> sendTo = new ArrayList<Address>();
            for(String address : toList){
                if (StringUtils.isNotBlank(address)) {
                    sendTo.add(new InternetAddress(address));
                }
            }

            mailMessage.setRecipients(Message.RecipientType.TO, sendTo.toArray(new InternetAddress[sendTo.size()]));
            if (cc != null && !cc.isEmpty()) {
                List<Address> ccAddress = new ArrayList<Address>();
                for (String address : cc) {
                    ccAddress.add(new InternetAddress(address));
                }
                mailMessage.setRecipients(Message.RecipientType.CC, ccAddress.toArray(new InternetAddress[ccAddress.size()]));
            }
            mailMessage.setSubject(title);
            mailMessage.setSentDate(new Date());
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            //添加附件
            if (files != null && !files.isEmpty()) {
                for (String fileName : files) {
                    html = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileName); // 得到数据源
                    html.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
                    try {
                        html.setFileName(MimeUtility.encodeText(fds.getName()));//附件名有中文
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } // 得到文件名同样至入BodyPart
                    mainPart.addBodyPart(html);
                }
            }

            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            return true;
        } catch (SendFailedException e) {
            e.printStackTrace();
            if (repeatCount > 0) {
                //处理邮箱不存在的情况
                List<String> failAddress = getAddress(e.getInvalidAddresses());
                log.error("邮件发送失败！不存在邮箱：{}", failAddress);
                List<String> successAddress = new ArrayList<String>();
                for (String address : toList) {
                    if (!failAddress.contains(address)) {
                        successAddress.add(address);
                    }
                }
                repeatCount--;
                send(title,content,successAddress,cc,files,repeatCount);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
            log.error("发送错误！",ex);
        }
        return false;
    }


    private static List<String> getAddress(Address[] sourceArray){
        List<String> address = new ArrayList<String>();
        for(Address as : sourceArray){
            address.add(as.toString());
        }
        return address;
    }
}
