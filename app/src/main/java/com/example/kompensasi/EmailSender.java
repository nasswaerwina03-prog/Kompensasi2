package com.example.kompensasi;

import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Boolean> {

    private String email;
    private String subject;
    private String message;
    private EmailCallback callback;

    public interface EmailCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public EmailSender(String email, String subject, String message, EmailCallback callback) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            final String senderEmail = "aplikasikompensasi@gmail.com";
            final String senderPassword = "ejfe wdyn cpfw umui";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderEmail, senderPassword);
                        }
                    });

            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(e);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(e);
            }
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success && callback != null) {
            callback.onSuccess();
        }
    }
}
