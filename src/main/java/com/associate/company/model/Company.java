package com.associate.company.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author Albert Gomes Cabral
 */
@Entity
public class Company {

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyAddressId() {
        return companyAddressId;
    }

    public void setCompanyAddressId(long companyAddressId) {
        this.companyAddressId = companyAddressId;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(length = 100, nullable = false, unique = true)
    private String companyName;

    @Column(length = 100, nullable = false)
    private long companyAddressId;

    @Column(length = 100, nullable = false, unique = true)
    private String companyEmail;

    @Column(length = 100, nullable = false, unique = true)
    private String companyPhone;

}
