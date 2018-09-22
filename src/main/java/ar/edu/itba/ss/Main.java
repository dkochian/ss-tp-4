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

        for (double velocity = ioManager.getConfiguration().getInitialVelocity();
             velocity < ioManager.getConfiguration().getFinalVelocity();
             velocity += ioManager.getConfiguration().getdVelocity()) {

            for (double height = Particle.EARTH_RADIUS;
                 height < Particle.EARTH_RADIUS + ioManager.getConfiguration().gettAltitude();
                 height += ioManager.getConfiguration().getdAltitude()) {

                double orbitalHeight = (height - Particle.EARTH_RADIUS) / 1000;

                logger.info("Running simulation for orbital height: " + orbitalHeight + " km, and velocity: " + velocity/1000 + " km/s.");

                simulationManager.setSpaceShip(height, velocity);
                outputWriter.remove(String.valueOf(orbitalHeight));

                simulationManager.findShortestDistance(height, velocity);
                simulationManager.reset();
            }
        }
    }
}