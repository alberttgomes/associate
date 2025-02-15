package com.associate.associate.service;

import com.associate.associate.api.AssociateActionService;
import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.constants.AssociateConstantType;
import com.associate.associate.model.Associate;
import com.associate.associate.api.AssociateService;
import com.associate.benefit.api.BenefitService;
import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.benefit.model.Benefit;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.notify.model.Notify;
import com.associate.notify.persistence.NotifyPersistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class AssociateActionServiceImpl implements AssociateActionService {
    @Autowired
    public AssociateActionServiceImpl(
            AssociateService associateService, BenefitService benefitService,
            NotifyPersistence notifyPersistence) {

        this._associateService = associateService;
        this._benefitService = benefitService;
        this._notifyPersistence = notifyPersistence;
    }


    @Override
    public List<Benefit> fetchAllBenefits(long companyId) throws CompanyNotFound {
        return _benefitService.fetchAllBenefits(companyId);
    }

    @Override
    public List<Benefit> fetchAllBenefitByAssociateId(long associateId, long companyId)
            throws AssociateNotFound, CompanyNotFound {

        Associate associate =
                _associateService.fetchAssociateById(associateId);

        if (associate == null) {
            return new ArrayList<>();
        }

        return _benefitService.fetchAllBenefitsByAssociateType(
                associate.getAssociateType(), companyId);
    }

    @Override
    public Benefit findBenefit(long associateId, long benefitId, long companyId)
        throws AssociateNotFound, BenefitNotFound {

        if (_associateService.hasAssociateById(associateId, companyId)) {
            return _benefitService.fetchBenefitById(benefitId);
        }
        else throw new AssociateNotFound(
            "Unable to take benefit from the associate. Associate not found with primary key %s"
                    .formatted(associateId));
    }

    @Override
    public List<Notify> findNotifiesByAssociateId(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound {

        if (!_associateService.hasAssociateById(associateId, companyId)) {
            return new ArrayList<>();
        }

        return _notifyPersistence.findByCompanyIdAndReceiver(
                companyId, associateId);
    }

    @Override
    public String identifierAssociate(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound {

        Associate associate = _associateService.fetchAssociateByCompanyId(
                associateId, companyId);

        if (associate == null) {
            return "Associate not found with id %s".formatted(associateId);
        }

        return associate.toString();
    }

    @Override
    public Notify notifyAssociate(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifyTitle, long receiver)
        throws AssociateNotFound {

        Associate associateSent = _associateService.fetchAssociateByCompanyId(
                associateId, companyId);

        if (associateSent == null)
            throw new AssociateNotFound(
                "Unable to take benefits. Associate not found with id %s".formatted(
                        associateId));

        Notify notify = new Notify();

        notify.setCompanyId(companyId);
        notify.setNotifyBody(notifyBody);
        notify.setNotifyHeader(notifyHeader);
        notify.setNotifyTitle(associateSent.getAssociateName());
        notify.setNotifyTitle(notifyTitle);
        notify.setReceiver(receiver);

        return _notifyPersistence.save(notify);
    }

    @Override
    public Associate reactivePlanAssociate(
            long associateId, String status, String type) throws AssociateNotFound {

        Associate associate = _associateService.fetchAssociateById(associateId);

        if (associate == null) {
            throw new AssociateNotFound(
                "Unable to reactive plan, associate with id was not found %s"
                        .formatted(associateId));
        }

        if (status.equals(AssociateConstantStatus.APPROVED) &&
                AssociateConstantType.getAssociateConstantsTypeList().contains(type)) {

            String associateEmail = associate.getAssociateEmail();
            String associateName = associate.getAssociateName();

            return _associateService.updateAssociate(
                    associateId, associateEmail, associateName, status, type);
        }

        return null;
    }

    @Override
    public String suspendPlanAssociate(
            long associateId, String reason) throws AssociateNotFound {

        Associate associate = _associateService.fetchAssociateById(associateId);

        if (associate == null) {
            throw new AssociateNotFound(
                "Unable to update type. Associate not found with id %s"
                        .formatted(associateId));
        }

        String type = associate.getAssociateType();

        if (type.equals(AssociateConstantStatus.APPROVED)) {
            String associateEmail = associate.getAssociateEmail();
            String associateName = associate.getAssociateName();

            _associateService.updateAssociate(
                    associateId, associateEmail, associateName,
                    AssociateConstantStatus.SUSPEND, associate.getAssociateType());
        }
        else {
            System.out.printf(
                "Associate don't have a valid plan situation to suspend. %s%n", type);
        }

        return associate.getAssociateStatus();
    }

    @Override
    public void updateAssociateType(
            long associateId, String oldType, String newType)
        throws AssociateNotFound {

        if (!AssociateConstantType.getAssociateConstantsTypeList().contains(newType)) {
            return;
        }

        Associate associate = _associateService.fetchAssociateById(associateId);

        if (associate == null) {
            throw new AssociateNotFound(
                "Unable to update associate plan. Associate not found with id %s".formatted(
                        associateId));
        }

        if (associate.getAssociateType().equals(oldType)) {
            associate.setAssociateType(newType);
        }

        String associateEmail = associate.getAssociateEmail();
        String associateName = associate.getAssociateName();

        _associateService.updateAssociate(
            associate.getAssociateId(), associateEmail, associateName,
            associate.getAssociateStatus(), associate.getAssociateType());
    }

    private final AssociateService _associateService;

    private final BenefitService _benefitService;

    private final NotifyPersistence _notifyPersistence;

}
