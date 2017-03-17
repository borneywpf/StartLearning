package com.study.library.dagger.lazy;

import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by borney on 2/20/17.
 */

public class DirectCounter {
    @Inject
    Integer value;

    public void print() {
        System.out.println("printing...");
        System.out.println(value);
        System.out.println(value);
        System.out.println(value);
    }

    @Test public void test() {
        DirectCounter counter = new DirectCounter();
        counter.print();
    }
}
