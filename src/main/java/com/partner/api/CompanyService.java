package com.partner.api;

import com.partner.api.exception.AssociateNotFound;
import com.partner.api.exception.CompanyNotFound;
import com.partner.api.exception.NotifyNotFound;
import com.partner.model.Address;
import com.partner.model.Benefit;
import com.partner.model.Company;
import com.partner.model.Notify;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public interface CompanyService {

    Address addCompanyAddress(
            String city, String country, long companyId, String number,
            String streetName, String zipCode)
        throws CompanyNotFound;

    Company addCompanyWithAddress(
            String companyEmail, String companyName, String companyPhone,
            String city, String country, String number, String streetName,
            String zipCode)
        throws Exception;

    Company addCompany(
            long companyAddressId, String companyEmail, String companyName,
            String companyPhone)
        throws Exception;

    void deleteCompanyById(long companyId) throws Exception;

    List<Company> fetchAllCompanies() throws Exception;

    Company fetchCompanyByEmailOrName(
            String email, String name) throws Exception;

    Company getCompanyById(long companyId) throws RuntimeException;

    Notify notifyAssociate(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifySent, String notifyTitle)
        throws NotifyNotFound;

    Company updateCompany(
            long companyId, long companyAddressId, String companyEmail,
            String companyName, String companyPhone)
        throws Exception;

}
