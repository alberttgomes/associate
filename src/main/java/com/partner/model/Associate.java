package com.partner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

/**
 * @author Albert Gomes Cabral
 */
@Entity
public class Associate {

    public Long getAssociateId() {
        return _associateId;
    }

    public void setAssociateId(Long associateId) {
        this._associateId = associateId;
    }

    public String getAssociateName() {
        return _associateName;
    }

    public void setAssociateName(String associateName) {
        this._associateName = associateName;
    }

    public String getAssociateStatus() {
        return _associateStatus;
    }

    public void setAssociateStatus(String associateStatus) {
        this._associateStatus = associateStatus;
    }

    public String getAssociateType() {
        return _associateType;
    }

    public void setAssociateType(String associateType) {
        this._associateType = associateType;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        this._createDate = createDate;
    }

    @Override
    public String toString() {
        return "Associate: { " +
                    "create-date: " + _createDate + "," +
                    "id: " +_associateId + "," +
                    "name: " +_associateName + "," +
                    "status: " +_associateStatus + "," +
                    "type: " +_associateType +
               "}";
    }

    @Id
    private Long _associateId;

    @Column(unique=true, length = 100, nullable = false)
    private String _associateName;

    @Column(length = 25, nullable = false)
    private String _associateStatus;

    @Column(length = 25, nullable = false)
    private String _associateType;

    @Column(length = 50, nullable = false)
    private Date _createDate;

}
