package com.associate.notify.engineer.whatsapp.client;

import com.associate.notify.engineer.whatsapp.WhatsAppSender;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.Twilio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class WhatsAppTwilioSender implements WhatsAppSender {

    @Override
    public void sendWhatsAppMessage(String to, String messageText) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:" + to),
            new com.twilio.type.PhoneNumber(fromNumber),
            messageText
        ).create();

        System.out.println("Sent message " + message.getSid());
    }

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

}
