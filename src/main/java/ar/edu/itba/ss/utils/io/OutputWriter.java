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
    private final String pathQuadraticError;
    private final BigDecimal printT;

    @Inject
    public OutputWriter(final IOManager ioManager) {
        final Configuration configuration = ioManager.getConfiguration();
        this.printT = new BigDecimal(ioManager.getConfiguration().getPrintT());
        this.outputDirectory = configuration.getOutputDirectory();
        this.pathQuadraticError = configuration.getOutputDirectory() + '/' + "Quadratic Error.txt";

        final File file = new File(configuration.getOutputDirectory());
        if (!file.exists())
            if (!file.mkdirs())
                throw new RuntimeException("Couldn't create the output directory.");

    }

    public void writeSchema(final List<Point<Double>> particlesPos, final String schema) throws IOException {
        String path;
        BigDecimal auxTime = new BigDecimal(0.0);

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


        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            for (Point<Double> particlePos : particlesPos) {
                printWriter
                        .append(String.valueOf(auxTime.setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP).doubleValue()))
                        .append("\t")
                        .append(String.valueOf(particlePos.getX()))
                        .append("\t")
                        .append(String.valueOf(particlePos.getY()))
                        .append("\r\n");

                auxTime = auxTime.add(printT);
            }

            printWriter.flush();
        }
    }

}