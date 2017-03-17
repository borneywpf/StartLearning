package com.study.library.guava;

import com.google.common.base.Optional;
import com.study.library.Print;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by borney on 3/3/17.
 */

public class OptionalTest {
    public static void main(String[] args) {
        testMethodReturn();
    }

    private static void testMethodReturn() {
        Optional<Long> optional = method_r_null();
        if (optional.isPresent()) {
            Print.println("值:" + optional.get());
        } else {
            Print.println("无值:" + optional.or(12L));
        }

        System.out.println("获得返回值 orNull: " + optional.orNull());

        Optional<Long> optional1 = method_not_r_null();

        if (optional1.isPresent()) {
            Print.println("值:" + optional1.get());
        } else {
            Print.println("无值:" + optional1.or(12L));
        }

        System.out.println("获得返回值 orNull: " + optional1.orNull());
    }

    private static Optional<Long> method_r_null() {
        return Optional.fromNullable(null);
    }

    private static Optional<Long> method_not_r_null() {
        return Optional.fromNullable(15L);
    }

    @Test(expected = NullPointerException.class)
    public void test_of_null() {
        Optional.of(null);
    }

    @Test
    public void test_of_not_null() {
        Optional<Object> of = Optional.of(new Object());
        Assert.assertNotNull(of.get());
    }

    @Test
    public void test_fromNullable_Present_false() {
        Optional<Object> optional = Optional.fromNullable(null);
        Assert.assertFalse(optional.isPresent());
    }

    @Test
    public void test_fromNullable_Present_true() {
        Optional<Object> optional = Optional.fromNullable(new Object());
        Assert.assertTrue(optional.isPresent());
    }
}
