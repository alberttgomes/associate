package com.associate.benefit.service;

import com.associate.benefit.api.BenefitService;
import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.benefit.model.Benefit;
import com.associate.benefit.persistence.BenefitPersistence;
import com.associate.company.util.CompanyDynamicQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
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
    public List<Benefit> fetchAllBenefits(long companyId) throws CompanyNotFound {
        if (_companyDynamicQuery.hasCompany(companyId)) {
            Iterable<Benefit> iterable =
                    _benefitPersistence.findAllByCompanyId(companyId);

            List<Benefit> benefits = new ArrayList<>();

            iterable.forEach(benefits::add);

            return benefits;
        }
        else throw new CompanyNotFound(
            "Unable to fetch all Benefits. Company not found with primary key %s"
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

    @Override
    public Benefit updateBenefit(
            long benefitId, String benefitCategory,String benefitName, String benefitStatus,
            String benefitResources, long companyId)
        throws BenefitNotFound {

        if (_companyDynamicQuery.hasCompany(companyId)) return null;

        Benefit benefit = fetchBenefitById(benefitId);

        if (benefit != null) {
            benefit.setBenefitCategory(benefitCategory);
            benefit.setBenefitId(benefitId);
            benefit.setBenefitName(benefitName);
            benefit.setBenefitStatus(benefitStatus);

            JSONObject jsonObject = new JSONObject(benefitResources);

            if (jsonObject.has("resources")) {
                benefit.setBenefitResources(benefitResources);
            }

            benefit.setCompanyId(companyId);

            return _benefitPersistence.save(benefit);
        }
        else throw new BenefitNotFound(
            "Unable to update Benefit. Benefit not found with id %s"
                    .formatted(benefitId));
    }

    private final BenefitPersistence _benefitPersistence;

    private final CompanyDynamicQuery _companyDynamicQuery;

}
