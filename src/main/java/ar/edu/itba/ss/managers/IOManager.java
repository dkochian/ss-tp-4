package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Configuration;
import ar.edu.itba.ss.entities.InputData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;

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

    public InputData getInputData() {
        if (inputData == null) {
            final String path = getConfiguration().getInputDirectory() + '/' + getConfiguration().getInputFilename();
            try {
                logger.debug("Loading configuration");
                inputData = read(path,
                        InputData.class);
            } catch (IOException e) {
                logger.error("Input data file could not be found.");
            }
        }

        return inputData;
    }

    public void reload() {
        inputData = null;
        configuration = null;
    }

    private <T> T read(final String filename, Class<T> clazz) throws IOException {
        checkAndCreateFolder(filename);
        try (final Reader reader = new BufferedReader(new FileReader(filename))) {
            final Gson gson = (new GsonBuilder()).create();
            return clazz.cast(gson.fromJson(reader, clazz));
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
            final String folder = filename.substring(0, index);
            final File file = new File(folder);
            if (!file.exists())
                if (!file.mkdirs())
                    throw new RuntimeException("Couldn't create the folder: " + folder);
        }
    }
}