package ar.edu.itba.ss.entities;

public class Configuration {
    //General
    private final String outputDirectory;
    private final String outputFilename;
    private final String inputDirectory;
    private final String inputFilename;

    //Simulation
    private final int dt;
    private final int duration;
    private final double dAltitude;
    private final int tAltitude;
    private final double initialVelocity;
    private final double finalVelocity;
    private final double dVelocity;
    private final String[] schemas;

    //Animation
    private final int print;

    public Configuration(String outputDirectory, String outputFilename, String inputDirectory, String inputFilename,
                         int dt, int duration, double dAltitude, int tAltitude, double initialVelocity,
                         double finalVelocity, double dVelocity, String[] schemas, int print) {
        this.outputDirectory = outputDirectory;
        this.outputFilename = outputFilename;
        this.inputDirectory = inputDirectory;
        this.inputFilename = inputFilename;
        this.dt = dt;
        this.duration = duration;
        this.dAltitude = dAltitude;
        this.tAltitude = tAltitude;
        this.initialVelocity = initialVelocity;
        this.finalVelocity = finalVelocity;
        this.dVelocity = dVelocity;
        this.schemas = schemas;
        this.print = print;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public int getDt() {
        return dt;
    }

    public int getDuration() {
        return duration;
    }

    public double getdAltitude() {
        return dAltitude;
    }

    public int gettAltitude() {
        return tAltitude;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getFinalVelocity() {
        return finalVelocity;
    }

    public double getdVelocity() {
        return dVelocity;
    }

    public String[] getSchemas() {
        return schemas;
    }

    public int getPrint() {
        return print;
    }

    public static Configuration getDefault() {
        //General
        final String outputDirectory = "output";
        final String outputFilename = "output.tsv";
        final String inputDirectory = "input";
        final String inputFilename = "input.json";

        //Gravity System
        final int dt = 60;
        final int duration = 60 * 60 * 24 * 365 * 5;
        final double dAltitude = 1000000;
        final int tAltitude = 10000000;
        final double initialVelocity = 1000;
        final double finalVelocity = 20000;
        final double dVelocity = 4000;
        final String[] schemas = new String[]{"Beeman", "Verlet"};

        //Animation
        final int print = 60 * 24;

        return new Configuration(outputDirectory, outputFilename, inputDirectory, inputFilename, dt, duration,
                dAltitude, tAltitude, initialVelocity, finalVelocity, dVelocity, schemas, print);
    }
}