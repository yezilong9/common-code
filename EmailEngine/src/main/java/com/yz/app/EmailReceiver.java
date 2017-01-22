package com.yz.app;

import com.yz.config.EmailFactory;
import com.yz.filter.EmailContentFilterChain;
import com.yz.filter.SignFilter;
import com.yz.filter.SourceContentFilter;
import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailReceiver {

    private static final Logger log = LoggerFactory.getLogger(EmailReceiver.class);
    private static EmailContentFilterChain filterChain = new EmailContentFilterChain();
    static {
        filterChain.addFilter(new SourceContentFilter());
        filterChain.addFilter(new SignFilter());
    }

    public static void loadEmail(){
        Message[] mss;
        try {
            Folder f = getFolder();
            //首先把所有未读邮件拿出来
//            mss = f.search(new FlagTerm(new Flags(Flags.Flag.RECENT), true));
            mss = f.getMessages();
            log.info("未读邮件数：{}", f.getUnreadMessageCount());
            System.out.println("未读邮件数：" + f.getUnreadMessageCount());
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
            if (mss != null) {
                for (final Message message : mss) {
                    fixedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            load((MimeMessage) message);
                        }
                    });
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private static void load(MimeMessage message) {
        try {
            //判断是否报账单内的邮件
            MimeMessageParser parser = new MimeMessageParser(message).parse();
            //获取邮件的正文内容
            String emailContent = filterChain.doParse(parser.getHtmlContent());
            //读取附件
            if (parser.hasAttachments()) {

                List<DataSource> dataSources = parser.getAttachmentList();

                for (DataSource dataSource : dataSources) {
                    //下载附件到本地
                    try {
//                                    FileUtil.copyFile(dataSource.getInputStream(), savePath, dataSource.getName());
                        log.info("读取附件成功！邮件标题:{}", parser.getSubject());
                    } catch (Exception e) {
                        log.error(" 读取邮件附件失败！", e);
                    }
                }
            }
            String desc = String.format("读取邮件！邮件标题：%s 邮件包含附件%s ", parser.getSubject(), parser.hasAttachments());
            System.out.println(desc);
//                    System.out.println("以下是邮件内容：");
//                    System.out.println(emailContent);
        } catch (IOException e) {
            log.error("邮件解释失败！", e);
        } catch (Exception e) {
            log.error("读取邮件发生错误！", e);
        }
    }


    private static Folder getFolder() throws MessagingException {
        EmailFactory factory = new EmailFactory();
        Store store = factory.getReceiverStore("imap");
        Folder folder = store.getFolder("INBOX");
//        folder.list();
//        Folder f = folder.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        log.info("收件箱邮件数：{}", folder.getMessageCount());
        System.out.println("收件箱："+folder.getName() + " 邮件数:" + folder.getMessageCount());
        return folder;
    }

}
