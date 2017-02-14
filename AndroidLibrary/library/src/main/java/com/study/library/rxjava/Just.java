package com.study.library.rxjava;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by borney on 2/14/17.
 */
public class Just {
    public static void main(String[] args) {
        /**
         * just函数，它接受一至九个参数，返回一个按参数列表顺序发射这些数据的Observable。
         */
        Observable.just("Hello", "RxJava", "I", "will", "Study").subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
    }
}
