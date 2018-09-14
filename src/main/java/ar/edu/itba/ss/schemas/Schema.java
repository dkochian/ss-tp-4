package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.State;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;

import java.util.*;

public abstract class Schema {

    final IOManager ioManager;
    final ParticleManager particleManager;

    final Map<Particle, State> states = new HashMap<>();

    Schema(final IOManager ioManager, final ParticleManager particleManager) {
        this.ioManager = ioManager;
        this.particleManager = particleManager;

        for (Particle particle: particleManager.getParticleList())
            states.put(particle, new State(particle.getPosition(), particle.getVelocity()));
    }

    /**
     * This function will update particles position and velocity
     */
    public abstract void updateParticles();

}
