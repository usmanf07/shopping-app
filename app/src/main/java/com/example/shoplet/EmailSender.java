package com.example.shoplet;

import android.os.AsyncTask;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Void> {

    private String email;
    private String subject;
    private String message;

    public EmailSender(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable Authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable StartTLS

        // Create a Session with the properties and authenticate the user
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("shoplet96@gmail.com", "uzlr zzxk rxxc tfub");
                    }
                });

        try {
            // Create a new MimeMessage
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("your_email@gmail.com")); // Sender's email
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Recipient's email
            mimeMessage.setSubject(subject); // Subject of the email
            mimeMessage.setText(message); // Message content

            // Send the email
            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
