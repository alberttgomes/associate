package com.associate.benefit.persistence;

import com.associate.benefit.model.BenefitResource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface BenefitResourcesPersistence extends CrudRepository<BenefitResource, Long> {

    List<BenefitResource> findByBenefitId(long benefitId);

    BenefitResource findByMetaDataIsLike(String metaDataLike);

}
