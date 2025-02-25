package com.associate.benefit.service;

import com.associate.benefit.api.BenefitService;
import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.benefit.model.BenefitResource;
import com.associate.benefit.persistence.BenefitResourcesPersistence;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.benefit.model.Benefit;
import com.associate.benefit.persistence.BenefitPersistence;
import com.associate.company.util.CompanyDynamicQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class BenefitServiceImpl implements BenefitService {

    @Autowired
    public BenefitServiceImpl(
            BenefitPersistence benefitPersistence,
            BenefitResourcesPersistence benefitResourcesPersistence,
            CompanyDynamicQuery companyDynamicQuery) {

        this._benefitResourcesPersistence = benefitResourcesPersistence;
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
            benefit.setCreateDate(new Date());
            benefit.setCompanyId(companyId);
            benefit.setModifierDate(new Date());

            return _benefitPersistence.save(benefit);
        }
        else throw new CompanyNotFound(
            "Unable to create Benefit. Company not found with primary key %s"
                    .formatted(companyId));
    }

    @Transactional
    @Override
    public Benefit createBenefitAndMetaData(
            String benefitCategory, String benefitName, String benefitStatus,
            long companyId, String metaDataResources)
        throws CompanyNotFound {

        try {
            Benefit benefit = addBenefit(
                benefitName, benefitStatus, metaDataResources,
                benefitCategory, companyId);

            String metaData = benefit.getBenefitResources();

            if (!metaData.isEmpty() && !metaData.isBlank()) {
                BenefitResource benefitResource = new BenefitResource();

                benefitResource.setBenefitId(benefit.getBenefitId());
                benefitResource.setBenefitName(benefitName);
                benefitResource.setCreateDate(new Date());
                benefitResource.setMetaData(metaData);

                _benefitResourcesPersistence.save(benefitResource);
            }

            return benefit;
        }
        catch (RuntimeException runtimeException) {
            throw new RuntimeException(runtimeException);
        }
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
    public BenefitResource fetchBenefitResourcesLatestVersion(long benefitId)
        throws BenefitNotFound {

        try {
            Benefit benefit = fetchBenefitById(benefitId);

            if (benefit == null) {
                throw new BenefitNotFound(
                    "Unable to get resources for benefit with primary key %s"
                            .formatted(benefitId));
            }

            List<BenefitResource> benefitResources =
                    _benefitResourcesPersistence.findByBenefitId(benefitId);

            return benefitResources.getFirst();
        }
        catch (RuntimeException runtimeException) {
            throw new BenefitNotFound(runtimeException.getMessage());
        }
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
            benefit.setModifierDate(new Date());

            JSONObject jsonObject = new JSONObject(benefitResources);

            if (jsonObject.has("resources")) {
                benefit.setBenefitResources(benefitResources);
            }

            benefit.setCompanyId(companyId);

            return _benefitPersistence.save(benefit);
        }
        else throw new BenefitNotFound(
            "Unable to updateNotify Benefit. Benefit not found with id %s"
                    .formatted(benefitId));
    }

    private final BenefitPersistence _benefitPersistence;

    private final BenefitResourcesPersistence _benefitResourcesPersistence;

    private final CompanyDynamicQuery _companyDynamicQuery;

}
