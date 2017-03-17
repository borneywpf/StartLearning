package com.study.library.dagger.coffee;

import dagger.Component;

/**
 * Created by borney on 2/20/17.
 */

@Component(modules = DripCoffeeModule.class)
interface CoffeeShop {
    CoffeeMaker maker();
}
