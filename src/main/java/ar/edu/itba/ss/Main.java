package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.schemas.Analytic;
import ar.edu.itba.ss.schemas.Beeman;
import ar.edu.itba.ss.utils.other.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final IOManager ioManager = InjectorManager.getInjector().getInstance(IOManager.class);
        final Configuration configuration = ioManager.getConfiguration();

        for (String schema : configuration.getSchemas()) {
            switch (schema) {
                case "Analytic":
                    analytic(configuration);
                    break;
                case "Beeman":
                    beeman(configuration);
                    break;
                case "Gear":
                    break;
                case "Verlet":
                    break;
                default:
                    throw new IllegalArgumentException("No schema found by " + schema);
            }
        }
    }

    static void beeman(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getM());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v), new BigDecimal(angle), configuration.getM());
        Oscillator oscillator = new Oscillator(configuration.getK(), configuration.getM(), configuration.getDt(),
                configuration.getGamma(), configuration.getDuration(), particle);

        Beeman beeman = new Beeman(oscillator);

        for (double t = 0; t < configuration.getDuration(); t += configuration.getDt()) {
            beeman.updateParticle();
            System.out.println(t + "\t" + oscillator.getParticle());
        }
    }

    static void analytic(final Configuration configuration) {
        double x = configuration.getAmplitude();
        double y = 0;
        double v = (configuration.getAmplitude() * (-1) * configuration.getGamma()) / (2 * configuration.getM());
        double angle = 0;
        Particle particle = new Particle(1, new BigDecimal(x), new BigDecimal(y), new BigDecimal(v), new BigDecimal(angle), configuration.getM());

        Analytic analytic = new Analytic(configuration.getAmplitude(), configuration.getGamma(), configuration.getK(), particle);

        for (double t = 0; t < configuration.getDuration(); t += configuration.getDt()) {
            analytic.calculatePosition(t);
            System.out.println(t + "\t" + particle);
        }
    }
}
