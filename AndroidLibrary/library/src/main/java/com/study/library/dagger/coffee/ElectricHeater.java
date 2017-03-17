package com.study.library.dagger.coffee;

/**
 * Created by borney on 2/20/17.
 */
public class ElectricHeater implements Heater {
    boolean heating;

    @Override
    public void on() {
        this.heating = true;
        System.out.println("ElectricHeater=====>on = " + heating + " this = " + this);
    }

    @Override
    public void off() {
        this.heating = false;
        System.out.println("ElectricHeater=====>off = " + heating + " this = " + this);
    }

    @Override
    public boolean isHot() {
        System.out.println("ElectricHeater=====>isHot = " + this.heating + " this = " + this);
        return this.heating;
    }
}
