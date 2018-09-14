package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

public class Particle {

    private final Integer id;

    private final Double mass;

    private final Double radius;

    private Point<Double> position;

    private Point<Double> velocity;

    private Point<Double> acceleration;

    public Particle(int id, Point<Double> position, Point<Double> velocity,
                    Point<Double> acceleration, double mass, double radius) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        this.radius = radius;
    }

    public static Point<Double> calculateAcceleration(final Particle particle, final Point<Double> forces) {
        return new Point<>(forces.getX() / particle.mass, forces.getY() / particle.mass);
    }

    public void update(final Point<Double> forces) {
        acceleration = calculateAcceleration(this, forces);
    }

    public double getDistance(final Particle p) {
        return getDistance(position, p.getPosition());
    }

    public static double getDistance(final Point<Double> pos1, final Point<Double> pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2)
                + Math.pow(pos1.getY() - pos2.getY(), 2));
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Particle)) return false;
        final Particle p = (Particle) obj;

        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        int result = 11;

        result = 19 * result + id.hashCode();
        result = 19 * result + position.hashCode();
        result = 19 * result + velocity.hashCode();
        result = 19 * result + acceleration.hashCode();
        result = 19 * result + mass.hashCode();
        result = 19 * result + radius.hashCode();

        return result;
    }

    public Point<Double> getPosition() {
        return position;
    }

    public Point<Double> getVelocity() {
        return velocity;
    }

    public Integer getId() {
        return id;
    }

    public Double getMass() {
        return mass;
    }

    public Double getRadius() {
        return radius;
    }

    public Point<Double> getAcceleration() {
        return acceleration;
    }

    public void setVelocity(final Point<Double> velocity) {
        this.velocity = velocity;
    }

    public void setPosition(final Point<Double> position) {
        this.position = position;
    }

    public void setAcceleration(final Point<Double> acceleration) {
        this.acceleration = acceleration;
    }

}