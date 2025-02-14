package com.associate.associate.service;

import com.associate.address.model.Address;
import com.associate.address.persistence.AddressPersistence;
import com.associate.associate.api.AssociateService;
import com.associate.associate.api.exception.AssociateAttributeInvalid;
import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.constants.AssociateConstantType;
import com.associate.associate.persistence.AssociatePersistence;
import com.associate.associate.model.Associate;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.company.util.CompanyDynamicQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class AssociateServiceImpl implements AssociateService {

    @Autowired
    public AssociateServiceImpl(
            AddressPersistence addressPersistence, AssociatePersistence associatePersistence,
            CompanyDynamicQuery companyDynamicQuery) {

        this._addressPersistence = addressPersistence;
        this._associatePersistence = associatePersistence;
        this._companyDynamicQuery = companyDynamicQuery;
    }

    @Override
    public Associate addAssociate(
            String email, long companyId, String name, String status, String type)
        throws RuntimeException {

        if (!_companyDynamicQuery.hasCompany(companyId))
            throw new CompanyNotFound(
                "No company found with id %s".formatted(companyId));

        _validate(email, name, status, type);

        if (_associatePersistence.findByAssociateEmailOrAssociateName(
                email, name) != null)
            throw new RuntimeException(
                "Duplicate entry to " + email + " or " + name);

        Associate associate = new Associate();

        associate.setAssociateEmail(email);
        associate.setAssociateName(name);
        associate.setAssociateStatus(status);
        associate.setAssociateType(type);
        associate.setCreateDate(new Date());
        associate.setCompanyId(companyId);

        return _associatePersistence.save(associate);
    }

    @Transactional
    @Override
    public Associate createAssociateWithAddress(
            Address address, long companyId, String email, String name, String type)
        throws AssociateAttributeInvalid {

        if (_companyDynamicQuery.hasCompany(companyId)) {
            Associate associate = addAssociate(
                    email, companyId, name, AssociateConstantStatus.APPROVED, type);

            if (associate == null || address == null) return null;

            _addressPersistence.save(address);

            return associate;
        }

        return null;
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

        if (!_companyDynamicQuery.hasCompany(companyId)) {
            throw new CompanyNotFound(
                "Unable to find associate with companyId %s".formatted(companyId));
        }

        return _associatePersistence.findByAssociateIdAndCompanyId(
                associateId, companyId);
    }

    @Override
    public Associate fetchAssociateByName(String name) throws AssociateNotFound {
        return _associatePersistence.findByAssociateEmailOrAssociateName(
                name, name);
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

        if (!_companyDynamicQuery.hasCompany(companyId))
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
            long associatedId, String email, String name, String status,
            String type)
        throws AssociateNotFound {

        try {
            Optional<Associate> associateOld =
                    _associatePersistence.findById(associatedId);

            if (associateOld.isEmpty()) {
                throw new AssociateNotFound(
                    "No associate found with id %s".formatted(associatedId));
            }

            Associate associateNew = associateOld.get();

            _validate(email, name, status, type);

            associateNew.setAssociateEmail(email);
            associateNew.setAssociateId(associatedId);
            associateNew.setAssociateName(name);
            associateNew.setAssociateStatus(status);
            associateNew.setAssociateType(type);

            return _associatePersistence.save(associateNew);
        }
        catch (AssociateNotFound associateNotFound) {
            throw new AssociateNotFound(associateNotFound);
        }

    }

    private void _validate(String email, String name, String status, String type)
        throws AssociateAttributeInvalid {

        try {
            List<String> statusList =
                    AssociateConstantStatus.getAvailableStatusList();

            List<String> typesList =
                    AssociateConstantType.getAssociateConstantsTypeList();

            if (!email.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new AssociateAttributeInvalid(
                    "Invalid attribute email %s".formatted(email));
            }
            else if (!name.matches("^[A-Za-z]+(?: [A-Za-z]+)*$")) {
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

    private final AddressPersistence _addressPersistence;

    private final AssociatePersistence _associatePersistence;

    private final CompanyDynamicQuery _companyDynamicQuery;

}
