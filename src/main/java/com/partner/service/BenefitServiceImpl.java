package com.partner.service;

import com.partner.api.BenefitService;
import com.partner.api.exception.BenefitNotFound;
import com.partner.api.exception.CompanyNotFound;
import com.partner.model.Benefit;
import com.partner.persistence.BenefitPersistence;
import com.partner.service.util.CompanyDynamicQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class BenefitServiceImpl implements BenefitService {

    @Autowired
    public BenefitServiceImpl(
            BenefitPersistence benefitPersistence, CompanyDynamicQuery companyDynamicQuery) {

        this._benefitPersistence = benefitPersistence;
        this._companyDynamicQuery = companyDynamicQuery;
    }

    @Override
    public Benefit addBenefit(
            String benefitName, String benefitStatus, String benefitResources,
            String benefitCategory, long companyId)
        throws CompanyNotFound {

        if (_companyDynamicQuery.hasCompany(companyId)) {
            Benefit benefit = new Benefit();

            benefit.setBenefitName(benefitName);
            benefit.setBenefitStatus(benefitStatus);
            benefit.setBenefitResources(benefitResources);
            benefit.setBenefitCategory(benefitCategory);
            benefit.setCompanyId(companyId);

            return _benefitPersistence.save(benefit);
        }
        else throw new CompanyNotFound(
            "Unable to create Benefit. Company not found with primary key %s"
                    .formatted(companyId));
    }

    @Override
    public Benefit fetchBenefitById(long benefitId) throws BenefitNotFound {
        Optional<Benefit> benefitOptional = _benefitPersistence.findById(benefitId);

        return benefitOptional.orElse(null);
    }

    @Override
    public Benefit fetchBenefitByName(String name) throws BenefitNotFound {
        return _benefitPersistence.findByBenefitName(name);
    }

    @Override
    public List<Benefit> fetchAllBenefitsByAssociateType(
            String associateType, long companyId) {

        if (_companyDynamicQuery.hasCompany(companyId)) {
            List<Benefit> allBenefits =
                    _benefitPersistence.findAllByCompanyId(companyId);

            List<Benefit> benefitsByCategory = new ArrayList<>();

            for (Benefit benefit : allBenefits) {
                if (benefit.getBenefitCategory().equals(associateType)) {
                    benefitsByCategory.add(benefit);
                }
            }

            return benefitsByCategory;
        }

        return new ArrayList<>();
    }

    private final BenefitPersistence _benefitPersistence;

    private final CompanyDynamicQuery _companyDynamicQuery;

}
