package ar.edu.itba.ss.utils.io;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Rounding;

import javax.inject.Inject;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OutputWriter {

    private final String outputDirectory;
    private final String pathMeanSquaredError;
    private final BigDecimal printT;
    private final Double duration;

    @Inject
    public OutputWriter(final IOManager ioManager) {
        final Configuration configuration = ioManager.getConfiguration();
        this.printT = new BigDecimal(ioManager.getConfiguration().getPrintT());
        this.outputDirectory = configuration.getOutputDirectory();
        this.pathMeanSquaredError = configuration.getOutputDirectory() + '/' + "Mean Squared Error.txt";
        this.duration = configuration.getDuration();

        final File file = new File(configuration.getOutputDirectory());
        if (!file.exists())
            if (!file.mkdirs())
                throw new RuntimeException("Couldn't create the output directory.");

    }

    public void writeSchema(final List<Double> particlesPosX, final String schema) throws IOException {
        String path;

        switch (schema) {
            case "Analytic":
                path = outputDirectory + '/' + "Analytic.txt";
                break;
            case "Beeman":
                path = outputDirectory + '/' + "Beeman.txt";
                break;
            case "Gear":
                path = outputDirectory + '/' + "Gear.txt";
                break;
            case "Verlet":
                path = outputDirectory + '/' + "Verlet.txt";
                break;
            default:
                throw new IllegalArgumentException("No schema found by " + schema);
        }

        final Path p = Paths.get(path);

        if (Files.exists(p))
            Files.delete(p);


        int elemIndex = 0;
        int elemPrint = (int) (particlesPosX.size() * (printT.doubleValue()/duration));
        BigDecimal currentTime = new BigDecimal(0D);

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            for (Double particlePos : particlesPosX) {
                if (elemIndex % elemPrint == 0) {
                    printWriter
                            .append(String.valueOf(currentTime.setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP).doubleValue()))
                            .append("\t")
                            .append(String.valueOf(particlePos))
                            .append("\r\n");
                    currentTime = currentTime.add(printT);
                }
                elemIndex++;
            }

            printWriter.flush();
        }
    }

    public void writeMSE(final double[] mse, final String[] schema) throws IOException {
        final Path p = Paths.get(pathMeanSquaredError);

        if (Files.exists(p))
            Files.delete(p);


        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(pathMeanSquaredError, true)))) {
            printWriter
                    .append("Mean Squared Error")
                    .append("\r\n");
            for (int i = 0; i < mse.length; i++) {
                printWriter
                        .append(schema[i + 1])
                        .append("\t")
                        .append(String.valueOf(mse[i]))
                        .append("\r\n");
            }

            printWriter.flush();
        }
    }

}