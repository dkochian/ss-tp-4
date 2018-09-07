package ar.edu.itba.ss.entities;

public class Configuration {
    //General
    private final String outputDirectory;
    private final String outputFileName;

    //Oscillator
    private final double k;
    private final double m;
    private final double dt;
    private final double gamma;
    private final double duration;
    private final double amplitude;

    //schemas
    private final String[] schemas;

    Configuration(String outputDirectory, String outputFileName, double k, double m, double dt, double gamma,
                         double duration, double amplitude, String[] schemas) {
        this.outputDirectory = outputDirectory;
        this.outputFileName = outputFileName;
        this.k = k;
        this.m = m;
        this.dt = dt;
        this.gamma = gamma;
        this.duration = duration;
        this.amplitude = amplitude;
        this.schemas = schemas;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getOutputFileName() {
        return outputFileName;
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

    public double getAmplitude() {
        return amplitude;
    }

    public String[] getSchemas() {
        return schemas;
    }

    public static Configuration getDefault() {
        //General
        final String outputDirectory = "output";
        final String outputFileName = "output.tsv";

        //Oscillator
        final double k = Math.pow(10, 4);
        final double m = 70.0;
        final double dt = 0.01;
        final double gamma = 100.0;
        final double duration = 5.0;
        final double amplitude = 1.0;

        //schemas
        final String[] schemas = new String[]{"Analytic", "Beeman", "Gear", "Verlet"};

        return new Configuration(outputDirectory, outputFileName, k, m, dt, gamma, duration, amplitude, schemas);
    }

}
