package ar.edu.itba.ss.entities;

import java.util.List;

public class InputData {

    private List<Particle> planets;

    private final Particle spaceShip;

    public InputData(final List<Particle> planets, final Particle spaceShip) {
        this.planets = planets;
        this.spaceShip = spaceShip;
    }

    public List<Particle> getPlanets() {
        return planets;
    }

    public Particle getSpaceShip() {
        return spaceShip;
    }
}
