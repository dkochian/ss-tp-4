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

    private static final int SPACESHIP_ID = 0;
    private static final int SUN_ID = 1;
    private static final int EARTH_ID = 2;
    private static final int SATURN_ID = 3;
    private static final int JUPITER_ID = 4;

    public boolean addParticle(final Particle p) {
        return particleList.add(p);
    }

    public List<Particle> getParticleList() {
        return Collections.unmodifiableList(particleList);
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

    Particle getSpaceShip(){
        for (Particle particle : particleList)
            if (particle.getId().equals(SPACESHIP_ID))
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
