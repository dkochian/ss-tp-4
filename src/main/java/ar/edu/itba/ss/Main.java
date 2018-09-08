package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.schemas.*;
import ar.edu.itba.ss.utils.io.OutputWriter;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Rounding;
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

        List<Point<Double>> analyticPoints = null;
        List<Point<Double>> beemanPoints = null;
        List<Point<Double>> gearPoints = null;
        List<Point<Double>> verletPoints = null;

        for (String schema : configuration.getSchemas()) {
            switch (schema) {
                case "Analytic":
                    try {
                        analyticPoints = analytic(configuration);

                        if (beemanPoints != analyticPoints)
                            outputWriter.writeSchema(analyticPoints, schema);
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
                        beemanPoints = schemaRun(configuration, new Beeman(createParticleAndOscillator(configuration)));

                        if (beemanPoints != null)
                            outputWriter.writeSchema(beemanPoints, schema);

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Beeman solution finished.");
                    break;
                case "Gear":
                    try {
                        gearPoints = schemaRun(configuration, new Gear(createParticleAndOscillator(configuration)));

                        if (gearPoints != null)
                            outputWriter.writeSchema(gearPoints, schema);

                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Gear solution finished.");
                    break;
                case "Verlet":
                    try {
                        verletPoints = schemaRun(configuration, new Verlet(createParticleAndOscillator(configuration)));

                        if (verletPoints != null)
                            outputWriter.writeSchema(verletPoints, schema);

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

        if (analyticPoints != null && beemanPoints != null)
            mse[0] = measure(analyticPoints, beemanPoints);
        if (analyticPoints != null && gearPoints != null)
            mse[1] = measure(analyticPoints, gearPoints);
        if (analyticPoints != null && verletPoints != null)
            mse[2] = measure(analyticPoints, verletPoints);

        try {
            outputWriter.writeMSE(mse, configuration.getSchemas());
        } catch (IOException e) {
            logger.error("Could not write to file the Mean Squared Error");
        }
    }

    private static List<Point<Double>> analytic(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getM());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v), new BigDecimal(angle), configuration.getM());

        double printT = configuration.getPrintT();

        Point<Double> aux = null;
        BigDecimal auxTime = new BigDecimal(0.0);
        BigDecimal dt = new BigDecimal(configuration.getDt());
        BigDecimal duration = new BigDecimal(configuration.getDuration());
        final List<Point<Double>> particlesPos = new ArrayList<>();

        Analytic analytic = new Analytic(configuration.getAmplitude(), configuration.getGamma(), configuration.getK(), particle);

        for (BigDecimal t = new BigDecimal(0.0); t.compareTo(duration) < 0; t = t.add(dt)) {
            aux = analytic.calculatePosition(t.setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP).doubleValue());
            if (Double.compare(auxTime.setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP).doubleValue(), printT) == 0) {
                particlesPos.add(aux);
                auxTime = new BigDecimal(0.0);
            }
            auxTime = auxTime.add(dt);
        }
        particlesPos.add(aux);
        return particlesPos;
    }

    private static List<Point<Double>> schemaRun(final Configuration configuration, final Schema schema) {
        Point<Double> aux = null;
        BigDecimal auxTime = new BigDecimal(0.0);
        BigDecimal dt = new BigDecimal(configuration.getDt());
        BigDecimal duration = new BigDecimal(configuration.getDuration());
        double printT = configuration.getPrintT();
        final List<Point<Double>> particlesPos = new ArrayList<>();
        for (BigDecimal t = new BigDecimal(0.0); t.compareTo(duration) < 0; t = t.add(dt)) {
            aux = schema.updateParticle();
            if (Double.compare(auxTime.setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP).doubleValue(), printT) == 0) {
                particlesPos.add(aux);
                auxTime = new BigDecimal(0.0);
            }
            auxTime = auxTime.add(dt);
        }
        particlesPos.add(aux);
        return particlesPos;
    }

    private static Oscillator createParticleAndOscillator(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getM());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v), new BigDecimal(angle), configuration.getM());
        return new Oscillator(configuration.getK(), configuration.getM(), configuration.getDt(),
                configuration.getGamma(), configuration.getDuration(), particle);
    }

    private static double measure(final List<Point<Double>> analytic, final List<Point<Double>> schemasResults) {
        if (analytic.size() != schemasResults.size()) {
            throw new IllegalArgumentException();
        }

        double sum = 0.0;
        for (int i = 0; i < schemasResults.size(); i++) {
            sum += Math.pow(Math.abs(analytic.get(i).getX() - schemasResults.get(i).getX()), 2);
        }

        return sum / analytic.size();
    }
}
