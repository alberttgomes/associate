package com.associate.api;

import com.associate.api.exception.BenefitNotFound;
import com.associate.api.exception.CompanyNotFound;
import com.associate.model.Benefit;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface BenefitService {

    Benefit addBenefit(
            String benefitName, String benefitStatus, String BenefitResources,
            String benefitCategory, long companyId)
        throws CompanyNotFound;

    Benefit fetchBenefitById(long benefitId) throws BenefitNotFound;

    Benefit fetchBenefitByName(String name) throws BenefitNotFound;

    List<Benefit> fetchAllBenefitsByAssociateType(
            String associateType, long companyId);

}
