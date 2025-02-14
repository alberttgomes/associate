package com.associate;

import com.associate.associate.api.AssociateService;
import com.associate.company.api.CompanyService;

import com.associate.associate.api.exception.AssociateAttributeInvalid;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.constants.AssociateConstantType;
import com.associate.associate.model.Associate;
import com.associate.company.model.Company;

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
                "albert.gomes@gmail.com", _company.getCompanyId(),
                "Albert Gomes Cabral", AssociateConstantStatus.APPROVED,
                AssociateConstantType.GOLD);

        Assert.notNull(
            associate, "testAddNewAssociateWithValidCompany passed.");
    }

    @Test
    void testCheckPatternNamesToAssociateAttributes() {
        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        "albert.gomes@gmail.com", _company.getCompanyId(),
                        "Miguel Garza", "unknown",
                        AssociateConstantType.GOLD));

        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        "albert.gomes@gmail.com", _company.getCompanyId(),
                        "Felipe Silva", AssociateConstantStatus.APPROVED,
                        "unknown"));

        assertThrows(
                AssociateAttributeInvalid.class,
                () -> _associateService.addAssociate(
                        "albert.gomes@gmail.com", _company.getCompanyId(),
                        "1234MyNameIs@#1", AssociateConstantStatus.APPROVED,
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
