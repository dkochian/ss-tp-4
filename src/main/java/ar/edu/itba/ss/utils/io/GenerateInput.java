package ar.edu.itba.ss.utils.io;

import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.utils.other.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GenerateInput {

    private static final Logger logger = LoggerFactory.getLogger(GenerateInput.class);

    private static final int SPACESHIP_INDEX = 0;
    private static final double SPACESHIP_MASS = 721.9;
    private static final double SPACESHIP_RADIUS = 5000000000.0;

    private static final int SUN_INDEX = 1;
    private static final double SUN_MASS = 1.989E30;
    private static final double SUN_RADIUS = 50000000000.0;

    private static final int EARTH_INDEX = 2;
    private static final double EARTH_MASS = 5.972E24;
    private static final double EARTH_RADIUS = 50000000000.0;

    private static final int JUPITER_INDEX = 3;
    private static final double JUPITER_MASS = 1.898E27;
    private static final double JUPITER_RADIUS = 50000000000.0;

    private static final int SATURN_INDEX = 4;
    private static final double SATURN_MASS = 5.683E26;
    private static final double SATURN_RADIUS = 50000000000.0;

    private static final int KM_TO_M = 1000;

    private final IOManager ioManager;

    @Inject
    public GenerateInput(final IOManager ioManager) {
        this.ioManager = ioManager;

        final File file = new File(ioManager.getConfiguration().getInputDirectory());
        if (!file.exists())
            if (!file.mkdirs())
                throw new RuntimeException("Couldn't create the input directory.");

    }

    public int generateInput() {
        String csvFileEarth = "data/earth.csv";
        String csvFileJupiter = "data/jupiter.csv";
        String csvFileSaturn = "data/saturn.csv";
        String earthLine;
        String jupiterLine;
        String saturnLine;
        String cvsSplitBy = ",";

        int count = 0;

        Map<Integer, Map<Integer, Map<String, Object>>> planetsAux = new HashMap<>();

        try (
                BufferedReader brEarth = new BufferedReader(new FileReader(csvFileEarth));
                BufferedReader brJupiter = new BufferedReader(new FileReader(csvFileJupiter));
                BufferedReader brSaturn = new BufferedReader(new FileReader(csvFileSaturn))
        ) {

            while ((earthLine = brEarth.readLine()) != null && (jupiterLine = brJupiter.readLine()) != null && (saturnLine = brSaturn.readLine()) != null) {
                Map<Integer, Map<String, Object>> infoPlanets = new HashMap<>();
                String[] earth = earthLine.split(cvsSplitBy);
                String[] jupiter = jupiterLine.split(cvsSplitBy);
                String[] saturn = saturnLine.split(cvsSplitBy);

                //SHIP
                final Map<String, Object> infoPlanetShip = new LinkedHashMap<>();
                infoPlanetShip.put("id", SPACESHIP_INDEX);
                infoPlanetShip.put("mass", SPACESHIP_MASS);
                infoPlanetShip.put("radius", SPACESHIP_RADIUS);
                infoPlanetShip.put("position", new Point<>(0.0, 0.0));
                infoPlanetShip.put("velocity", new Point<>(0.0, 0.0));
                infoPlanetShip.put("acceleration", new Point<>(0.0, 0.0));
                infoPlanets.put(SPACESHIP_INDEX, infoPlanetShip);

                //SUN
                final Map<String, Object> infoPlanetSun = new LinkedHashMap<>();
                infoPlanetSun.put("id", SUN_INDEX);
                infoPlanetSun.put("mass", SUN_MASS);
                infoPlanetSun.put("radius", SUN_RADIUS);
                infoPlanetSun.put("position", new Point<>(0.0, 0.0));
                infoPlanetSun.put("velocity", new Point<>(0.0, 0.0));
                infoPlanetSun.put("acceleration", new Point<>(0.0, 0.0));
                infoPlanets.put(SUN_INDEX, infoPlanetSun);

                //EARTH
                final Map<String, Object> infoPlanetEarth = new LinkedHashMap<>();
                infoPlanetEarth.put("id", EARTH_INDEX);
                infoPlanetEarth.put("mass", EARTH_MASS);
                infoPlanetEarth.put("radius", EARTH_RADIUS);
                infoPlanetEarth.put("position", new Point<>(Double.valueOf(earth[1]) * KM_TO_M, Double.valueOf(earth[2]) * KM_TO_M));
                infoPlanetEarth.put("velocity", new Point<>(Double.valueOf(earth[3]) * KM_TO_M, Double.valueOf(earth[4]) * KM_TO_M));
                infoPlanetEarth.put("acceleration", new Point<>(0.0, 0.0));
                infoPlanets.put(EARTH_INDEX, infoPlanetEarth);

                //JUPITER
                final Map<String, Object> infoPlanetJupiter = new LinkedHashMap<>();
                infoPlanetJupiter.put("id", JUPITER_INDEX);
                infoPlanetJupiter.put("mass", JUPITER_MASS);
                infoPlanetJupiter.put("radius", JUPITER_RADIUS);
                infoPlanetJupiter.put("position", new Point<>(Double.valueOf(jupiter[1]) * KM_TO_M, Double.valueOf(jupiter[2]) * KM_TO_M));
                infoPlanetJupiter.put("velocity", new Point<>(Double.valueOf(jupiter[3]) * KM_TO_M, Double.valueOf(jupiter[4]) * KM_TO_M));
                infoPlanetJupiter.put("acceleration", new Point<>(0.0, 0.0));
                infoPlanets.put(JUPITER_INDEX, infoPlanetJupiter);

                //SATURN
                final Map<String, Object> infoPlanetSaturn = new LinkedHashMap<>();
                infoPlanetSaturn.put("id", SATURN_INDEX);
                infoPlanetSaturn.put("mass", SATURN_MASS);
                infoPlanetSaturn.put("radius", SATURN_RADIUS);
                infoPlanetSaturn.put("position", new Point<>(Double.valueOf(saturn[1]) * KM_TO_M, Double.valueOf(saturn[2]) * KM_TO_M));
                infoPlanetSaturn.put("velocity", new Point<>(Double.valueOf(saturn[3]) * KM_TO_M, Double.valueOf(saturn[4]) * KM_TO_M));
                infoPlanetSaturn.put("acceleration", new Point<>(0.0, 0.0));
                infoPlanets.put(SATURN_INDEX, infoPlanetSaturn);

                logger.info(saturn[0]  + " input generated.");
                planetsAux.put(count++, infoPlanets);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int size = planetsAux.size();

        for (Integer obj : planetsAux.keySet()) {
            final Map<String, List<Map<String, Object>>> planetsMap = new HashMap<>();
            final List<Map<String, Object>> aux = new ArrayList<>();
            for (Integer obj2 : planetsAux.get(obj).keySet()) {
                aux.add(planetsAux.get(obj).get(obj2));
            }
            planetsMap.put("planets", aux);
            final Path p = Paths.get(ioManager.getConfiguration().getInputDirectory() + "/input-" + obj + ".json");

            if (Files.exists(p)) {
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            try (Writer writer = new FileWriter(p.toString())) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(planetsMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (int) Math.floor(size/2.0);
    }
}
