package com.study.library.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by borney on 2/14/17.
 */
public class Map {
    public static void main(String[] args) {
        /**
         * 操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果。
         * 如下，将原始Observable数据转化成大写，再发射：
         */
        Observable
                .just("Hello", "RxJava")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.toUpperCase();
                    }
                })
                .subscribe(new Subscriber<String>() {
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
