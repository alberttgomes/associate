package com.partner.api;

import com.partner.api.exception.CompanyNotFound;
import com.partner.model.Benefit;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface BenefitService {

    Benefit addBenefit(
            String benefitName, String benefitStatus, String BenefitResources);

    List<Benefit> fetchAllBenefitsByType(long companyId, String type)
        throws CompanyNotFound;

}
