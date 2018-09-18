package ar.edu.itba.ss.entities;

public class Configuration {
    //General
    private final String outputDirectory;
    private final String outputFilename;
    private final String inputDirectory;
    private final String inputFilename;

    //Simulation
    private final double dt;
    private final double duration;
    private final double dAltitude;
    private final int tAltitude;
    private final String[] schemas;

    //Animation
    private final double print;

    public Configuration(String outputDirectory, String outputFilename, String inputDirectory, String inputFilename,
                         double dt, double duration, double dAltitude, int tAltitude, String[] schemas, double print) {
        this.outputDirectory = outputDirectory;
        this.outputFilename = outputFilename;
        this.inputDirectory = inputDirectory;
        this.inputFilename = inputFilename;
        this.dt = dt;
        this.duration = duration;
        this.dAltitude = dAltitude;
        this.tAltitude = tAltitude;
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

    public double getDt() {
        return dt;
    }

    public double getDuration() {
        return duration;
    }

    public double getdAltitude() {
        return dAltitude;
    }

    public int gettAltitude() {
        return tAltitude;
    }

    public String[] getSchemas() {
        return schemas;
    }

    public double getPrint() {
        return print;
    }

    public static Configuration getDefault() {
        //General
        final String outputDirectory = "output";
        final String outputFilename = "output.tsv";
        final String inputDirectory = "input";
        final String inputFilename = "input.json";

        //Oscillator
        final double dt = 60;
        final double duration = 5.0;
        final double dAltitude = 100.0;
        final int tAltitude = 100;
        final String[] schemas = new String[]{"Analytic", "Beeman", "Gear", "Verlet"};

        //Animation
        final double print = 60*24;

        return new Configuration(outputDirectory, outputFilename, inputDirectory, inputFilename, dt, duration,
                dAltitude, tAltitude, schemas, print);
    }
}