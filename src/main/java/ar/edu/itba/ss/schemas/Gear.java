package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

import java.time.Year;

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
        double coef = -getOscillator().getK() / getOscillator().getParticle().getMass();
        r3 = new Point<>(coef * particleVelocity.getX(), coef * particleVelocity.getY());
        r4 = new Point<>(coef * xActualAcceleration, coef * yActualAcceleration);
        r5 = new Point<>(coef * r3.getX(), coef * r3.getY());
    }

    @Override
    public Point<Double> updateParticle() {
        final Particle particle = getOscillator().getParticle();
        final double dt = getOscillator().getDt();

        final Point<Double> particlePosition = particle.getDoublePosition();
        final Point<Double> particleVelocity = particle.getVelocity();

        double newXPosition = particlePosition.getX() + particleVelocity.getX() * dt + xActualAcceleration * Math.pow(dt, 2)/2
                + r3.getX() * Math.pow(dt, 3)/6 + r4.getX() * Math.pow(dt, 4)/24 + r5.getX() * Math.pow(dt, 5)/120;
        double newYPosition = particlePosition.getY() + particleVelocity.getY() * dt + yActualAcceleration * Math.pow(dt, 2)/2
                + r3.getY() * Math.pow(dt, 3)/6 + r4.getY() * Math.pow(dt, 4)/24 + r5.getY() * Math.pow(dt, 5)/120;

        double newXVelocity = particleVelocity.getX() + xActualAcceleration * dt + r3.getX() * Math.pow(dt, 2)/2
                + r4.getX() * Math.pow(dt, 3)/6 + r5.getX() * Math.pow(dt, 4)/24;
        double newYVelocity = particleVelocity.getY() + yActualAcceleration * dt + r3.getY() * Math.pow(dt, 2)/2
                + r4.getY() * Math.pow(dt, 3)/6 + r5.getY() * Math.pow(dt, 4)/24;

        xActualAcceleration = xActualAcceleration + r3.getX() * dt + r4.getX() * Math.pow(dt, 2)/2
                + r5.getX() * Math.pow(dt, 3)/6;

        yActualAcceleration = yActualAcceleration + r3.getY() * dt + r4.getY() * Math.pow(dt, 2)/2
                + r5.getY() * Math.pow(dt, 3)/6;

        r3 = new Point<>(r3.getX() + r4.getX() * dt + r5.getX() * Math.pow(dt, 2)/2,
                r3.getY() + r4.getY() * dt + r5.getY() * Math.pow(dt, 2)/2);
        r4 = new Point<>(r4.getX() + r5.getX() * dt, r4.getY() + r5.getY() * dt);

        r5 = new Point<>((-getOscillator().getK() / getOscillator().getParticle().getMass()) * r3.getX(),
                (-getOscillator().getK() / getOscillator().getParticle().getMass()) * r3.getY());

        // evaluate

        double xDeltaAcceleration = getOscillator().getXAcceleration() - xActualAcceleration;

        double yDeltaAcceleration = getOscillator().getYAcceleration() - yActualAcceleration;

        double xDeltaR2 = xDeltaAcceleration * Math.pow(dt, 2)/2;

        double yDeltaR2 = yDeltaAcceleration * Math.pow(dt, 2)/2;

        // correct

        newXPosition = newXPosition + (3/20) * xDeltaR2;
        newYPosition = newYPosition + (3/20) * yDeltaR2;

        newXVelocity = newXVelocity + (251/360) * xDeltaR2/dt;
        newYVelocity = newYVelocity + (251/360) * yDeltaR2/dt;

        xActualAcceleration = xActualAcceleration +  xDeltaR2 * 2 / Math.pow(dt, 2);
        yActualAcceleration = yActualAcceleration +  yDeltaR2 * 2 / Math.pow(dt, 2);

        r3 = new Point<>(r3.getX() + (11/18) * xDeltaR2 * 6 / Math.pow(dt, 3),
                r3.getY() + (11/18) * yDeltaR2 * 2 / Math.pow(dt, 3));

        r4 = new Point<>(r3.getX() + (1/6) * xDeltaR2 * 24 / Math.pow(dt, 4),
                r3.getY() + (1/6) * yDeltaR2 * 24 / Math.pow(dt, 4));

        r5 = new Point<>(r3.getX() + (1/60) * xDeltaR2 * 120 / Math.pow(dt, 5),
                r3.getY() + (1/60) * yDeltaR2 * 120 / Math.pow(dt, 5));

        particle.updatePosition(new Point<>(newXPosition, newYPosition));
        particle.updateVelocity(new Point<>(newXVelocity, newYVelocity));

        return particle.getDoublePosition();
    }
}
