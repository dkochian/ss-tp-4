package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

public class Gear extends Schema {

    private Point<Double> r3;
    private Point<Double> r4;
    private Point<Double> r5;

    private double xActualAcceleration;
    private double yActualAcceleration;

    public Gear(final Oscillator oscillator) {
        super(oscillator);

        final Point<Double> particleVelocity = oscillator.getParticle().getVelocity();

        xActualAcceleration = getOscillator().getXAcceleration();
        yActualAcceleration = getOscillator().getYAcceleration();
        double coefficient1 = -getOscillator().getK() / getOscillator().getParticle().getMass();
        double coefficient2 = getOscillator().getGamma() / getOscillator().getParticle().getMass();
        r3 = new Point<>(coefficient1 * particleVelocity.getX() - coefficient2 * xActualAcceleration,
                coefficient1 * particleVelocity.getY() - coefficient2 * yActualAcceleration);
        r4 = new Point<>(coefficient1 * xActualAcceleration - coefficient2 * r3.getX(),
                coefficient1 * yActualAcceleration - coefficient2 * r3.getY());
        r5 = new Point<>(coefficient1 * r3.getX() - coefficient2 * r4.getX(),
                coefficient1 * r3.getY() - coefficient2 * r4.getY());
    }

    @Override
    public Point<Double> updateParticle() {
        final Particle particle = getOscillator().getParticle();
        final double dt = getOscillator().getDt();

        final Point<Double> particlePosition = particle.getDoublePosition();
        final Point<Double> particleVelocity = particle.getVelocity();

        double newXPosition = particlePosition.getX() + particleVelocity.getX() * dt + xActualAcceleration * Math.pow(dt, 2) / 2
                + r3.getX() * Math.pow(dt, 3) / 6 + r4.getX() * Math.pow(dt, 4) / 24 + r5.getX() * Math.pow(dt, 5) / 120;
        double newYPosition = particlePosition.getY() + particleVelocity.getY() * dt + yActualAcceleration * Math.pow(dt, 2) / 2
                + r3.getY() * Math.pow(dt, 3) / 6 + r4.getY() * Math.pow(dt, 4) / 24 + r5.getY() * Math.pow(dt, 5) / 120;

        double newXVelocity = particleVelocity.getX() + xActualAcceleration * dt + r3.getX() * Math.pow(dt, 2) / 2
                + r4.getX() * Math.pow(dt, 3) / 6 + r5.getX() * Math.pow(dt, 4) / 24;
        double newYVelocity = particleVelocity.getY() + yActualAcceleration * dt + r3.getY() * Math.pow(dt, 2) / 2
                + r4.getY() * Math.pow(dt, 3) / 6 + r5.getY() * Math.pow(dt, 4) / 24;

        xActualAcceleration = xActualAcceleration + r3.getX() * dt + r4.getX() * Math.pow(dt, 2) / 2
                + r5.getX() * Math.pow(dt, 3) / 6;

        yActualAcceleration = yActualAcceleration + r3.getY() * dt + r4.getY() * Math.pow(dt, 2) / 2
                + r5.getY() * Math.pow(dt, 3) / 6;

        r3 = new Point<>(r3.getX() + r4.getX() * dt + r5.getX() * Math.pow(dt, 2) / 2,
                r3.getY() + r4.getY() * dt + r5.getY() * Math.pow(dt, 2) / 2);
        r4 = new Point<>(r4.getX() + r5.getX() * dt, r4.getY() + r5.getY() * dt);

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(newXVelocity, newYVelocity));

        // evaluate
        double xDeltaAcceleration = getOscillator().getXAcceleration() - xActualAcceleration;
        double yDeltaAcceleration = getOscillator().getYAcceleration() - yActualAcceleration;

        double xDeltaR2 = xDeltaAcceleration * Math.pow(dt, 2) / 2;
        double yDeltaR2 = yDeltaAcceleration * Math.pow(dt, 2) / 2;

        // correct
        newXPosition = newXPosition + (3 / 16.0) * xDeltaR2;
        newYPosition = newYPosition + (3 / 16.0) * yDeltaR2;

        newXVelocity = newXVelocity + (251 / 360.0) * xDeltaR2 / dt;
        newYVelocity = newYVelocity + (251 / 360.0) * yDeltaR2 / dt;

        xActualAcceleration = xActualAcceleration + xDeltaR2 * 2 / Math.pow(dt, 2);
        yActualAcceleration = yActualAcceleration + yDeltaR2 * 2 / Math.pow(dt, 2);

        r3 = new Point<>(r3.getX() + (11 / 18.0) * xDeltaR2 * 6 / Math.pow(dt, 3),
                r3.getY() + (11 / 18.0) * yDeltaR2 * 2 / Math.pow(dt, 3));

        r4 = new Point<>(r4.getX() + (1 / 6.0) * xDeltaR2 * 24 / Math.pow(dt, 4),
                r4.getY() + (1 / 6.0) * yDeltaR2 * 24 / Math.pow(dt, 4));

        r5 = new Point<>(r5.getX() + (1 / 60.0) * xDeltaR2 * 120 / Math.pow(dt, 5),
                r5.getY() + (1 / 60.0) * yDeltaR2 * 120 / Math.pow(dt, 5));

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(newXVelocity, newYVelocity));

        return particle.getDoublePosition();
    }
}
