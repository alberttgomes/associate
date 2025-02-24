package com.associate.notify.engineer.whatsapp.client;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Albert Gomes Cabral
 */
@Component
public class SMSTwilioSender {

    public void sendSms(String from, String to, String text) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(to), new PhoneNumber(from), text).create();

        System.out.println("Sent message " + message.getSid());
    }

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

}
