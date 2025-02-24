package com.associate.notify.engineer.mail.client;

import com.associate.notify.engineer.mail.MailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class DefaultMailSender implements MailSender {

    @Autowired
    public DefaultMailSender(JavaMailSender javaMailSender) {
        this._javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(
            String body, String from, String subject, String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);

        _javaMailSender.send(message);
    }

    private final JavaMailSender _javaMailSender;

}
