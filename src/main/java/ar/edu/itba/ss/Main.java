package ar.edu.itba.ss;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.schemas.Analytic;
import ar.edu.itba.ss.schemas.Beeman;
import ar.edu.itba.ss.schemas.Schema;
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
                    logger.info("Analytic solution finished.");
                    break;
                case "Beeman":
                    schema(configuration.getDuration(), configuration.getDt(), new Beeman(createParticleAndOscillator(configuration)));
                    break;
                case "Gear":
                    //TODO: make Gear schema
                    //schema(configuration.getDuration(), configuration.getDt(), new Gear(createParticleAndOscillator(configuration)));
                    break;
                case "Verlet":
                    //TODO: make Verlet schema
                    //schema(configuration.getDuration(), configuration.getDt(), new Verlet(createParticleAndOscillator(configuration)));
                    break;
                default:
                    throw new IllegalArgumentException("No schema found by " + schema);
            }
        }
    }

    private static void analytic(final Configuration configuration) {
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

    private static void schema(final double duration, final double dt, final Schema schema) {
        for (double t = 0; t < duration; t += dt) {
            schema.updateParticle();
            System.out.println(t + "\t" + schema.getParticle());
        }
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
