package com.associate.notify.engineer.whatsapp;

/**
 * @author Albert Gomes Cabral
 */
public interface WhatsAppSender {

    void sendWhatsAppMessage(String messageText, String to);

}
