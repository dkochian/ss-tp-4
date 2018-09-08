package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.Oscillator;
import ar.edu.itba.ss.utils.other.Point;

public class Gear extends Schema {

    public Gear(final Oscillator oscillator) {
        super(oscillator);
    }

    @Override
    public Point<Double> updateParticle() {
        return null;
    }
}
