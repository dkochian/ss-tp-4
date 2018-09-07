package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;

public class Verlet implements Schema{

    private final Oscillator oscillator;

    public Verlet(Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    @Override
    public void updateParticle() {

    }
}
