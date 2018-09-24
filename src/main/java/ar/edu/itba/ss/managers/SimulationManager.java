package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.utils.io.OutputWriter;
import ar.edu.itba.ss.utils.other.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class SimulationManager {
    private static final Logger logger = LoggerFactory.getLogger(SimulationManager.class);

    private static final double G = 6.693E-11;

    public static final double DAY = 24 * 3600.0;
    public static final double WEEK = DAY * 7;
    public static final double MONTH = DAY * 31;
    public static final double YEAR = DAY * 365;

    private String actualFileName;


    private final Schema schema;
    private final OutputWriter outputWriter;
    private final IOManager ioManager;
    private final ParticleManager particleManager;

    @Inject
    public SimulationManager(IOManager ioManager, ParticleManager particleManager, Schema schema, OutputWriter outputWriter) {
        this.schema = schema;
        this.ioManager = ioManager;
        this.outputWriter = outputWriter;
        this.particleManager = particleManager;
    }

    public void reset(final String fileName) {
        actualFileName = fileName;
        particleManager.clear();
        ioManager.reload();
        load();
    }

    public static Point<Double> calculateForces(final int id, final Point<Double> position, final Double mass,
                                                final List<Particle> particleList) {
        double forceX = 0.0;
        double forceY = 0.0;
        for (Particle particle : particleList) {
            if (!particle.getId().equals(id)) {
                double gravityForce = getGravityForce(position, particle.getPosition(), mass, particle.getMass());
                forceY += gravityForce * (particle.getPosition().getY() - position.getY()) / Particle.getDistance(particle.getPosition(), position);
                forceX += gravityForce * (particle.getPosition().getX() - position.getX()) / Particle.getDistance(particle.getPosition(), position);
            }
        }
        return new Point<>(forceX, forceY);
    }

    private double simulate() {
        return schema.updateParticles();
    }

    private void load() {
        for (Particle particle : ioManager.getInputData(actualFileName).getPlanets())
            particleManager.addParticle(particle);

        schema.init();
    }

    private static double getGravityForce(final Point<Double> pos1, final Point<Double> pos2, final double m1, final double m2) {
        return G * m1 * m2 / (Math.pow(Particle.getDistance(pos1, pos2), 2));
    }

    public void setSpaceShip(double height, double velocity) {

        Point<Double> earthPosition = particleManager.getEarth().getPosition();

        double angle = Math.atan2(earthPosition.getY(), earthPosition.getX());

        double earthHypotenuse = Particle.getDistance(earthPosition, new Point<>(0.0, 0.0));

        double spaceShipHypotenuse = earthHypotenuse + height;

        Point<Double> spaceShipPosition = new Point<>(spaceShipHypotenuse * Math.cos(angle),
                spaceShipHypotenuse * Math.sin(angle));

        particleManager.getSpaceShip().setPosition(spaceShipPosition);

        Point<Double> earthVelocity = particleManager.getEarth().getVelocity();

        Point<Double> tangentialVersor = new Point<>(Math.abs((spaceShipPosition.getY() - earthPosition.getY()) / Particle.getDistance(earthPosition, spaceShipPosition)),
                Math.abs((spaceShipPosition.getX() - earthPosition.getX()) / Particle.getDistance(earthPosition, spaceShipPosition)));

        particleManager.getSpaceShip().setVelocity(new Point<>(earthVelocity.getX() + Math.signum(earthVelocity.getX()) * tangentialVersor.getX() * velocity,
                earthVelocity.getY() + Math.signum(earthVelocity.getY()) * tangentialVersor.getY() * velocity));
    }

    public void findShortestDistance(final double height, final double velocity) {

        double distanceToJupiter = Particle.getDistance(particleManager.getSpaceShip().getPosition(), particleManager.getJupiter().getPosition());
        double distanceToSaturn = Particle.getDistance(particleManager.getSpaceShip().getPosition(), particleManager.getSaturn().getPosition());
        double tOfLessDistanceJupiter = 0;
        double tOfLessDistanceSaturn = 0;
        int elapsed = 0;

        String day = actualFileName.substring(actualFileName.indexOf("-") + 1, actualFileName.indexOf("."));

        while (elapsed <= ioManager.getConfiguration().getDuration()) {
            elapsed += simulate();

            if (elapsed % ioManager.getConfiguration().getPrint() == 0) {
                try {
                    outputWriter.write("D:" + day
                            + "-L:" + String.valueOf((height - Particle.EARTH_RADIUS) / 1000) + "-V:" + String.valueOf(velocity / 1000));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            double newDistanceToJupiter = Particle.getDistance(particleManager.getSpaceShip().getPosition(), particleManager.getJupiter().getPosition());
            double newDistanceToSaturn = Particle.getDistance(particleManager.getSpaceShip().getPosition(), particleManager.getSaturn().getPosition());
            if (newDistanceToJupiter < distanceToJupiter) {
                distanceToJupiter = newDistanceToJupiter;
                tOfLessDistanceJupiter = elapsed;
            }

            if (newDistanceToSaturn < distanceToSaturn) {
                distanceToSaturn = newDistanceToSaturn;
                tOfLessDistanceSaturn = elapsed;
            }
        }
        outputWriter.writeDistanceAndTime("Distance&Time:" + day, distanceToJupiter, distanceToSaturn,
                tOfLessDistanceJupiter, tOfLessDistanceSaturn, (height - Particle.EARTH_RADIUS) / 1000, velocity / 1000);
    }
}
