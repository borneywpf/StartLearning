package com.study.library.rxjava;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by borney on 2/15/17.
 */

public class GroupBy {
    public static void main(String[] args) {
        groupby();
    }

    private static void groupby() {
        Observable.just(1, 2, 3, 4, 5).groupBy(new Func1<Integer, Object>() {
            @Override
            public Object call(Integer integer) {
                return null;
            }
        });
    }
}
