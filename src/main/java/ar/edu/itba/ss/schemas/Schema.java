package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;

public abstract class Schema {

    private final Oscillator oscillator;

    Schema(Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    Oscillator getOscillator() {
        return oscillator;
    }

    public Particle getParticle(){
        return oscillator.getParticle();
    }

    /**
     * This function will update particles position and velocity
     */
    public abstract Point<Double> updateParticle();
}
