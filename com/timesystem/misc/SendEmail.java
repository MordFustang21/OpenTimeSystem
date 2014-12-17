package com.timesystem.misc;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;

public class SendEmail {
    String timecardText;
    String toAddress;
    String fromAddress = "swampdonk@gmail.com";

    public SendEmail(String timecardText, String toAddress) {
        this.timecardText = timecardText;
        this.toAddress = toAddress;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        //TODO: Get email and pass from db or prop file
                        return new javax.mail.PasswordAuthentication(fromAddress, "DJlaird2");
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject("Timecard");
            message.setText(timecardText);

            Transport.send(message);

            JOptionPane.showMessageDialog(new JFrame(), "Email Sent!");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
        }
    }
}
