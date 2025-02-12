package com.associate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private long companyId;

}
