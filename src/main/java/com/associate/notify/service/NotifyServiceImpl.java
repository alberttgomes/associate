package com.associate.notify.service;

import com.associate.notify.api.NotifyService;
import com.associate.notify.api.exception.WhatsAppException;
import com.associate.notify.engineer.mail.MailSender;
import com.associate.notify.engineer.whatsapp.WhatsAppSender;
import com.associate.notify.model.Notify;
import com.associate.notify.persistence.NotifyPersistence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    public NotifyServiceImpl(
            MailSender mailSender, NotifyPersistence notifyPersistence,
            @Qualifier("whatsAppTwilioSender") WhatsAppSender whatsAppSender) {

        this._mailSender = mailSender;
        this._notifyPersistence = notifyPersistence;
        this._whatsAppSender = whatsAppSender;
    }

    @Transactional
    @Override
    public Notify createNotifyAndSendMailMessage(
            String body, long companyId, String header, String mail,
            String sent, String title, long receiver)
        throws WhatsAppException {

        Notify notify = _createNotify(
                companyId, body, header, sent, title, receiver);

        sendEmailMessageNotify(
            body, sent, header, title, mail);

        return notify;
    }

    @Transactional
    @Override
    public Notify createNotifyAndSendWhatsAppMessage(
            String body, long companyId, String header, String sent,
            long receiver,String title, String to)
        throws WhatsAppException {

        Notify notify = _createNotify(
                companyId, body, header, sent, title, receiver);

        String messageText =
                notify.getNotifyTitle() + "\n" + notify.getNotifyBody();

        sendWhatsAppMessageNotify(messageText, to);

        return notify;
    }

    @Override
    public void sendEmailMessageNotify(
            String body, String from, String header, String subject,
            String to)
        throws WhatsAppException {

        if (_isValidMail(from) && _isValidMail(to)) {
            _mailSender.sendMail(body, from, subject, to);
        }
    }

    @Override
    public void sendWhatsAppMessageNotify(
            String messageText, String to) throws WhatsAppException {

        if (_isValidPhoneNumber(to)) {
            _whatsAppSender.sendWhatsAppMessage(messageText, to);
        }
        else throw new WhatsAppException(
            "Invalid patterns to number receiver message %s".formatted(to));
    }

    @Override
    public void sendSMSMessageNotify(
            String from, String text, String to) throws WhatsAppException {

        if (_isValidPhoneNumber(to) && _isValidPhoneNumber(from)) {
            System.out.println("Sending SMS message to " + to);
        }
        else throw new WhatsAppException(
            "Invalid patterns to number receiver message %s"
                .formatted(from + " - " + to));
    }

    private Notify _createNotify(
            long companyId, String body, String header, String sent,
            String title, long receiver) {

        Notify notify = new Notify();

        notify.setCompanyId(companyId);
        notify.setCreateDate(new Date());
        notify.setNotifyBody(body);
        notify.setNotifyHeader(header);
        notify.setNotifySent(sent);
        notify.setNotifyTitle(title);
        notify.setReceiver(receiver);

        return  _notifyPersistence.save(notify);
    }

    private boolean _isValidMail(String mail) {
        return mail.matches(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean _isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+\\d{1,3}\\d{8,12}");
    }

    private final MailSender _mailSender;

    private final NotifyPersistence _notifyPersistence;

    private final WhatsAppSender _whatsAppSender;

}
