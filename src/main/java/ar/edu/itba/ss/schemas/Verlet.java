package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

/**
 * Velocity-Verlet
 */
public class Verlet extends Schema{

    public Verlet(final Oscillator oscillator) {
        super(oscillator);
    }

    @Override
    public Point<Double> updateParticle() {
        final Particle particle = getOscillator().getParticle();
        final double dt = getOscillator().getDt();

        final Point<Double> particlePosition = particle.getDoublePosition();
        final Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = getOscillator().getXAcceleration();
        double yActualAcceleration = getOscillator().getYAcceleration();

        double newXPosition = particlePosition.getX() + dt * particleVelocity.getX() + (Math.pow(dt,2) * xActualAcceleration);
        double newYPosition = particlePosition.getY() + dt * particleVelocity.getY() + (Math.pow(dt,2) * yActualAcceleration);

        double halfStepXVelocity = particleVelocity.getX() + xActualAcceleration * dt/2;
        double halfStepYVelocity = particleVelocity.getY() + yActualAcceleration * dt/2;

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(halfStepXVelocity, halfStepYVelocity));

        double xNewAcceleration = getOscillator().getXAcceleration();
        double yNewAcceleration = getOscillator().getYAcceleration();

        double xVelocity = halfStepXVelocity + xNewAcceleration * dt/2;
        double yVelocity = halfStepYVelocity + yNewAcceleration * dt/2;

        particle.updateVelocity(new Point<>(xVelocity, yVelocity));

        return particle.getDoublePosition();
    }
}
