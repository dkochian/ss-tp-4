package ar.edu.itba.ss.entities;

public class Oscillator {

    private final double k;
    private final double m;
    private final double dt;
    private final double gamma;
    private final double duration;
    private final Particle particle;

    public Oscillator(double k, double m, double dt, double gamma, double duration, Particle particle) {
        this.k = k;
        this.m = m;
        this.dt = dt;
        this.gamma = gamma;
        this.duration = duration;
        this.particle = particle;
    }

    public double getK() {
        return k;
    }

    public double getM() {
        return m;
    }

    public double getDt() {
        return dt;
    }

    public double getGamma() {
        return gamma;
    }

    public double getDuration() {
        return duration;
    }

    public Particle getParticle() {
        return particle;
    }

    public double getXForce(){
        return - (gamma * particle.getVelocity().getX()) -(k * particle.getPosition().getX().doubleValue());
    }

    public double getYForce(){
        return - (gamma * particle.getVelocity().getY()) -(k * particle.getPosition().getY().doubleValue());
    }

    public double getXAcceleration(){
        return this.getXForce() / particle.getMass();
    }

    public double getYAcceleration(){
        return this.getYForce() / particle.getMass();
    }
}
