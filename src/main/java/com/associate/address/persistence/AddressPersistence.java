package com.associate.address.persistence;

import com.associate.address.model.Address;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
@Repository
public interface AddressPersistence extends CrudRepository<Address, Long> {

    Address findByAddressIdAndCompanyId(long addressId, long companyId);

    List<Address> findByCompanyId(long companyId);

}
