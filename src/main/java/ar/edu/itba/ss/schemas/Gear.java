package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;

public class Gear implements Schema{

    private final Oscillator oscillator;

    public Gear(Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    @Override
    public void updateParticle() {
        
    }
}
