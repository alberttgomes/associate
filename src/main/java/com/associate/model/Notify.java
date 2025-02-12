package com.associate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

/**
 * @author Albert Gomes Cabral
 */
@Entity
public class Notify {

    public long getNotifyId() {
        return notifyId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNotifyBody() {
        return notifyBody;
    }

    public void setNotifyBody(String body) {
        this.notifyBody = body;
    }

    public String getNotifyHeader(String header) {
        return notifyHeader;
    }

    public void setNotifyHeader(String header) {
        this.notifyHeader = header;
    }

    public String getNotifySent() {
        return notifySent;
    }

    public void setNotifySent(String sent) {
        this.notifySent = sent;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String title) {
        this.notifyTitle = title;
    }

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notifyId;

    @Column(nullable = false)
    private long companyId;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private String notifyBody;

    @Column(nullable = false)
    private String notifyHeader;

    @Column(nullable = false)
    public String notifySent;

    @Column(nullable = false)
    private String notifyTitle;

    @Column(nullable = false)
    private long receiver;

}
