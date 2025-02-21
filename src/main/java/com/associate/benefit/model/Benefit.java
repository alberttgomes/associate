package com.associate.benefit.model;

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
public class Benefit {

    public Long getBenefitId() {
        return benefitId;
    }

    public void setBenefitId(long benefitId) {
        this.benefitId = benefitId;
    }

    public String getBenefitCategory() {
        return benefitCategory;
    }

    public void setBenefitCategory(String benefitCategory) {
        this.benefitCategory = benefitCategory;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public String getBenefitStatus() {
        return benefitStatus;
    }

    public void setBenefitStatus(String benefitStatus) {
        this.benefitStatus = benefitStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getBenefitResources() {
        return benefitResources;
    }

    public void setBenefitResources(String benefitResources) {
        this.benefitResources = benefitResources;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Date getModifierDate() {
        return modifierDate;
    }

    public void setModifierDate(Date lastUpdateDate) {
        this.modifierDate = lastUpdateDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitId;

    @Column(nullable = false, length = 50)
    private String benefitCategory;

    @Column(length = 30, nullable = false, unique = true)
    private String benefitName;

    @Column(length = 15, nullable = false)
    private String benefitStatus;

    @Column(nullable = false, length = 15000)
    private String benefitResources;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private long companyId;

    @Column(nullable = false)
    private Date modifierDate;

}
