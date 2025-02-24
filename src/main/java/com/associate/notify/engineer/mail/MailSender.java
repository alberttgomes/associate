package com.associate.notify.engineer.mail;

/**
 * @author Albert Gomes Cabral
 */
public interface MailSender {

    void sendMail(
            String body, String from, String subject, String to);

}
