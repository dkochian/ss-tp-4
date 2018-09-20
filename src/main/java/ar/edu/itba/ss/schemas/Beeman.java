package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.State;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.managers.SimulationManager;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Inject;

public class Beeman extends Schema {

    private final IOManager ioManager;

    @Inject
    public Beeman(final IOManager ioManager, final ParticleManager particleManager) {
        super(particleManager);

        this.ioManager = ioManager;
    }

    @Override
    public double updateParticles() {
        final double dt = ioManager.getConfiguration().getDt();
        for (Particle particle : particleManager.getParticleList())
            updateParticle(particle, dt);

        for (Particle particle : particleManager.getParticleList()) {
            if (particle.getId() != 1) {
                particle.setPosition(states.get(particle).getPosition());
                particle.setVelocity(states.get(particle).getVelocity());
                particle.setAcceleration(states.get(particle).getAcceleration());
            }
        }

        return dt;
    }

    private void updateParticle(final Particle particle, final double dt) {
        final Point<Double> particlePosition = particle.getPosition();
        Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = particle.getAcceleration().getX();
        double yActualAcceleration = particle.getAcceleration().getY();

        Point<Double> newPosition = new Point<>(particlePosition.getX() + particleVelocity.getX() * dt + (2 / 3.0) * xActualAcceleration * Math.pow(dt, 2) - (1 / 6.0) * states.get(particle).getAcceleration().getX() * Math.pow(dt, 2),
                particlePosition.getY() + particleVelocity.getY() * dt + (2 / 3.0) * yActualAcceleration * Math.pow(dt, 2) - (1 / 6.0) * states.get(particle).getAcceleration().getY() * Math.pow(dt, 2));

        Point<Double> newAcceleration = Particle.calculateAcceleration(particle,
                SimulationManager.calculateForces(particle.getId(), newPosition,
                        particle.getMass(), particleManager.getParticleList()));

        Point<Double> newVelocity = new Point<>(particleVelocity.getX() + (1 / 3.0) * newAcceleration.getX() * dt + (5 / 6.0) * xActualAcceleration * dt - (1 / 6.0) * states.get(particle).getAcceleration().getX() * dt,
                particleVelocity.getY() + (1 / 3.0) * newAcceleration.getY() * dt + (5 / 6.0) * yActualAcceleration * dt - (1 / 6.0) * states.get(particle).getAcceleration().getY() * dt);

        states.put(particle, new State(newPosition, newVelocity, newAcceleration));
    }
}