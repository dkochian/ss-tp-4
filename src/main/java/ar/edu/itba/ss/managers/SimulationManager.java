package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Inject;
import java.util.List;

public class SimulationManager {

    private static final double G = 6.693E-11;

    private final Schema schema;

    @Inject
    public SimulationManager(Schema schema) {
        this.schema = schema;
    }

    public void simulate() {
        schema.updateParticles();
    }

    public static Point<Double> calculateForces(final int id, final Point<Double> position, final Double mass,
                                                final List<Particle> particleList){
        double forceX = 0.0;
        double forceY = 0.0;
        for (Particle particle : particleList) {
            if (!particle.getId().equals(id)) {
                forceY += getGravityForce(position, particle.getPosition(), mass, particle.getMass()) * (particle.getPosition().getY() - position.getY()) / Particle.getDistance(particle.getPosition(), position);
                forceX += getGravityForce(position, particle.getPosition(), mass, particle.getMass()) * (particle.getPosition().getX() - position.getX()) / Particle.getDistance(particle.getPosition(), position);
            }
        }
        return new Point<>(forceX, forceY);
    }

    private static double getGravityForce(final Point<Double> pos1, final Point<Double> pos2, final double m1, final double m2) {
        return G * m1 * m2 / Math.pow(Particle.getDistance(pos1, pos2), 2);
    }
}
