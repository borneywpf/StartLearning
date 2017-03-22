package com.study.java;

import java.io.UnsupportedEncodingException;

/**
 * Created by borney on 3/22/17.
 */

public class CharSet {
    public static void main(String[] args) {
        String str = "ÀÏÂí";
        try {
            recover(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        printChar();
    }

    public static void recover(String str)
            throws UnsupportedEncodingException {
        String[] charsets = new String[]{"windows-1252", "GB18030", "Big5", "UTF-8"};
        for (int i = 0; i < charsets.length; i++) {
            for (int j = 0; j < charsets.length; j++) {
                if (i != j) {
                    String s = new String(str.getBytes(charsets[i]), charsets[j]);
                    System.out.println(
                            "---- 原来编码(A)假设是: " + charsets[j] + ", 被错误解读为了(B): " + charsets[i]);
                    System.out.println(s);
                    System.out.println();
                }
            }
        }
    }

    private static void printChar() {
        char a = '马';
        int b = a;
        System.out.println(Integer.toBinaryString(a));
        System.out.println("b = " + b + ":" + Integer.toBinaryString(a));
    }
}
