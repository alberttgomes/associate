package com.partner.api;

import com.partner.api.exception.AssociateNotFound;
import com.partner.api.exception.BenefitNotFound;
import com.partner.api.exception.CompanyNotFound;
import com.partner.model.Associate;
import com.partner.model.Benefit;
import com.partner.model.Notify;

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
