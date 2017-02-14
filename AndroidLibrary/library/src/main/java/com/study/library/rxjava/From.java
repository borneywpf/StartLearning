package com.study.library.rxjava;

import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by borney on 2/14/17.
 */
public class From {
    public static void main(String[] args) {
        from();
    }

    private static void from() {
        Observable.from(Arrays.asList("Hello", "RxJava")).subscribe(new Subscriber<String>() {
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
