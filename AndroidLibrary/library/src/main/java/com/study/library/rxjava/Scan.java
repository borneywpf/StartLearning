package com.study.library.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by borney on 2/14/17.
 */

public class Scan {
    public static void main(String[] args) {
        scan();
    }

    /**
     * 一个累加器函数，操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。
     */
    private static void scan() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        System.out.println("integer = " + integer.intValue() + " integer2 = " + integer2.intValue());
                        return integer + integer2;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer.intValue());
                    }
                });
    }
}
