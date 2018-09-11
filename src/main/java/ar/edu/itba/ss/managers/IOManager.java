package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.InputData;
import ar.edu.itba.ss.entities.Particle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class IOManager {
    private static final String FILENAME = "config.json";
    private static final Logger logger = LoggerFactory.getLogger(IOManager.class);

    private Configuration configuration = null;
    private InputData inputData = null;

    public Configuration getConfiguration() {
        if (configuration == null) {
            try {
                logger.debug("Loading configuration");
                configuration = read(FILENAME, Configuration.class);
            } catch (IOException e) {
                try {
                    logger.debug("Writing configuration");
                    write(FILENAME, Configuration.getDefault());
                } catch (IOException e1) {
                    logger.error(e.getMessage());
                }
                return Configuration.getDefault();
            }
        }

        return configuration;
    }

    public InputData getInputData() throws IOException {
        if (inputData == null) {
            try {
                logger.debug("Loading configuration");
                inputData = read(getConfiguration().getInputDirectory() + '/' + getConfiguration().getInputFileName(),
                        InputData.class);
            } catch (IOException e) {
                logger.error("Input data file could not be found.");

                final List<Particle> planets = new ArrayList<>();
                planets.add(new Particle(0, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(3D),
                        new BigDecimal(Math.PI), 10D, new BigDecimal(5.0)));
                planets.add(new Particle(1, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(2D),
                        new BigDecimal(Math.PI), 7D, new BigDecimal(4.0)));

                final Particle spaceship = new Particle(2, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(2D),
                        new BigDecimal(Math.PI), 15D, new BigDecimal(4.0));

                final InputData inputData = new InputData(planets, spaceship);

                write(getConfiguration().getInputDirectory() + "/" + getConfiguration().getInputFileName(), inputData);

                throw e;
            }
        }

        return inputData;
    }

    private <T> T read(final String filename, Class<T> clazz) throws IOException {
        checkAndCreateFolder(filename);
        try (final Reader reader = new BufferedReader(new FileReader(filename))) {
            final Gson gson = (new GsonBuilder()).create();
            return clazz.cast(gson.fromJson(reader, Configuration.class));
        }
    }

    private <T> void write(final String filename, final T object) throws IOException {
        checkAndCreateFolder(filename);
        try (final Writer writer = new BufferedWriter(new FileWriter(filename))) {
            final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            gson.toJson(object, writer);
        }
    }

    private void checkAndCreateFolder(final String filename) {
        final int index = filename.lastIndexOf('/');
        if (index != -1) {
            final String folder = FILENAME.substring(0, index);
            final File file = new File(folder);
            if (!file.exists())
                if (!file.mkdirs())
                    throw new RuntimeException("Couldn't create the folder: " + folder);
        }
    }
}