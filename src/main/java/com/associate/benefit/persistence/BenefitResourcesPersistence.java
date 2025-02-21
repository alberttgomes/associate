package com.associate.benefit.persistence;

import com.associate.benefit.model.BenefitResources;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface BenefitResourcesPersistence extends CrudRepository<BenefitResources, Long> {

    List<BenefitResources> findByBenefitId(long benefitId);

    BenefitResources findByMetaDataIsLike(String metaDataLike);

}
