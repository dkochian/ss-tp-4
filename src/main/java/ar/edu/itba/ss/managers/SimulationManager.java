package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.Spaceship;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Inject;
import java.util.List;

public class SimulationManager {

    private static final double G = 6.693E-11;

    private static final int SPACESHIP_INDEX = 0;
    private static final int EARTH_INDEX = 2;
    
    private final Schema schema;
    private final ParticleManager particleManager;

    @Inject
    public SimulationManager(IOManager ioManager, ParticleManager particleManager, Schema schema) {
        this.schema = schema;
        this.particleManager = particleManager;

        for(Particle particle : ioManager.getInputData().getPlanets())
            particleManager.addParticle(particle);

        schema.init();
    }

    public double simulate(final double height) {
        for(Particle particle : particleManager.getParticleList()) {
            if (particle instanceof Spaceship) {
                ((Spaceship) particle).setHeight(height);
                break;
            }
        }

        return schema.updateParticles();
    }

    public static Point<Double> calculateForces(final int id, final Point<Double> position, final Double mass,
                                                final List<Particle> particleList){
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

    private static double getGravityForce(final Point<Double> pos1, final Point<Double> pos2, final double m1, final double m2) {
        return G * m1 * m2 / (Math.pow(Particle.getDistance(pos1, pos2), 2));
    }

    public void setSpaceShip(double height, double velocity) {

        Point<Double> earthPosition = particleManager.getParticleList().get(EARTH_INDEX).getPosition();

        double angle = Math.atan2(earthPosition.getY(), earthPosition.getX());

        double earthHypotenuse = Particle.getDistance(earthPosition, new Point<>(0.0, 0.0));

        double spaceShipHypotenuse = earthHypotenuse + height;

        Point<Double> spaceShipPosition = new Point<>(spaceShipHypotenuse * Math.sin(angle),
                spaceShipHypotenuse * Math.cos(angle));

        particleManager.getParticleList().get(SPACESHIP_INDEX).setPosition(spaceShipPosition);

        Point<Double> earthVelocity = particleManager.getParticleList().get(EARTH_INDEX).getVelocity();

        Point<Double> tangentialVersor = new Point<>(-1 * ((earthPosition.getY() - spaceShipPosition.getY()) / Particle.getDistance(earthPosition, spaceShipPosition)),
                (earthPosition.getX() - spaceShipPosition.getX()) / Particle.getDistance(earthPosition, spaceShipPosition));

        particleManager.getParticleList().get(SPACESHIP_INDEX).setVelocity(new Point<>(earthVelocity.getX() + tangentialVersor.getX() * velocity,
                earthVelocity.getY() + tangentialVersor.getY() * velocity));
    }
}
