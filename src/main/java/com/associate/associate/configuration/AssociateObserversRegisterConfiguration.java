package com.associate.associate.configuration;

import com.associate.associate.api.AssociateService;
import com.associate.associate.model.Associate;
import com.associate.associate.util.AssociateObserver;
import com.associate.notify.util.NotifySubject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albert Gomes Cabral
 */
@Configuration
public class AssociateObserversRegisterConfiguration {

    @Autowired
    public AssociateObserversRegisterConfiguration(
            AssociateService associateService, NotifySubject notifySubject) {

        this._associateService = associateService;
        this._notifySubject = notifySubject;
    }

    @Bean
    public List<AssociateObserver> registerAssociates() {
        for (Associate associate : _associateService.fetchAllAssociates()) {
            _notifySubject.addObserver(associate);
        }

        return _notifySubject.getObservers();
    }

    private final AssociateService _associateService;

    private final NotifySubject _notifySubject;

}
