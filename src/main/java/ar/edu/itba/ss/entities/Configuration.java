package ar.edu.itba.ss.entities;

public class Configuration {
    //General
    private final String inputDirectory;
    private final String outputDirectory;
    private final String inputFileName;

    //Oscillator
    private final double k;
    private final double m;
    private final double dt;
    private final double gamma;
    private final double duration;
    private final double amplitude;

    //schemas
    private final String[] schemas;

    //Print Time
    private final double printT;

    Configuration(String inputDirectory, String outputDirectory, String inputFileName, double k, double m, double dt, double gamma,
                         double duration, double amplitude, double printT, String[] schemas) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.inputFileName = inputDirectory;
        this.k = k;
        this.m = m;
        this.dt = dt;
        this.gamma = gamma;
        this.duration = duration;
        this.amplitude = amplitude;
        this.printT = printT;
        this.schemas = schemas;

    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getInputFileName() {
        return inputFileName;
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

    public double getPrintT() {
        return printT;
    }

    public static Configuration getDefault() {
        //General
        final String inputDirectory = "input";
        final String outputDirectory = "output";
        final String inputFileName = "data.json";

        //Oscillator
        final double k = Math.pow(10, 4);
        final double m = 70.0;
        final double dt = 0.0001;
        final double gamma = 100.0;
        final double duration = 5.0;
        final double amplitude = 1.0;

        //Print Time
        final double printT = 0.05;

        //schemas
        final String[] schemas = new String[]{"Beeman", "Gear", "Verlet"};

        //Sun - Mass, Radius, Xo, Yo, VXo, VYo
        final String[] sun = new String[]{};

        //Earth
        final String[] earth = new String[]{};

        //Jupiter
        final String[] jupiter = new String[]{};

        //Saturn
        final String[] saturn = new String[]{};

        //Ship - Mass,
        final String[] ship = new String[]{};

        return new Configuration(inputDirectory, outputDirectory, inputFileName, k, m, dt, gamma, duration, amplitude, printT, schemas);
    }

}
