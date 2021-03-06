package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

public class Beeman extends Schema{

    private double xPreviousAcceleration = 0.0;
    private double yPreviousAcceleration = 0.0;

    public Beeman(final Oscillator oscillator) {
        super(oscillator);
    }

    @Override
    public Point<Double> updateParticle() {
        final Particle particle = getOscillator().getParticle();
        final double dt = getOscillator().getDt();

        final Point<Double> particlePosition = particle.getDoublePosition();
        Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = getOscillator().getXAcceleration();
        double yActualAcceleration = getOscillator().getYAcceleration();

        double newXPosition = particlePosition.getX() + particleVelocity.getX() * dt + (2 / 3.0) * xActualAcceleration * Math.pow(dt,2) - (1 / 6.0) * xPreviousAcceleration * Math.pow(dt,2);
        double newYPosition = particlePosition.getY() + particleVelocity.getY() * dt + (2 / 3.0) * yActualAcceleration * Math.pow(dt,2) - (1 / 6.0) * yPreviousAcceleration * Math.pow(dt,2);

        double xVelocityP = particleVelocity.getX() + (3 / 2.0) * xActualAcceleration * dt - (1 / 2.0) * xPreviousAcceleration * dt;
        double yVelocityP = particleVelocity.getY() + (3 / 2.0) * yActualAcceleration * dt - (1 / 2.0) * yPreviousAcceleration * dt;

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(xVelocityP, yVelocityP));

        double xNewAcceleration = getOscillator().getXAcceleration();
        double yNewAcceleration = getOscillator().getYAcceleration();

        double correctedXVelocity = particleVelocity.getX() + (1 / 3.0) * xNewAcceleration * dt + (5 / 6.0) * xActualAcceleration * dt - (1 / 6.0) * xPreviousAcceleration * dt;
        double correctedYVelocity = particleVelocity.getY() + (1 / 3.0) * yNewAcceleration * dt + (5 / 6.0) * yActualAcceleration * dt - (1 / 6.0) * yPreviousAcceleration * dt;

        particle.updateVelocity(new Point<>(correctedXVelocity, correctedYVelocity));

        xPreviousAcceleration = xActualAcceleration;
        yPreviousAcceleration = yActualAcceleration;

        return particle.getDoublePosition();
    }
}
