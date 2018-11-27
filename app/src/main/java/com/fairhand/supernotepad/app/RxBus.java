package com.fairhand.supernotepad.app;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Phanton
 * @date 11/25/2018 - Sunday - 10:16 PM
 */
public class RxBus {
    
    private static RxBus instance;
    
    private final Subject<Object> mBus;
    
    public static synchronized RxBus getInstance() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }
    
    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishSubject.create().toSerialized();
    }
    
    public static RxBus get() {
        return Holder.BUS;
    }
    
    public void post(Object obj) {
        mBus.onNext(obj);
    }
    
    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }
    
    public Observable<Object> toObservable() {
        return mBus;
    }
    
    public boolean hasObservers() {
        return mBus.hasObservers();
    }
    
    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }
    
}
