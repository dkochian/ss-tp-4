package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.schemas.*;
import ar.edu.itba.ss.utils.io.OutputWriter;
import ar.edu.itba.ss.utils.other.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final IOManager ioManager = InjectorManager.getInjector().getInstance(IOManager.class);
        final OutputWriter outputWriter = InjectorManager.getInjector().getInstance(OutputWriter.class);
        final Configuration configuration = ioManager.getConfiguration();

        List<Double> analyticXPosition = null;
        List<Double> beemanXPosition = null;
        List<Double> gearXPosition = null;
        List<Double> verletXPosition = null;

        for (String schema : configuration.getSchemas()) {
            switch (schema) {
                case "Analytic":
                    try {
                        analyticXPosition = analytic(configuration);

                        if (beemanXPosition != analyticXPosition)
                            outputWriter.writeSchema(analyticXPosition, schema);
                        else {
                            logger.error("Nothing to analyze.");
                            return;
                        }

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Analytic solution finished.");
                    break;
                case "Beeman":
                    try {
                        beemanXPosition = schemaRun(configuration, new Beeman(createParticleAndOscillator(configuration)));

                        if (beemanXPosition != null)
                            outputWriter.writeSchema(beemanXPosition, schema);

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Beeman solution finished.");
                    break;
                case "Gear":
                    try {
                        gearXPosition = schemaRun(configuration, new Gear(createParticleAndOscillator(configuration)));

                        if (gearXPosition != null)
                            outputWriter.writeSchema(gearXPosition, schema);

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Gear solution finished.");
                    break;
                case "Verlet":
                    try {
                        verletXPosition = schemaRun(configuration, new Verlet(createParticleAndOscillator(configuration)));

                        if (verletXPosition != null)
                            outputWriter.writeSchema(verletXPosition, schema);

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Verlet solution finished.");
                    break;
                default:
                    throw new IllegalArgumentException("No schema found by " + schema);
            }
        }

        double[] mse = new double[3];

        if (analyticXPosition != null && beemanXPosition != null)
            mse[0] = measure(analyticXPosition, beemanXPosition);
        if (analyticXPosition != null && gearXPosition != null)
            mse[1] = measure(analyticXPosition, gearXPosition);
        if (analyticXPosition != null && verletXPosition != null)
            mse[2] = measure(analyticXPosition, verletXPosition);

        try {
            outputWriter.writeMSE(mse, configuration.getSchemas());
        } catch (IOException e) {
            logger.error("Could not write to file the Mean Squared Error");
        }
    }

    private static List<Double> analytic(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getParticleMass());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v), new BigDecimal(angle), configuration.getParticleMass());

        Point<Double> aux = null;
        BigDecimal dt = new BigDecimal(configuration.getDt());
        BigDecimal duration = new BigDecimal(configuration.getDuration());
        final List<Double> particlesPosX = new ArrayList<>();

        Analytic analytic = new Analytic(configuration.getAmplitude(), configuration.getGamma(), configuration.getK(), particle);

        for (BigDecimal t = new BigDecimal(0.0); t.compareTo(duration) < 0; t = t.add(dt)) {
            aux = analytic.calculatePosition(t.doubleValue());
            particlesPosX.add(aux.getX());
        }
        if (aux != null)
            particlesPosX.add(aux.getX());
        return particlesPosX;
    }

    private static List<Double> schemaRun(final Configuration configuration, final Schema schema) {
        Point<Double> aux = null;
        BigDecimal dt = new BigDecimal(configuration.getDt());
        BigDecimal duration = new BigDecimal(configuration.getDuration());
        final List<Double> particlesPosX = new ArrayList<>();
        for (BigDecimal t = new BigDecimal(0.0); t.compareTo(duration) < 0; t = t.add(dt)) {
            aux = schema.updateParticle();
            particlesPosX.add(aux.getX());
        }
        if (aux != null)
            particlesPosX.add(aux.getX());

        return particlesPosX;
    }

    private static Oscillator createParticleAndOscillator(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getParticleMass());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v),
                new BigDecimal(angle), configuration.getParticleMass());
        return new Oscillator(configuration.getK(), configuration.getDt(), configuration.getGamma(), particle);
    }

    private static double measure(final List<Double> analytic, final List<Double> schemasResults) {
        if (analytic.size() != schemasResults.size()) {
            throw new IllegalArgumentException();
        }

        double sum = 0.0;
        for (int i = 0; i < schemasResults.size(); i++) {
            sum += Math.pow(Math.abs(analytic.get(i) - schemasResults.get(i)), 2);
        }

        return sum / analytic.size();
    }
}
