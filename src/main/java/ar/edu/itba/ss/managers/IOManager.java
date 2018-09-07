package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Configuration;
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

    public Configuration getConfiguration() {
        if (configuration == null) {
            try {
                logger.debug("Loading configuration");
                configuration = read();
            } catch (IOException e) {
                try {
                    logger.debug("Writing configuration");
                    write(Configuration.getDefault());
                } catch (IOException e1) {
                    logger.error(e.getMessage());
                }
                return Configuration.getDefault();
            }
        }

        return configuration;
    }

    private Configuration read() throws IOException {
        checkAndCreateFolder();
        try (final Reader reader = new BufferedReader(new FileReader(FILENAME))) {
            final Gson gson = (new GsonBuilder()).create();
            return Configuration.class.cast(gson.fromJson(reader, Configuration.class));
        }
    }

    private <T> void write(final T object) throws IOException {
        checkAndCreateFolder();
        try (final Writer writer = new BufferedWriter(new FileWriter(FILENAME))) {
            final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            gson.toJson(object, writer);
        }
    }

    private void checkAndCreateFolder() {
        final int index = FILENAME.lastIndexOf('/');
        if (index != -1) {
            final String folder = FILENAME.substring(0, index);
            final File file = new File(folder);
            if (!file.exists())
                if (!file.mkdirs())
                    throw new RuntimeException("Couldn't create the folder: " + folder);
        }
    }
}