package com.study.library.java;

/**
 * Created by borney on 2/22/17.
 */

/**
 * this.show(o)
 * super.show(o)
 * this.show((super)o)
 * super.show((super)o)
 */
public class DuoTai {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();

        System.out.println("a1 = " + a1);
        System.out.println("a2 = " + a2);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("d = " + d);

        System.out.println("1--" + a1.show(b)); //A and A
        System.out.println("2--" + a1.show(c)); //A and A
        System.out.println("3--" + a1.show(d)); //A and D
        System.out.println("4--" + a2.show(b)); //B and A
        System.out.println("5--" + a2.show(c)); //B and A
        System.out.println("6--" + a2.show(d)); //A and D
        System.out.println("7--" + b.show(b));  //B and B
        System.out.println("8--" + b.show(c));  //B and B
        System.out.println("9--" + b.show(d));  //A and D
    }
}

class A {

    A() {
        System.out.println("A = " + this);
    }

    public String show(D obj) {
        return ("A and D");
    }

    public String show(A obj) {
        return ("A and A");
    }
}

class B extends A {

    B() {
        System.out.println("B = " + this);
    }

    public String show(B obj) {
        return ("B and B");
    }

    public String show(A obj) {
        return ("B and A");
    }
}

class C extends B {
    C() {
        System.out.println("C = " + this);
    }
}

class D extends B {
    D() {
        System.out.println("D = " + this);
    }
}
