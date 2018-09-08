package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

public class Analytic {

    private double amplitude;

    private double gamma;

    private double k;

    private Particle particle;

    public Analytic(double amplitude, double gamma, double k, Particle particle) {
        this.amplitude = amplitude;
        this.gamma = gamma;
        this.k = k;
        this.particle = particle;
    }

    public Point<Double> calculatePosition(double time) {
        particle.updatePosition(new Point<>(
                amplitude * Math.exp((-gamma / (2 * particle.getMass())) * time)
                        * Math.cos(Math.sqrt(k / particle.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(particle.getMass(), 2))) * time),
                0.0
        ));

        return particle.getDoublePosition();
    }
}