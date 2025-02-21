package com.associate.notify.util;

import com.associate.associate.util.AssociateObserver;
import com.associate.notify.model.Notify;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Albert Gomes Cabral
 */
@Component
public class NotifySubject {

    public List<AssociateObserver> getObservers() {
        return _observers;
    }

    public void addObserver(AssociateObserver observer) {
        this._observers.add(observer);
    }

    public void removeObserver(AssociateObserver observer) {
        _observers.remove(observer);
    }

    public void notifyObservers(Notify notify) {
        for (AssociateObserver observer : _observers) {
            observer.updateNotify(notify);
        }
    }

    private final List<AssociateObserver> _observers = new ArrayList<>();

}
