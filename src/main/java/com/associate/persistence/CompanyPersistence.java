package com.associate.persistence;

import com.associate.model.Company;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface CompanyPersistence extends CrudRepository<Company, Long> {

    Company findByCompanyEmailOrCompanyName(
            String companyEmail, String companyName);

}
