package ar.edu.itba.ss.Schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

public class Beeman {

    private final Oscillator oscillator;

    private double xPreviousAcceleration = 0.0;
    private double yPreviousAcceleration = 0.0;

    public Beeman(final Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    public void updateParticleBeeman() {
        final Particle particle = oscillator.getParticle();
        final double dt = oscillator.getDt();

        final Point<Double> particlePosition = particle.getDoublePosition();
        final Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = oscillator.getXAcceleration();
        double yActualAcceleration = oscillator.getYAcceleration();

        double newXPosition = particlePosition.getX() + (dt * particleVelocity.getX()) + (2 / 3.0) * xActualAcceleration * (dt * dt) - (1.0 / 6) * xPreviousAcceleration * (dt * dt);
        double newYPosition = particlePosition.getY() + (dt * particleVelocity.getY()) + (2 / 3.0) * yActualAcceleration * (dt * dt) - (1.0 / 6) * yPreviousAcceleration * (dt * dt);

        double xVelocityP = particleVelocity.getX() + (3 / 2.0) * xActualAcceleration * dt - (1 / 2.0) * xPreviousAcceleration * dt;
        double yVelocityP = particleVelocity.getY() + (3 / 2.0) * xActualAcceleration * dt - (1 / 2.0) * yPreviousAcceleration * dt;

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(xVelocityP, yVelocityP));

        xActualAcceleration = oscillator.getXAcceleration();
        yActualAcceleration = oscillator.getYAcceleration();

        double correctedXVelocity = particleVelocity.getX() + (1 / 3.0) * xActualAcceleration * dt + (5 / 6.0) * xActualAcceleration * dt - (1 / 6.0) * xPreviousAcceleration * dt;
        double correctedYVelocity = particleVelocity.getY() + (1 / 3.0) * yActualAcceleration * dt + (5 / 6.0) * yActualAcceleration * dt - (1 / 6.0) * yPreviousAcceleration * dt;

        particle.updateVelocity(new Point<>(correctedXVelocity, correctedYVelocity));

        xPreviousAcceleration = xActualAcceleration;
        yPreviousAcceleration = yActualAcceleration;

    }
}
