package ar.edu.itba.ss.managers;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorManager {

    private static final Injector injector = Guice.createInjector();

    private InjectorManager() {}

    public static Injector getInjector() {
        return injector;
    }

}