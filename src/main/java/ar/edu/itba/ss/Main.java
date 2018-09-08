package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.schemas.Analytic;
import ar.edu.itba.ss.schemas.Beeman;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.schemas.Verlet;
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

        configuration.getPrintT();

        for (String schema : configuration.getSchemas()) {
            switch (schema) {
                case "Analytic":
                    try {
                        outputWriter.writeSchema(analytic(configuration), schema);
                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Analytic solution finished.");
                    break;
                case "Beeman":
                    try {
                        outputWriter.writeSchema(schema(configuration,
                                new Beeman(createParticleAndOscillator(configuration))), schema);
                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Beeman solution finished.");
                    break;
                case "Gear":
                    //TODO: make Gear schema
                    /*try {
                        outputWriter.writeSchema(schema(configuration.getDuration(), configuration.getDt(),
                                new Gear(createParticleAndOscillator(configuration))), schema);
                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Gear solution finished.");*/
                    break;
                case "Verlet":
                    try {
                        outputWriter.writeSchema(schema(configuration,
                                new Verlet(createParticleAndOscillator(configuration))), schema);
                    } catch (IOException e) {
                        logger.error("Could not write " + schema + " points file.");
                    }
                    logger.info("Verlet solution finished.");
                    break;
                default:
                    throw new IllegalArgumentException("No schema found by " + schema);
            }
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

    private static List<Point<Double>> schema(final Configuration configuration, final Schema schema) {
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
}
