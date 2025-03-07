package com.associate.notify.persistence;

import com.associate.notify.model.Notify;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface NotifyPersistence extends CrudRepository<Notify, Long> {

    List<Notify> findByCompanyIdAndReceiver(long companyId, long receiver);

}
