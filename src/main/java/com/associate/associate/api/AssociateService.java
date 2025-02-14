package com.associate.associate.api;

import com.associate.associate.api.exception.AssociateAttributeInvalid;
import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.associate.model.Associate;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface AssociateService {

    Associate addAssociate(
            long companyId, String name, String status, String type)
        throws AssociateAttributeInvalid, CompanyNotFound;

    void deleteAssociate(long associateId) throws AssociateNotFound;

    void deleteAssociateByCompanyId(long companyId, long associateId)
        throws AssociateNotFound, CompanyNotFound;

    Associate fetchAssociateById(
            long associateId) throws AssociateNotFound;

    Associate fetchAssociateByCompanyId(long associateId, long companyId)
        throws AssociateNotFound, CompanyNotFound;

    Associate fetchAssociateByName(
            String name) throws AssociateNotFound;

    List<Associate> fetchAllAssociates();

    List<Associate> fetchAllAssociatesByCompanyId
            (long companyId) throws CompanyNotFound;

    List<Associate> getAssociatesByStatus(
            long companyId, String status) throws CompanyNotFound;

    boolean hasAssociateById(long associateId, long companyId);

    Associate updateAssociate(
            long associatedId, String name, String status, String type)
        throws AssociateNotFound;

}
