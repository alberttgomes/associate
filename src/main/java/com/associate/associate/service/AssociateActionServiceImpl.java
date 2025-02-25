package com.associate.associate.service;

import com.associate.associate.api.AssociateActionService;
import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.constants.AssociateConstantCategory;
import com.associate.associate.model.Associate;
import com.associate.associate.api.AssociateService;
import com.associate.benefit.api.BenefitService;
import com.associate.benefit.api.exception.BenefitNotFound;
import com.associate.benefit.model.Benefit;
import com.associate.benefit.model.BenefitResource;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.notify.model.Notify;
import com.associate.notify.persistence.NotifyPersistence;
import com.associate.notify.util.NotifySubject;

import java.util.ArrayList;
import java.util.Date;
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
            NotifyPersistence notifyPersistence, NotifySubject notifySubject) {

        this._associateService = associateService;
        this._benefitService = benefitService;
        this._notifyPersistence = notifyPersistence;
        this._notifySubject = notifySubject;
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
                associate.getAssociateCategory(), companyId);
    }

    @Override
    public BenefitResource fetchBenefitResource(long benefitId) {
        return _benefitService.fetchBenefitResourcesLatestVersion(benefitId);
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
    public List<Notify> findNotifiesByReceiverId(long companyId, long receiverId)
        throws AssociateNotFound, CompanyNotFound {

        if (!_associateService.hasAssociateById(receiverId, companyId)) {
            return new ArrayList<>();
        }

        return _notifyPersistence.findByCompanyIdAndReceiver(
                companyId, receiverId);
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
    public Associate reactivePlanAssociate(
        long associateId, String status, String category) throws AssociateNotFound {

        Associate associate = _associateService.fetchAssociateById(associateId);

        if (associate == null) {
            throw new AssociateNotFound(
                "Unable to reactive plan, associate with id was not found %s"
                        .formatted(associateId));
        }

        if (status.equals(AssociateConstantStatus.APPROVED) &&
                AssociateConstantCategory.getAssociateConstantsTypeList().contains(category)) {

            String email = associate.getAssociateEmail();
            String phoneNumber = associate.getAssociatePhoneNumber();
            String name = associate.getAssociateName();

            return _associateService.updateAssociate(
                    category, associateId, email, phoneNumber, name, status);
        }

        return null;
    }

    @Override
    public Notify sendNotify(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifyTitle, long receiver)
        throws AssociateNotFound {

        Associate associateSent = _associateService.fetchAssociateByCompanyId(
                associateId, companyId);

        Notify notify = new Notify();

        notify.setCompanyId(companyId);
        notify.setCreateDate(new Date());
        notify.setNotifyBody(notifyBody);
        notify.setNotifyHeader(notifyHeader);
        notify.setNotifySent(associateSent.getAssociateName());
        notify.setNotifyTitle(notifyTitle);
        notify.setReceiver(receiver);

        notify = _notifyPersistence.save(notify);

        _notifySubject.notifyObservers(notify);

        return notify;
    }

    @Override
    public void shutdown(
        long associateId, long companyId) throws AssociateNotFound {

        try {
            Associate associate = _associateService.fetchAssociateByCompanyId(
                    associateId, companyId);

            String notifyBody = "request to leave of membership " +
                    associate.getAssociateName();

            sendNotify(
                associateId, companyId, notifyBody,
                "request to leave",
                "request to leave", companyId);

            _associateService.deleteAssociate(associateId);

            _notifySubject.removeObserver(associate);
        }
        catch (AssociateNotFound associateNotFound) {
            throw new AssociateNotFound(associateNotFound);
        }
    }

    @Override
    public String suspendPlanAssociate(
            long associateId, String reason) throws AssociateNotFound {

        Associate associate = _associateService.fetchAssociateById(associateId);

        String type = associate.getAssociateCategory();

        if (type.equals(AssociateConstantStatus.APPROVED)) {
            String email = associate.getAssociateEmail();
            String phoneNumber = associate.getAssociatePhoneNumber();
            String name = associate.getAssociateName();

            associate = _associateService.updateAssociate(
                    associate.getAssociateCategory(), associateId, email,
                    phoneNumber, name, AssociateConstantStatus.SUSPEND);
        }
        else {
            System.out.printf(
                "Associate don't have a valid plan situation to suspend. %s%n", type);
        }

        return associate.getAssociateStatus();
    }

    @Override
    public void updateAssociateCategory(
            long associateId, String oldType, String newType)
        throws AssociateNotFound {

        if (!AssociateConstantCategory.getAssociateConstantsTypeList().contains(newType)) {
            return;
        }

        Associate associate = _associateService.fetchAssociateById(associateId);

        if (associate.getAssociateCategory().equals(oldType)) {
            associate.setAssociateCategory(newType);
        }

        String email = associate.getAssociateEmail();
        String phoneNumber = associate.getAssociatePhoneNumber();
        String name = associate.getAssociateName();

        _associateService.updateAssociate(
            associate.getAssociateCategory(), associateId, email,
            phoneNumber, name, associate.getAssociateStatus());
    }

    private final AssociateService _associateService;

    private final BenefitService _benefitService;

    private final NotifyPersistence _notifyPersistence;

    private final NotifySubject _notifySubject;

}
