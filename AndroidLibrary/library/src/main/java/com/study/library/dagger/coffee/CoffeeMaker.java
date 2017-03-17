package com.study.library.dagger.coffee;

import javax.inject.Inject;

/**
 * Created by borney on 2/20/17.
 */

public class CoffeeMaker {
    @Inject
    Heater heater;
    @Inject Pump pump;

    @Inject public CoffeeMaker() {
    }

    public void brew() {
        heater.on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        heater.off();
    }
}
