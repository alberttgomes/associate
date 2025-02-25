package com.associate.benefit.api;

import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.benefit.model.BenefitResource;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.benefit.model.Benefit;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface BenefitService {

    Benefit addBenefit(
            String benefitName, String benefitStatus, String BenefitResources,
            String benefitCategory, long companyId)
        throws CompanyNotFound;

    Benefit createBenefitAndMetaData(
            String benefitCategory, String benefitName, String benefitStatus,
            long companyId, String metaDataResources)
        throws CompanyNotFound;

    List<Benefit> fetchAllBenefits(long companyId) throws CompanyNotFound;

    List<Benefit> fetchAllBenefitsByAssociateType(
            String associateType, long companyId);

    Benefit fetchBenefitById(long benefitId) throws BenefitNotFound;

    Benefit fetchBenefitByName(String name) throws BenefitNotFound;

    BenefitResource fetchBenefitResourcesLatestVersion(long benefitId)
        throws BenefitNotFound;

    Benefit updateBenefit(
            long benefitId, String benefitCategory,String benefitName, String benefitStatus,
            String benefitResources, long companyId)
        throws BenefitNotFound;

}
