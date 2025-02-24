package com.associate.notify.api;

import com.associate.notify.api.exception.WhatsAppException;
import com.associate.notify.model.Notify;

/**
 * @author Albert Gomes Cabral
 */
public interface NotifyService {

    Notify createNotifyAndSendMailMessage(
            String body, long companyId, String header, String mail,
            String sent, String title, long receiver)
        throws WhatsAppException;

    Notify createNotifyAndSendWhatsAppMessage(
            String body, long companyId, String header, String sent,
            long receiver,String title, String to)
        throws WhatsAppException;

    void sendEmailMessageNotify(
            String body, String from, String header, String subject,
            String to) throws WhatsAppException;

    void sendWhatsAppMessageNotify(
            String messageText, String to) throws WhatsAppException;

    void sendSMSMessageNotify(
            String from, String text, String to) throws WhatsAppException;

}
