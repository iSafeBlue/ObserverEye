package org.observer.base.notice;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/3/5 18:27
 */
public class MailNotice {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("application");
    private static final String sendFrom = bundle.getString("email.from");
    private static final String username = bundle.getString("email.username");
    private static final String password = bundle.getString("email.password");
    private static final String host = bundle.getString("email.host");

    public static void sendEmail(String someone, String subject, String content) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.host", host);
        props.setProperty("mail.smtp.auth", "true");

        Authenticator authenticator = new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        };
        Session session = Session.getDefaultInstance(props,authenticator);
        session.setDebug(true);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sendFrom));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(someone));
            message.setSubject(subject);
            message.setContent(content,"text/html;charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            throw e;
        }
    }


}
