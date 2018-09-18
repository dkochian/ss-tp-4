package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.State;
import ar.edu.itba.ss.managers.ParticleManager;

import java.util.*;

public abstract class Schema {

    final ParticleManager particleManager;

    final Map<Particle, State> states = new HashMap<>();

    Schema(final ParticleManager particleManager) {
        this.particleManager = particleManager;

        for (Particle particle: particleManager.getParticleList())
            states.put(particle, new State(particle.getPosition(), particle.getVelocity()));
    }

    /**
     * This function will update particles position and velocity
     */
    public abstract double updateParticles();

}
