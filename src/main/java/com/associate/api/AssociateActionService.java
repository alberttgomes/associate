package com.associate.api;

import com.associate.api.exception.AssociateNotFound;
import com.associate.api.exception.BenefitNotFound;
import com.associate.api.exception.CompanyNotFound;
import com.associate.model.Associate;
import com.associate.model.Benefit;
import com.associate.model.Notify;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface AssociateActionService {

    List<Benefit> fetchAllBenefits(long companyId) throws BenefitNotFound;

    List<Benefit> fetchAllBenefitByAssociateId(long associateId, long companyId)
    throws AssociateNotFound, CompanyNotFound;

    Benefit fetchBenefit(long associateId, long benefitId, long companyId)
        throws AssociateNotFound, BenefitNotFound;

    List<Notify> getNotifiesByAssociateId(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound;

    String identifierAssociate(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound;

    Notify notifyAssociate(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifyTitle, long receiver)
        throws AssociateNotFound;

    Associate reactivePlanAssociate(
            long associateId, String status, String type) throws AssociateNotFound;

    String suspendPlanAssociate(long associateId, String reason)
        throws AssociateNotFound;

    void updateAssociateType(
            long associateId, String oldType, String newType)
        throws AssociateNotFound;

}
