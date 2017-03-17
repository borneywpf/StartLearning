package com.study.library.dagger.coffee;

import dagger.Module;
import dagger.Provides;

/**
 * Created by borney on 2/20/17.
 */

@Module
public class DripCoffeeModule {
    @Provides
    static Heater provideHeater() {
        return new ElectricHeater();
    }

    @Provides static Pump providePump(Thermosiphon pump) {
        return pump;
    }
}
