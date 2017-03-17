package com.study.library.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by borney on 2/17/17.
 */

public class Filter {
    public static void main(String[] args) {
        filter();
        System.out.println("-----------------------");
        filterError();
    }

    private static void filterError() {
        Observable.just(new P(1, "111"))
                .filter(new Func1<P, Boolean>() {
                    @Override
                    public Boolean call(P p) {
                        if (p.getId() != 1) {
                            return true;
                        }
                        throw new RuntimeException("error");
                    }
                })
                .subscribe(new Subscriber<P>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onNext(P p) {
                        System.out.println(p);
                    }
                });
    }

    private static void filter() {
        Observable.just(new P(1, "111"), new P(2, "222"), new P(3, "333"), new P(4, "444"),
                new P(5, "555"), new P(6, "666"))
                .filter(new Func1<P, Boolean>() {
                    @Override
                    public Boolean call(P p) {
                        return p.id % 2 == 0;
                    }
                }).subscribe(new Subscriber<P>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error " + e);
            }

            @Override
            public void onNext(P p) {
                System.out.println(p);
            }
        });
    }

    private static class P {
        private int id;
        private String name;

        P(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "P{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
