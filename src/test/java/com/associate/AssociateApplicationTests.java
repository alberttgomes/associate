package com.associate;

import com.associate.api.AssociateService;
import com.associate.api.CompanyService;

import com.associate.api.exception.AssociateAttributeInvalid;
import com.associate.constants.AssociateConstantStatus;
import com.associate.constants.AssociateConstantType;
import com.associate.model.Associate;
import com.associate.model.Company;

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
