package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Particle;
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

        for (double height = Particle.EARTH_RADIUS;
             height <  Particle.EARTH_RADIUS + ioManager.getConfiguration().gettAltitude();
             height += ioManager.getConfiguration().getdAltitude()) {

            logger.info("Running simulation for height: " + height);

            simulationManager.setSpaceShip(height, ioManager.getConfiguration().getVelocity());
            outputWriter.remove(String.valueOf(height));

            simulationManager.findShortestDistance(height);
            simulationManager.reset();
        }
    }
}