package com.study.library.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by borney on 2/17/17.
 */

public class Concat {
    public static void main(String[] args) {
        concat();
    }

    private static void concat() {
        Observable<Integer> just = Observable.just(1, 2, 3, 4);
        Observable<Integer> just1 = Observable.just(5, 6, 7, 8)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return false;
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }
                });

        Observable.concat(just, just1).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });
    }
}
