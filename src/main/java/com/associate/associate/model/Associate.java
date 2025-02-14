package com.associate.associate.model;

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
public class Associate {

    public Long getAssociateId() {
        return associateId;
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

    public String getAssociateType() {
        return associateType;
    }

    public void setAssociateType(String associateType) {
        this.associateType = associateType;
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

    @Override
    public String toString() {
        return "associate: {\n" +
                    "\t companyId: " + companyId + ", \n" +
                    "\t create-date: " + createDate + ", \n" +
                    "\t id: " + associateId + ", \n" +
                    "\t name: " + associateName + ", \n" +
                    "\t status: " + associateStatus + ", \n" +
                    "\t type: " + associateType + "\n" +
               "}\n\t";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long associateId;

    @Column(unique=true, length = 100, nullable = false)
    private String associateName;

    @Column(length = 25, nullable = false)
    private String associateStatus;

    @Column(length = 25, nullable = false)
    private String associateType;

    @Column(length = 100, nullable = false)
    private long companyId;

    @Column(length = 50, nullable = false)
    private Date createDate;

}
