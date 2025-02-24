package com.associate.associate.api;

import com.associate.address.model.Address;
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
            String email, long companyId, String name, String phoneNumber,
            String status, String type)
        throws RuntimeException;

    Associate createAssociateWithAddress(
            Address address, long companyId, String email, String name,
            String phoneNumber, String type)
        throws AssociateAttributeInvalid, CompanyNotFound;

    Associate createAssociateWorkflow(
            long companyId, String email, boolean needsWorkflowApprove, String name,
            String phoneNumber, String type);

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
            long associatedId, String email, String name, String status,
            String type)
        throws AssociateNotFound;

}
