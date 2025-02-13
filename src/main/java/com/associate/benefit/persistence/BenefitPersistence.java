package com.associate.benefit.persistence;

import com.associate.benefit.model.Benefit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface BenefitPersistence extends CrudRepository<Benefit, Long> {

    Benefit findByBenefitName(String benefitName);

    List<Benefit> findAllByCompanyId(long companyId);

}
