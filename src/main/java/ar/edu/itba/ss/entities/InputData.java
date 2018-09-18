package ar.edu.itba.ss.entities;

import java.util.List;

public class InputData {

    private List<Particle> planets;

    private final Spaceship spaceship;

    public InputData(final List<Particle> planets, final Spaceship spaceship) {
        this.planets = planets;
        this.spaceship = spaceship;
    }

    public List<Particle> getPlanets() {
        return planets;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }
}
