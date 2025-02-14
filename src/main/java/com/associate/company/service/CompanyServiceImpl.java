package com.associate.company.service;

import com.associate.associate.api.AssociateActionService;
import com.associate.company.api.CompanyService;
import com.associate.associate.api.exception.AssociateNotFound;
import com.associate.company.api.exception.CompanyNotFound;
import com.associate.notify.exception.NotifyNotFound;
import com.associate.address.model.Address;
import com.associate.company.model.Company;
import com.associate.notify.model.Notify;
import com.associate.address.persistence.AddressPersistence;
import com.associate.company.persistence.CompanyPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    public CompanyServiceImpl(
            AddressPersistence addressPersistence, AssociateActionService associateActionService,
            CompanyPersistence companyPersistence) {

        this._addressPersistence = addressPersistence;
        this._associateActionService = associateActionService;
        this._companyPersistence = companyPersistence;
    }

    @Override
    public Address addCompanyAddress(
            String city, String country, long companyId, String number,
            String streetName, String zipCode)
        throws CompanyNotFound {

        if (_companyPersistence.findById(companyId).isPresent()) {
            throw new CompanyNotFound(
                "No company found with id %s".formatted(companyId));
        }

        Address address = new Address();

        address.setCity(city);
        address.setCountry(country);
        address.setCompanyId(companyId);
        address.setNumber(number);
        address.setStreetName(streetName);
        address.setZipCode(zipCode);

        return _addressPersistence.save(address);
    }

    @Transactional
    @Override
    public Company addCompanyWithAddress(
            String companyEmail, String companyName, String companyPhone,
            String city, String country, String number, String streetName,
            String zipCode)
        throws Exception {

        Company company = addCompany(
            0, companyEmail, companyName, companyPhone);

        if (company == null) {
            return null;
        }

        Address address = addCompanyAddress(
                city, country, company.getCompanyId(), number,
                streetName, zipCode);

        if (address == null) {
            return company;
        }

        company.setCompanyAddressId(address.getAddressId());

        return _companyPersistence.save(company);
    }

    @Override
    public Company addCompany(
            long companyAddressId, String companyEmail, String companyName,
            String companyPhone)
        throws Exception {

        if (_companyPersistence.findByCompanyEmailOrCompanyName(
                companyEmail, companyName) != null) {

            throw new Exception(
                "Company with the name and email already exists.");
        }

        Company company = new Company();

        company.setCompanyAddressId(companyAddressId);
        company.setCompanyEmail(companyEmail);
        company.setCompanyName(companyName);
        company.setCompanyPhone(companyPhone);

        return _companyPersistence.save(company);
    }

    @Override
    public void deleteCompanyById(long companyId) throws Exception {
        if (_companyPersistence.findById(companyId).isPresent()) {
            _companyPersistence.deleteById(companyId);
        }
        else throw new Exception(
            "No company found with id %s".formatted(companyId));
    }

    @Override
    public List<Company> fetchAllCompanies() throws Exception {
        Iterable<Company> iterable = _companyPersistence.findAll();

        List<Company> companies = new ArrayList<>();

        iterable.forEach(companies::add);

        return companies;
    }

    @Override
    public Company fetchCompanyByEmailOrName(String email, String name)
        throws Exception {

        if (email.matches(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            return _companyPersistence.findByCompanyEmailOrCompanyName(
                        email, name);
        }
        else throw new Exception(
            "Invalid email or name format %s".formatted(email + " " + name));
    }

    @Override
    public Company getCompanyById(long companyId) {
        Optional<Company> company = _companyPersistence.findById(companyId);

        return company.orElse(null);
    }

    @Override
    public Notify notifyAssociate(
            long associateId, long companyId, String notifyBody, String notifyHeader,
            String notifySent, String notifyTitle)
        throws NotifyNotFound {

        try {
            Notify notify = _associateActionService.notifyAssociate(
                associateId, companyId, notifyBody, notifyHeader,
                notifyTitle, companyId);

            if (notify == null) {
                throw new NotifyNotFound(
                    ("Unable to send notify to associate." +
                        " Cannot be found with id %s").formatted(associateId));
            }

            return notify;
        }
        catch (AssociateNotFound | CompanyNotFound | NotifyNotFound
                runtimeException) {
            if (runtimeException instanceof AssociateNotFound) {
                throw new AssociateNotFound(runtimeException);
            }
            else if (runtimeException instanceof CompanyService) {
                throw new CompanyNotFound((runtimeException).getMessage());
            }
            else {
                throw new NotifyNotFound(runtimeException);
            }
        }

    }

    @Override
    public Company updateCompany(
            long companyId, long companyAddressId, String companyEmail,
            String companyName, String companyPhone)
        throws Exception {

        Optional<Company> companyOld = _companyPersistence.findById(companyId);

        if (companyOld.isPresent()) {
            Company companyNew = companyOld.get();

            companyNew.setCompanyAddressId(companyAddressId);
            companyNew.setCompanyEmail(companyEmail);
            companyNew.setCompanyName(companyName);
            companyNew.setCompanyPhone(companyPhone);

            return _companyPersistence.save(companyNew);
        }
        else throw new Exception(
            "No company found with id %s".formatted(companyId));
    }

    private final AddressPersistence _addressPersistence;

    private final AssociateActionService _associateActionService;

    private final CompanyPersistence _companyPersistence;

}
