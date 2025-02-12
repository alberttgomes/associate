package com.partner.persistence;

import com.partner.model.Benefit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface BenefitPersistence extends CrudRepository<Benefit, Long> {

    Benefit findByBenefitName(String benefitName);

}
