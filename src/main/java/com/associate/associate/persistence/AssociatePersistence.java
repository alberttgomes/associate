package com.associate.associate.persistence;

import com.associate.associate.model.Associate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface AssociatePersistence extends CrudRepository<Associate, Long> {

    int countByAssociateIdAndCompanyId(long associateId, long companyId);

    void deleteByAssociateIdAndCompanyId(long associateId, long companyId);

    Associate findByAssociateIdAndCompanyId(long associateId, long companyId);

    Associate findByAssociateEmailOrAssociateName(String email, String name);

    List<Associate> findByCompanyId(long companyId);

    List<Associate> findByCompanyIdAndAssociateStatus(
            long companyId, String status);

}
