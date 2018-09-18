package ar.edu.itba.ss;

import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.managers.SimulationManager;
import ar.edu.itba.ss.utils.io.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final IOManager ioManager = InjectorManager.getInjector().getInstance(IOManager.class);
        final SimulationManager simulationManager = InjectorManager.getInjector().getInstance(SimulationManager.class);
        final OutputWriter outputWriter = InjectorManager.getInjector().getInstance(OutputWriter.class);

        int elapsed = 0;

        for (double height = 0D; height <= ioManager.getConfiguration().gettAltitude() * ioManager.getConfiguration().getdAltitude();
             height += ioManager.getConfiguration().getdAltitude()) {

            while (elapsed <= ioManager.getConfiguration().getDuration()) {
                elapsed += simulationManager.simulate(height);

                if (elapsed % ioManager.getConfiguration().getPrint() == 0) {
                    try {
                        outputWriter.write(String.valueOf(height));
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
    }
}