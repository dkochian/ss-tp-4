package ar.edu.itba.ss.schemas;

public class Analytic {

    private double initialPosition;

    private double gamma;

    private double k;

    private double particleMass;

    public Analytic(double initialPosition, double gamma, double k, double particleMass) {
        this.initialPosition = initialPosition;
        this.gamma = gamma;
        this.k = k;
        this.particleMass = particleMass;
    }

    public double calculatePosition(double time) {
        return initialPosition * Math.exp((-gamma /(2*particleMass)) * time)
                * Math.cos(Math.sqrt(k/particleMass - Math.pow(gamma, 2)/(4*Math.pow(particleMass, 2))) * time);
    }
}
