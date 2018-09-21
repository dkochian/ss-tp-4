package ar.edu.itba.ss.entities;

import java.util.List;

public class InputData {

    private List<Particle> planets;

    public InputData(final List<Particle> planets) {
        this.planets = planets;
    }

    public List<Particle> getPlanets() {
        return planets;
    }
}
