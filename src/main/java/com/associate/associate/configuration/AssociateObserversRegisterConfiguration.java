package com.associate.associate.configuration;

import com.associate.associate.api.AssociateService;
import com.associate.associate.model.Associate;
import com.associate.associate.util.AssociateObserver;
import com.associate.company.model.Company;
import com.associate.notify.util.NotifySubject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albert Gomes Cabral
 */
@Configuration
public class AssociateObserversRegisterConfiguration {

    @Autowired
    public AssociateObserversRegisterConfiguration(
            AssociateService associateService,
            @Qualifier("company") Company company, NotifySubject notifySubject) {

        this._associateService = associateService;
        this._company = company;
        this._notifySubject = notifySubject;
    }

    @Bean
    public List<AssociateObserver> registerAssociates() {
        long companyId = 0;

        if (_company != null) {
            companyId = _company.getCompanyId();
        }

        for (Associate associate :
                _associateService.fetchAllAssociatesByCompanyId(
                        companyId)) {

            _notifySubject.addObserver(associate);
        }

        return _notifySubject.getObservers();
    }

    private final AssociateService _associateService;

    private final Company _company;

    private final NotifySubject _notifySubject;

}
