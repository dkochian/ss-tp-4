package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.State;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.managers.SimulationManager;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Inject;

/**
 * Velocity-Verlet
 */
public class Verlet extends Schema{

    private final IOManager ioManager;

    @Inject
    public Verlet(final IOManager ioManager, final ParticleManager particleManager) {
        super(particleManager);

        this.ioManager = ioManager;
    }

    @Override
    public double updateParticles() {
        final double dt = ioManager.getConfiguration().getDt();
        for (Particle particle : particleManager.getParticleList())
            updateParticle(particle, dt);

        for (Particle particle : particleManager.getParticleList()) {
            particle.setPosition(states.get(particle).getPosition());
            particle.setVelocity(states.get(particle).getVelocity());
            particle.setAcceleration(states.get(particle).getAcceleration());
        }

        return dt;
    }

    private void updateParticle(final Particle particle, final double dt) {
        final Point<Double> particlePosition = particle.getPosition();
        Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = particle.getAcceleration().getX();
        double yActualAcceleration = particle.getAcceleration().getY();

        Point<Double> newPosition = new Point<>(particlePosition.getX() + dt * particleVelocity.getX() + (Math.pow(dt,2) * xActualAcceleration),
                particlePosition.getY() + dt * particleVelocity.getY() + (Math.pow(dt,2) * yActualAcceleration));

        Point<Double> newAcceleration = Particle.calculateAcceleration(particle,
                SimulationManager.calculateForces(particle.getId(), newPosition,
                        particle.getMass(), particleManager.getParticleList()));

        Point<Double> newVelocity = new Point<>(particleVelocity.getX() + xActualAcceleration * dt / 2 +  newAcceleration.getX() * dt /2,
                particleVelocity.getY() + yActualAcceleration * dt / 2 +  newAcceleration.getY() * dt /2);

        states.put(particle, new State(newPosition, newVelocity, newAcceleration));
    }
}