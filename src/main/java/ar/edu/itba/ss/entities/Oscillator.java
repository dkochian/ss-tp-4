package ar.edu.itba.ss.entities;

public class Oscillator {

    private final double k;
    private final double dt;
    private final double gamma;
    private final Particle particle;

    public Oscillator(double k, double dt, double gamma, Particle particle) {
        this.k = k;
        this.dt = dt;
        this.gamma = gamma;
        this.particle = particle;
    }

    public double getK() {
        return k;
    }

    public double getDt() {
        return dt;
    }

    public double getGamma() { return gamma; }

    public Particle getParticle() {
        return particle;
    }

    private double getXForce(){
        return - (gamma * particle.getVelocity().getX()) -(k * particle.getDoublePosition().getX());
    }

    private double getYForce(){
        return - (gamma * particle.getVelocity().getY()) -(k * particle.getDoublePosition().getY());
    }

    public double getXAcceleration(){
        return this.getXForce() / particle.getMass();
    }

    public double getYAcceleration(){
        return this.getYForce() / particle.getMass();
    }
}
