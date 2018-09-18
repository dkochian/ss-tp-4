package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

public class Spaceship extends Particle {

    private double height;

    public Spaceship(int id, Point<Double> position, Point<Double> velocity, Point<Double> acceleration, double mass,
                     double radius, double height) {
        super(id, position, velocity, acceleration, mass, radius);

        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
