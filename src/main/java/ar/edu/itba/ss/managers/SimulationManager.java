package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.Spaceship;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.utils.io.OutputWriter;
import ar.edu.itba.ss.utils.other.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationManager {
    private static final Logger logger = LoggerFactory.getLogger(SimulationManager.class);

    private static final double G = 6.693E-11;

    private static final int SPACESHIP_INDEX = 0;
    private static final int EARTH_INDEX = 2;

    private static final double DAY = 24 * 3600.0;
    private static final double WEEK = DAY * 7;
    private static final double MONTH = DAY * 31;
    private static final double YEAR = DAY * 365;


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

        load();
    }

    public void reset() {
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

    private double simulate(final double height) {
        for (Particle particle : particleManager.getParticleList()) {
            if (particle instanceof Spaceship) {
                ((Spaceship) particle).setHeight(height);
                break;
            }
        }

        return schema.updateParticles();
    }

    private void load() {
        for (Particle particle : ioManager.getInputData().getPlanets())
            particleManager.addParticle(particle);

        schema.init();
    }

    private static double getGravityForce(final Point<Double> pos1, final Point<Double> pos2, final double m1, final double m2) {
        return G * m1 * m2 / (Math.pow(Particle.getDistance(pos1, pos2), 2));
    }

    public void setSpaceShip(double height, double velocity) {

        Point<Double> earthPosition = particleManager.getParticleList().get(EARTH_INDEX).getPosition();

        double angle = Math.atan2(earthPosition.getY(), earthPosition.getX());

        double earthHypotenuse = Particle.getDistance(earthPosition, new Point<>(0.0, 0.0));

        double spaceShipHypotenuse = earthHypotenuse + height;

        Point<Double> spaceShipPosition = new Point<>(spaceShipHypotenuse * Math.cos(angle),
                spaceShipHypotenuse * Math.sin(angle));

        particleManager.getParticleList().get(SPACESHIP_INDEX).setPosition(spaceShipPosition);

        Point<Double> earthVelocity = particleManager.getParticleList().get(EARTH_INDEX).getVelocity();

        Point<Double> tangentialVersor = new Point<>(Math.abs((spaceShipPosition.getY() - earthPosition.getY()) / Particle.getDistance(earthPosition, spaceShipPosition)),
                Math.abs((spaceShipPosition.getX() - earthPosition.getX()) / Particle.getDistance(earthPosition, spaceShipPosition)));

        particleManager.getParticleList().get(SPACESHIP_INDEX).setVelocity(new Point<>(earthVelocity.getX() + Math.signum(earthVelocity.getX()) * tangentialVersor.getX() * velocity,
                earthVelocity.getY() + Math.signum(earthVelocity.getY()) * tangentialVersor.getY() * velocity));
    }

    public void findShortestDistance(final double height) {

        double distanceToJupiter = Particle.getDistance(particleManager.getEarth().getPosition(), particleManager.getJupiter().getPosition());
        double distanceToSaturn = Particle.getDistance(particleManager.getEarth().getPosition(), particleManager.getSaturn().getPosition());
        double tOfLessDistanceJupiter = 0;
        double tOfLessDistanceSaturn = 0;
        int elapsed = 0;

        while (elapsed <= ioManager.getConfiguration().getDuration()) {
            elapsed += simulate(height);

            if (elapsed % ioManager.getConfiguration().getPrint() == 0) {
                try {
                    outputWriter.write(String.valueOf(height));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            double newDistanceToJupiter = Particle.getDistance(particleManager.getEarth().getPosition(), particleManager.getJupiter().getPosition());
            double newDistanceToSaturn = Particle.getDistance(particleManager.getEarth().getPosition(), particleManager.getSaturn().getPosition());
            if (newDistanceToJupiter < distanceToJupiter) {
                distanceToJupiter = newDistanceToJupiter;
                tOfLessDistanceJupiter = elapsed;
            }

            if (newDistanceToSaturn < distanceToSaturn) {
                distanceToSaturn = newDistanceToSaturn;
                tOfLessDistanceSaturn = elapsed;
            }
        }

        System.out.println("To Jupiter Day of min distance: " + tOfLessDistanceJupiter / DAY + ". Distance: " + distanceToJupiter);
        System.out.println("To Saturn Day of min distance: " + tOfLessDistanceSaturn / DAY + ". Distance: " + distanceToSaturn);
    }

    public List<Double> findDistances(double finalDay, double height) {
        final Particle earth = particleManager.getEarth();
        final Particle jupiter = particleManager.getJupiter();
        final Particle saturn = particleManager.getSaturn();
        final Particle ship = particleManager.getShip();

        double originalDistanceToJupiter = distanceToJupiter();

        double minDistanceToJupiter = Particle.getDistance(earth.getPosition(), jupiter.getPosition());
        double minDistanceToSaturn = Particle.getDistance(earth.getPosition(), saturn.getPosition());
        double dtOfMinDistance = 0;
        double earthToJupiter = minDistanceToJupiter - (earth.getRadius() + jupiter.getRadius());
        double earthToSaturn = minDistanceToSaturn - (earth.getRadius() + saturn.getRadius());

        int elapsed = 0;

        Particle crashedWith = null;

        while (elapsed <= ioManager.getConfiguration().getDuration() && crashedWith == null && elapsed < DAY * finalDay) {
            elapsed += simulate(height);


            double newDistanceJupiter = distanceToJupiter();
            if (newDistanceJupiter < minDistanceToJupiter) {
                minDistanceToJupiter = newDistanceJupiter;
                dtOfMinDistance = elapsed;
            }

            double newDistanceEarthJupiter = Particle.getDistance(earth.getPosition(), jupiter.getPosition()) - (earth.getRadius() + jupiter.getRadius());
            if (newDistanceEarthJupiter < earthToJupiter) {
                earthToJupiter = newDistanceEarthJupiter;
            }

            crashedWith = hasCrashed(height);
        }

        double finalVelocity = Math.sqrt(Math.pow((ship.getVelocity().getX() - jupiter.getVelocity().getX()), 2) +
                Math.pow((ship.getVelocity().getY() - jupiter.getVelocity().getY()), 2));

        List<Double> answer = new ArrayList<>();

        answer.add(minDistanceToJupiter);
        answer.add(originalDistanceToJupiter);
        answer.add(finalVelocity);
        answer.add(dtOfMinDistance / DAY);

        if (minDistanceToJupiter == originalDistanceToJupiter || (dtOfMinDistance / DAY) <= 0.9) {
            return null;
        }

        if (crashedWith != null) answer.add(Double.valueOf(crashedWith.getId()));

        return answer;
    }

    private double distanceToJupiter() {
        return Particle.getDistance(particleManager.getShip().getPosition(), particleManager.getJupiter().getPosition()) - particleManager.getJupiter().getRadius();
    }

    private Particle hasCrashed(double height) {
        final Particle sun = particleManager.getSun();
        final Particle earth = particleManager.getEarth();
        final Particle ship = particleManager.getShip();
        if (Particle.getDistance(ship.getPosition(), sun.getPosition()) < (ship.getRadius() + sun.getRadius() + height)) {
            return sun;
        } else if(Particle.getDistance(ship.getPosition(), earth.getPosition()) < (ship.getRadius() + earth.getRadius()))
            return earth;

        return null;
    }

}
