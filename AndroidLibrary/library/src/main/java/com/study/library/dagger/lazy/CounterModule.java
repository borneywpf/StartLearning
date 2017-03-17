package com.study.library.dagger.lazy;

import dagger.Module;
import dagger.Provides;

/**
 * Created by borney on 2/20/17.
 */

@Module
public class CounterModule {
    int next = 100;

    @Provides Integer provideInteger() {
        System.out.println("computing...");
        return next++;
    }
}
