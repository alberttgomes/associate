package com.partner;

import com.partner.api.AssociateService;
import com.partner.api.CompanyService;

import com.partner.api.exception.AssociateAttributeInvalid;
import com.partner.constants.AssociateConstantStatus;
import com.partner.constants.AssociateConstantType;
import com.partner.model.Associate;
import com.partner.model.Company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Albert Gomes Cabral
 */
@SpringBootTest
class AssociateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testAddNewAssociateWithValidCompany() {
        Associate associate = _associateService.addAssociate(
                _company.getCompanyId(),
                "Albert Gomes Cabral",
                AssociateConstantStatus.APPROVED,
                AssociateConstantType.GOLD);

        Assert.notNull(
            associate, "testAddNewAssociateWithValidCompany passed.");
    }

    @Test
    void testCheckPatternNamesToAssociateAttributes() {
        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        _company.getCompanyId(), "Miguel Garza",
                        "unknown", AssociateConstantType.GOLD));

        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        _company.getCompanyId(), "Felipe Silva",
                        AssociateConstantStatus.APPROVED, "unknown"));

        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        _company.getCompanyId(), "1234MyNameIs@#1",
                        AssociateConstantStatus.APPROVED,
                        AssociateConstantType.GOLD));
    }

    @BeforeAll
    static void beforeTests() {
        System.out.println("Before Tests");
    }

    @Autowired
    private AssociateService _associateService;

    @Autowired
    @Qualifier("initCompany")
    private Company _company;

    @Autowired
    private CompanyService _companyService;

}
