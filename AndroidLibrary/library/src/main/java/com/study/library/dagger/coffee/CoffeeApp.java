package com.study.library.dagger.coffee;

/**
 * Created by borney on 2/20/17.
 */

public class CoffeeApp {
    public static void main(String[] args) {
        CoffeeShop coffeeShop = DaggerCoffeeShop.builder().dripCoffeeModule(new DripCoffeeModule()).build();
        coffeeShop.maker().brew();
    }
}
