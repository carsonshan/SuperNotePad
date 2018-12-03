package com.fairhand.supernotepad.app;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 基于RxJava Diy事件分发RxBus<br/>
 * 主线程中观察observe，子线程中订阅subscribe<br/>
 * 雏形：11/27/2018 - Tuesday - 3:24 PM
 *
 * @author Phanton
 * @date 11/27/2018 - Tuesday - 3:24 PM
 */
public class RxBus {
    
    private volatile static RxBus instance;
    private final Subject<Object> mBus;
    
    private RxBus() {
        mBus = PublishSubject.create();
    }
    
    /**
     * DCL返回单例
     */
    public static RxBus getInstance() {
        if (instance == null) {
            synchronized ((RxBus.class)) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }
    
    /**
     * 发送消息<br/>
     * 以onNext()方式发送，在doOnNext()方法中接收并处理
     *
     * @param o 消息内容
     */
    public void post(Object o) {
        mBus.onNext(o);
    }
    
    /**
     * 接收消息处理<br/>
     * ex:
     * <p>
     * RxBus.getInstance().toObservable(String.class)
     * .observeOn(AndroidSchedulers.mainThread())
     * .doOnNext(textView::setText)
     * .subscribe();
     * </p>
     * 当onNext()发生时，doOnNext()被调用<br/>
     * tip:
     * <br/>
     * doOnNext()方法会在subscribe的onNext()之前调用，亦在输出值前做额外的事
     * <br/>
     * ofType(class)过滤操作，即指定某个类型的class<br/>
     * 只接收指定类型的值，其他类型会被抛弃忽略
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
    
    /**
     * 取消注册
     */
    public void unregister() {
        mBus.onComplete();
    }
    
}
