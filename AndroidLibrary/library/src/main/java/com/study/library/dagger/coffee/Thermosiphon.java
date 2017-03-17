package com.study.library.dagger.coffee;

import javax.inject.Inject;

/**
 * Created by borney on 2/20/17.
 */

public class Thermosiphon implements Pump {
    private final Heater heater;

    @Inject
    public Thermosiphon(Heater heater) {
        this.heater = heater;
        System.out.println("Thermosiphon===> heater = " + heater);
    }

    @Override public void pump() {
        System.out.println("Thermosiphon===> pump heater = " + heater);
        if (heater.isHot()) {
            System.out.println("=> => pumping => =>");
        }
    }
}
