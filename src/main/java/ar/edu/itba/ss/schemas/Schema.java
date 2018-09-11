package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.utils.other.Point;

public abstract class Schema {

    final ParticleManager particleManager;

    Schema(final ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

    /**
     * This function will update particles position and velocity
     */
    public abstract Point<Double> updateParticle(Particle p);


}
