package ar.edu.itba.ss.entities;

import java.util.List;

public class GravityForce {

    private static final double G = 6.693E-11;

    private Particle particle;
    private List<Particle> particlesNeighbours;


    public GravityForce(final Particle particle, final List<Particle> particlesNeighbours) {
        this.particle = particle;
        this.particlesNeighbours = particlesNeighbours;
    }

    private double getGravityForce(final Particle particle, final Particle particle2) {
        return G * particle.getMass() * particle2.getMass() / Math.pow(particle.getDistance(particle2), 2);
    }

    public double getXGravityForce() {
        double sumX = 0;
        for (Particle particle2 : particlesNeighbours)
            sumX -= getGravityForce(particle, particle2) * (particle.getDoublePosition().getX() - particle2.getDoublePosition().getX()) / particle.getDistance(particle2);

        return sumX;
    }

    public double getYGravityForce() {
        double sumY = 0;
        for (Particle particle2 : particlesNeighbours)
            sumY -= getGravityForce(particle, particle2) * (particle.getDoublePosition().getY() - particle2.getDoublePosition().getY()) / particle.getDistance(particle2);

        return sumY;
    }
}
