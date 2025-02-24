package com.associate.associate.model;

import com.associate.associate.util.AssociateObserver;
import com.associate.notify.model.Notify;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Entity
public class Associate implements AssociateObserver {

    public String getAssociateCategory() {
        return associateCategory;
    }

    public void setAssociateCategory(String associateType) {
        this.associateCategory = associateType;
    }

    public String getAssociateEmail() {
        return associateEmail;
    }

    public void setAssociateEmail(String associateEmail) {
        this.associateEmail = associateEmail;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(long associateId) {
        this.associateId = associateId;
    }

    public String getAssociateName() {
        return associateName;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }

    public String getAssociateStatus() {
        return associateStatus;
    }

    public void setAssociateStatus(String associateStatus) {
        this.associateStatus = associateStatus;
    }

    public String getAssociatePhoneNumber() {
        return associatePhoneNumber;
    }

    public void setAssociatePhoneNumber(String associatePhoneNumber) {
        this.associatePhoneNumber = associatePhoneNumber;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public void updateNotify(Notify notify) {
        if (this.associateId.equals(notify.getReceiver())) {
            notifies.add(notify);

            System.out.println(
                "\nNew notify to " + associateName +
                        " from " + notify.getNotifySent());

            System.out.println("Notifies total: " + notifies.size());
        }
    }

    @Override
    public String toString() {
        return "associate: {\n" +
                "\t category: " + associateCategory + "\n" +
                "\t company-id: " + companyId + ", \n" +
                "\t create-date: " + createDate + ", \n" +
                "\t id: " + associateId + ", \n" +
                "\t modified-date: " + modifiedDate + ", \n" +
                "\t name: " + associateName + ", \n" +
                "\t phone-number: " +associatePhoneNumber + ", \n" +
                "\t status: " + associateStatus + ", \n" +
               "}\n\t";
    }

    @Column(length = 25, nullable = false)
    private String associateCategory;

    @Column(length = 100, unique = true)
    private String associateEmail;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long associateId;

    @Column(unique=true, length = 100, nullable = false)
    private String associateName;

    @Column(unique = true, length = 25, nullable = false)
    private String associatePhoneNumber;

    @Column(length = 25, nullable = false)
    private String associateStatus;

    @Column(length = 100, nullable = false)
    private long companyId;

    @Column(length = 50, nullable = false)
    private Date createDate;

    @Column(length = 50, nullable = false)
    private Date modifiedDate;

    private static final List<Notify> notifies = new ArrayList<>();

}
