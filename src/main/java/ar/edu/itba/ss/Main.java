package ar.edu.itba.ss;

import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.managers.SimulationManager;
import ar.edu.itba.ss.utils.io.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final IOManager ioManager = InjectorManager.getInjector().getInstance(IOManager.class);
        final SimulationManager simulationManager = InjectorManager.getInjector().getInstance(SimulationManager.class);
        final OutputWriter outputWriter = InjectorManager.getInjector().getInstance(OutputWriter.class);

        for (double height = 0D; height <= ioManager.getConfiguration().gettAltitude() * ioManager.getConfiguration().getdAltitude();
             height += ioManager.getConfiguration().getdAltitude()) {

            outputWriter.remove(String.valueOf(height));

            simulationManager.findShortestDistance(height);
        }
    }
}