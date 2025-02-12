package com.partner.service;

import com.partner.api.AssociateService;
import com.partner.api.exception.AssociateAttributeInvalid;
import com.partner.api.exception.AssociateNotFound;
import com.partner.api.exception.CompanyNotFound;
import com.partner.constants.AssociateConstantStatus;
import com.partner.constants.AssociateConstantType;
import com.partner.model.Company;
import com.partner.persistence.AssociatePersistence;
import com.partner.model.Associate;
import com.partner.util.CompanyThreadLocal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class AssociateServiceImpl implements AssociateService {

    @Autowired
    public AssociateServiceImpl(
        AssociatePersistence associatePersistence) {

        this._associatePersistence = associatePersistence;
    }

    @Override
    public Associate addAssociate(
            long companyId, String name, String status, String type)
        throws AssociateAttributeInvalid, CompanyNotFound {

        if (!_hasCompany())
            throw new CompanyNotFound(
                    "No company found with id %s".formatted(companyId));

        _validate(name, status, type);

        Associate associate = new Associate();

        associate.setAssociateName(name);
        associate.setAssociateStatus(status);
        associate.setAssociateType(type);
        associate.setCreateDate(new Date());
        associate.setCompanyId(companyId);

        return associate;
    }

    @Override
    public void deleteAssociate(long associateId) throws AssociateNotFound {
        if (_associatePersistence.findById(associateId).isPresent()) {
            _associatePersistence.deleteById(associateId);
        }
        else throw new AssociateNotFound(
            "No associate found with id %s".formatted(associateId));
    }

    @Override
    public void deleteAssociateByCompanyId(long companyId, long associateId)
        throws AssociateNotFound, CompanyNotFound {

        if (_associatePersistence.countByAssociateIdAndCompanyId(
                associateId, companyId) <= 0) {

            throw new AssociateNotFound(
                "No associate found with associateId %s".formatted(
                    associateId) + " and companyId %s".formatted(companyId));
        }

        _associatePersistence.deleteByAssociateIdAndCompanyId(
                associateId, companyId);
    }

    @Override
    public Associate fetchAssociateById(long associateId)
        throws AssociateNotFound {

        try {
            Optional<Associate> associate =
                    _associatePersistence.findById(associateId);

            if (associate.isEmpty()) {
                throw new AssociateNotFound(
                    "No associate found with id %s".formatted(associateId));
            }

            return associate.get();
        }
        catch (AssociateNotFound associateNotFound) {
            throw new AssociateNotFound(associateNotFound);
        }

    }

    @Override
    public Associate fetchAssociateByCompanyId(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound {

        if (!_hasCompany()) {
            throw new CompanyNotFound(
                "Unable to find associate with companyId %s".formatted(companyId));
        }

        return _associatePersistence.findByAssociateIdAndCompanyId(
                associateId, companyId);
    }

    @Override
    public Associate fetchAssociateByName(String name) throws AssociateNotFound {
        return _associatePersistence.findByAssociateName(name);
    }

    @Override
    public List<Associate> fetchAllAssociates() {
        Iterable<Associate> iterable = _associatePersistence.findAll();

        List<Associate> associates = new ArrayList<>();

        iterable.forEach(associates::add);

        return associates;
    }

    @Override
    public List<Associate> fetchAllAssociatesByCompanyId(long companyId)
        throws CompanyNotFound {

        if (_hasCompany())
            return new ArrayList<>();

        return _associatePersistence.findByCompanyId(companyId);
    }

    @Override
    public List<Associate> getAssociatesByStatus(
            long companyId, String status) throws CompanyNotFound {

        List<Associate> associateList =
                _associatePersistence.findByCompanyIdAndAssociateStatus(
                        companyId, status);

        if (associateList.isEmpty()) {
            return new ArrayList<>();
        }

        return associateList;
    }

    @Override
    public boolean hasAssociateById(long associateId, long companyId) {
        return _associatePersistence.countByAssociateIdAndCompanyId(
                associateId, companyId) > 0;
    }

    @Override
    public Associate updateAssociate(
            long associatedId, String name, String status, String type)
        throws AssociateNotFound {

        try {
            Optional<Associate> associateOld =
                    _associatePersistence.findById(associatedId);

            if (associateOld.isEmpty()) {
                throw new AssociateNotFound(
                    "No associate found with id %s".formatted(associatedId));
            }

            Associate associateNew = associateOld.get();

            _validate(name, status, type);

            associateNew.setAssociateName(name);
            associateNew.setAssociateStatus(status);
            associateNew.setAssociateType(type);

            return _associatePersistence.save(associateNew);
        }
        catch (AssociateNotFound associateNotFound) {
            throw new AssociateNotFound(associateNotFound);
        }

    }

    private boolean _hasCompany() {
        return CompanyThreadLocal.getCompanyThreadLocal().getCompanyId() > 0;
    }

    private void _validate(String name, String status, String type)
        throws AssociateAttributeInvalid {

        try {
            List<String> statusList =
                    AssociateConstantStatus.getAvailableStatusList();

            List<String> typesList =
                    AssociateConstantType.getAssociateConstantsTypeList();

            if (!name.matches("[A-Za-z]")) {
                throw new AssociateAttributeInvalid(
                    "Invalid attribute name %s".formatted(name));
            }
            else if (!statusList.contains(status)) {
                throw new AssociateAttributeInvalid(
                    "Invalid attribute status %s".formatted(status));
            }
            else if (!typesList.contains(type)) {
                throw new AssociateAttributeInvalid(
                    "Invalid attribute type %s".formatted(type));
            }
        }
        catch (AssociateAttributeInvalid associateAttributeInvalid) {
            throw new AssociateAttributeInvalid(
                    associateAttributeInvalid);
        }
    }

    private final AssociatePersistence _associatePersistence;

}
