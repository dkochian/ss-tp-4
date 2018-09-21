package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class ParticleManager {

    private List<Particle> particleList = new ArrayList<>();

    private static final int SHIP_ID = 0;
    private static final int SUN_ID = 1;
    private static final int EARTH_ID = 2;
    private static final int JUPITER_ID = 3;
    private static final int SATURN_ID = 4;

    boolean addParticle(final Particle p) {
        return particleList.add(p);
    }

    public List<Particle> getParticleList() {
        return Collections.unmodifiableList(particleList);
    }

    public void updateParticles(final Point<Double> forces) {
        for (Particle particle : particleList)
            particle.update(forces);

    }

    void clear(){
        particleList.clear();
    }

    Particle getEarth(){
        for (Particle particle : particleList)
            if (particle.getId().equals(EARTH_ID))
                return particle;
        return null;
    }

    Particle getJupiter(){
        for (Particle particle : particleList)
            if (particle.getId().equals(SATURN_ID))
                return particle;
        return null;
    }

    Particle getSaturn(){
        for (Particle particle : particleList)
            if (particle.getId().equals(JUPITER_ID))
                return particle;
        return null;
    }

    Particle getShip(){
        for (Particle particle : particleList)
            if (particle.getId().equals(SHIP_ID))
                return particle;
        return null;
    }

    Particle getSun(){
        for (Particle particle : particleList)
            if (particle.getId().equals(SUN_ID))
                return particle;
        return null;
    }
}
