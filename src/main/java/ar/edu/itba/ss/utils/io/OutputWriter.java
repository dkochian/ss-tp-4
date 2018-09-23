package ar.edu.itba.ss.utils.io;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.managers.SimulationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static jdk.nashorn.internal.parser.DateParser.DAY;

public class OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(OutputWriter.class);

    private final IOManager ioManager;
    private final ParticleManager particleManager;

    private int counter = 0;

    @Inject
    public OutputWriter(final IOManager ioManager, final ParticleManager particleManager) {
        this.ioManager = ioManager;
        this.particleManager = particleManager;

        final File file = new File(ioManager.getConfiguration().getOutputDirectory());
        if (!file.exists())
            if (!file.mkdirs())
                throw new RuntimeException("Couldn't create the output directory.");

    }

    public void write(final String postFix) throws IOException {
        final String path = getPath(postFix);

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            printWriter
                    .append(String.valueOf(particleManager.getParticleList().size()))
                    .append("\r\n")
                    .append(String.valueOf(counter++))
                    .append("\r\n");

            for (Particle particle : particleManager.getParticleList())
                printWriter
                        .append(String.valueOf(particle.getPosition().getX()))
                        .append('\t')
                        .append(String.valueOf(particle.getPosition().getY()))
                        .append('\t')
                        .append(String.valueOf(particle.getVelocity().getX()))
                        .append('\t')
                        .append(String.valueOf(particle.getVelocity().getY()))
                        .append('\t')
                        .append(String.valueOf(particle.getRadius()))
                        .append('\t')
                        .append(String.valueOf(particle.getMass()))
                        .append('\t')
                        .append(String.valueOf(particle.getId()))
                        .append("\r\n");

            printWriter.flush();
        }
    }

    public void writeDistanceAndTime(final String postFix, final double distanceToJupiter, final double distanceToSaturn,
                                     final double tOfLessDistanceJupiter, final double tOfLessDistanceSaturn, final double height,
                                     final double velocity) {
        final String path = getPath(postFix);

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            printWriter
                    .append("L = ").append(String.valueOf(height)).append(" km, V = ").append(String.valueOf(velocity )).append(" km/s.")
                    .append("\n\t")
                    .append("To Jupiter ")
                    .append("\n\t\t")
                    .append("Min distance: ").append(String.valueOf(distanceToJupiter))
                    .append("\n\t\t")
                    .append("Time: ").append(String.valueOf(((int) (tOfLessDistanceJupiter / SimulationManager.YEAR)))).append(" years ")
                    .append(String.valueOf(((int) ((tOfLessDistanceJupiter % SimulationManager.YEAR) / SimulationManager.DAY)))).append(" days.")
                    .append("\n\t")
                    .append("To Saturn ")
                    .append("\n\t\t")
                    .append("Min distance: ").append(String.valueOf(distanceToSaturn))
                    .append("\n\t\t")
                    .append("Time: ").append(String.valueOf(((int) (tOfLessDistanceSaturn / SimulationManager.YEAR)))).append(" years ")
                    .append(String.valueOf(((int) ((tOfLessDistanceSaturn % SimulationManager.YEAR) / SimulationManager.DAY)))).append(" days.")
                    .append("\n\n");

            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void remove(final String postFix) {
        final Path p = Paths.get(getPath(postFix));

        if (Files.exists(p)) {
            try {
                Files.delete(p);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private String getPath(final String postFix) {
        final int index = ioManager.getConfiguration().getOutputFilename().lastIndexOf('.');
        String path = ioManager.getConfiguration().getOutputDirectory() + "/"
                + ioManager.getConfiguration().getOutputFilename() + postFix;

        if (index != -1) {
            final String name = ioManager.getConfiguration().getOutputFilename().substring(0, index);
            final String extension = ioManager.getConfiguration().getOutputFilename().substring(index);
            final String newName = name + "-" + postFix + extension;
            path = ioManager.getConfiguration().getOutputDirectory() + "/" + newName;
        }

        return path;
    }

}