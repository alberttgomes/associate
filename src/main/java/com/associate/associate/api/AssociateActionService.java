package com.associate.associate.api;

import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.associate.model.Associate;
import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.benefit.model.Benefit;
import com.associate.benefit.model.BenefitResource;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.notify.model.Notify;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface AssociateActionService {

    /**
     * List all benefits with have the same company id
     *
     * @param companyId see @Company
     * @return all benefits by company id
     * @throws BenefitNotFound thrown an exception if an error occurs
     */
    List<Benefit> fetchAllBenefits(long companyId) throws CompanyNotFound;

    /**
     * Return a specific benefit gave that an associate and company id exists
     * and, the benefit exists too
     *
     * @param associateId see @Associate
     * @param benefitId see @Benefit
     * @param companyId see @Company
     * @return a benefit by associateId, benefitId, and companyId
     * @throws AssociateNotFound an exception occurs if the associate not found
     * @throws BenefitNotFound an exception occurs if anyone benefit was found
     */
    Benefit findBenefit(long associateId, long benefitId, long companyId)
        throws AssociateNotFound, BenefitNotFound;

    /**
     * List all benefits that belong the associate id
     *
     * @param associateId see @Associate
     * @param companyId see @Company
     * @return all benefits by associate and company id
     * @throws AssociateNotFound an exception occurs if the associate not found
     * @throws CompanyNotFound an exception occurs if the company not found
     */
    List<Benefit> fetchAllBenefitByAssociateId(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound;

    /**
     * List benefit resource metadata as a map of string
     *
     * @param benefitId see @Benefit
     * @return a metadata as a benefit resources map
     * @throws BenefitNotFound an exception occurs if the benefit not found
     */
    BenefitResource fetchBenefitResource(long benefitId) throws BenefitNotFound;

    /**
     * List all notifies that belong the associate and company id
     *
     * @param companyId see @Company
     * @param receiverId see @Associate
     * @return a list of notifies by associateId, and companyId
     * @throws AssociateNotFound an exception occurs if the associate not found
     * @throws CompanyNotFound an exception occurs if the company not found
     */
    List<Notify> findNotifiesByReceiverId(long companyId, long receiverId)
        throws AssociateNotFound, CompanyNotFound;

    /**
     * Return the Associate's identifier as a string
     *
     * @param associateId see @Associate
     * @param companyId see @Company
     * @return Associate's identifier by associateId, and companyId
     * @throws AssociateNotFound an exception occurs if associate not found
     * @throws CompanyNotFound n exception occurs if company not found
     */
    String identifierAssociate(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound;

    /**
     * Reactive deactivated or suspended associates.
     * Can be possible updateNotify the associate's type
     *
     * @param associateId see @Associate
     * @param status must be updated to approved
     * @param type could be updated if you want
     * @return an associate reactive by associateId, status, and type
     * @throws AssociateNotFound an exceptions occurs if associate not found
     */
    Associate reactivePlanAssociate(
            long associateId, String status, String type) throws AssociateNotFound;

    /**
     * Send notify to other associates or the company himself
     *
     * @param associateId see @Associate
     * @param companyId see @Company
     * @param notifyBody the body of notify as string value
     * @param notifyHeader the header of notify as string value
     * @param notifyTitle the title of notify as string value
     * @param receiver the receiver identifier as long value
     * @return notify that was sent
     * @throws AssociateNotFound an exception occurs if associateId that is sending it is not found.
     */
    Notify sendNotify(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifyTitle, long receiver)
        throws AssociateNotFound;

    /**
     * Suspend associate setting him status to suspend see @AssociateConstantStatus
     *
     * @param associateId see @Associate
     * @param reason a reason must be informed as a string value
     * @return the associate status as suspend if every is ok
     * @throws AssociateNotFound an exception occurs if the associate not found
     */
    String suspendPlanAssociate(long associateId, String reason)
        throws AssociateNotFound;

    /**
     * Remove an associate deleting from system's data and notify the company
     * that associate was shutdown.
     *
     * @param associateId see @Associate entity
     * @param companyId see @Company entity
     * @throws AssociateNotFound an exception occurs if the associate not found
     */
    void shutdown(long associateId, long companyId) throws AssociateNotFound;

    /**
     * Update associate type checking if associate old type is correctly, and
     * if exists an associate with gave associate id
     *
     * @param associateId see @Associate
     * @param oldType old associate's type to replaced by new
     * @param newType new associate's type to replaced
     * @throws AssociateNotFound an exceptions occurs if associate not found
     */
    void updateAssociateCategory(
            long associateId, String oldType, String newType) throws AssociateNotFound;

}
