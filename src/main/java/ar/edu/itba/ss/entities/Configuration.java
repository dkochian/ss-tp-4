package ar.edu.itba.ss.entities;

public class Configuration {
    //General
    private final String outputDirectory;

    //Oscillator
    private final double k;
    private final double particleMass;
    private final double dt;
    private final double gamma;
    private final double duration;
    private final double amplitude;

    //schemas
    private final String[] schemas;

    //Print Time
    private final double printT;

    Configuration(String outputDirectory, double k, double particleMass, double dt, double gamma,
                  double duration, double amplitude, double printT, String[] schemas) {
        this.outputDirectory = outputDirectory;
        this.k = k;
        this.particleMass = particleMass;
        this.dt = dt;
        this.gamma = gamma;
        this.duration = duration;
        this.amplitude = amplitude;
        this.printT = printT;
        this.schemas = schemas;

    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public double getK() {
        return k;
    }

    public double getParticleMass() {
        return particleMass;
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

    public double getAmplitude() {
        return amplitude;
    }

    public String[] getSchemas() {
        return schemas;
    }

    public double getPrintT() {
        return printT;
    }

    public static Configuration getDefault() {
        //General
        final String outputDirectory = "output";

        //Oscillator
        final double k = Math.pow(10, 4);
        final double particleMass = 70.0;
        final double dt = 0.0001;
        final double gamma = 100.0;
        final double duration = 5.0;
        final double amplitude = 1.0;

        //Print Time
        final double printT = 0.05;

        //schemas
        final String[] schemas = new String[]{"Analytic", "Beeman", "Gear", "Verlet"};

        return new Configuration(outputDirectory, k, particleMass, dt, gamma, duration, amplitude, printT, schemas);
    }

}