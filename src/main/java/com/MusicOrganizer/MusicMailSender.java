package com.MusicOrganizer;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.ModelMap;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by user on 1/20/2017.
 */
public class MusicMailSender {

    private JavaMailSender mailSender;
    private SimpleMailMessage templateMessage;
    private ServerProperties properties;
    public static final String TYPE_SPRING = "spring";
    public static final String TYPE_JAVA = "java";

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setMailSender() {
        this.mailSender = new JavaMailSenderImpl();
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void sendMail(String type, String[] recipients, String[] songs, ModelMap modelMap) {
        //business calc
        String messageBody = "";
        String subject = "Music Organizer Plus!";
        for (int index = 0; index < songs.length; index++) {
            messageBody += songs[index] + "\n";
        }
//        List<SongEntity> songs = (ArrayList)modelMap.get("AllSongs");
//        SongEntity[] songInfo = new SongEntity[songs.size()];
//        for (int index = 0; index < songInfo.length; index++) {
//            songInfo[index] = songs.get(index);
//        }
        if(type == TYPE_JAVA) {
            String host = "smtp.gmail.com";
            String password = "Soccer1725";
            String port = "587";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", host);
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.password", password);
            properties.put("mail.smtp.port", port);
            Session session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            String user = "b.peterson@kunzleigh.com";
                            String pass = "Soccer1725";
                            return new PasswordAuthentication(user, pass);
                        }
                    });


            try {
                Address[] addresses = new Address[recipients.length];
                for (int index = 0; index < recipients.length; index++) {
                    addresses[index] = new InternetAddress(recipients[index]);
                }
                MimeMessage message = new MimeMessage(session);
                message.setFrom("b.peterson@kunzleigh.com");
                message.addRecipients(Message.RecipientType.TO, addresses);
                message.setSubject(subject);
                message.setText(messageBody);
                Transport.send(message);
                System.out.println("Message 2 sent successfully");
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        }
        if(type == TYPE_SPRING) {
            properties = new ServerProperties();
            properties.setPort(465);
            System.setProperty("spring.mail.port", "465");
            System.setProperty("spring.mail.properties.mail.smtp.socketFactory.port", "587");

            mailSender = new JavaMailSenderImpl();


            SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
            msg.setFrom("b.peterson@kunzleigh.com");
            msg.setTo("brandonman17@gmail.com");
            msg.setText("Your music will show up here!");
            msg.setSubject("Music Organizer Plus");
            msg.setReplyTo("b.peterson@kunzleigh.com");
            try {
                mailSender.send(msg);
                System.out.println("Message 1 sent successfully");
            } catch (MailException ex) {
                ex.printStackTrace();
            }
        }
    }
}
