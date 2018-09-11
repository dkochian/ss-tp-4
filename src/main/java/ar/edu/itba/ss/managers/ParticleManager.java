package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleManager {

    private final List<Particle> particleList = new ArrayList<>();

    public boolean addParticle(final Particle p) {
        return particleList.add(p);
    }

    public boolean removeParticle(final Particle p) {
        return particleList.remove(p);
    }

    public List<Particle> getParticleList() {
        return Collections.unmodifiableList(particleList);
    }

}
