package com.study.library.rxjava;

import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by borney on 2/14/17.
 */

public class FlatMap {
    public static void main(String[] args) {
        flatmap();
    }

    /**
     * flatMap()接收一个Observable的输出作为输入，然后作为一个新的Observable再发射。理解flatMap的关键点在于，
     * flatMap输出的新的Observable正是我们在Subscriber想要接收的，比如这里输出单个的字符串。
     */
    private static void flatmap() {
        Observable.from(Arrays.asList("Hello", "RxJava"))
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s.toUpperCase());
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
                    public void onNext(String o) {
                        System.out.println(o);
                    }
                });
    }
}
