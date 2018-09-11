package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Rounding;

import java.math.BigDecimal;

public class Particle {

    private final Integer id;

    private Point<BigDecimal> position;

    private BigDecimal speed;

    private BigDecimal angle;

    private final Double mass;

    private final BigDecimal radius;

    public Particle(int id, BigDecimal x, BigDecimal y, BigDecimal speed, BigDecimal angle, Double mass,
                    BigDecimal radius) {
        position = new Point<>(x, y);

        this.id = id;
        this.speed = speed;
        this.angle = angle;
        this.mass = mass;
        this.radius = radius;
    }

    public Point<Double> getVelocity() {
        return new Point<>(speed.multiply(new BigDecimal(Math.cos(angle.doubleValue()))).doubleValue(),
                speed.multiply(new BigDecimal(Math.sin(angle.doubleValue()))).doubleValue());
    }

    public void updateVelocity(final Point<Double> velocity) {
        speed = new BigDecimal(Math.sqrt(Math.pow(velocity.getX(), 2) + Math.pow(velocity.getY(), 2)))
                .setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP);
        angle = new BigDecimal(Math.atan2(velocity.getY(), velocity.getX()))
                .setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP);
    }

    public void updatePosition(final Point<Double> position) {
        this.position = new Point<>(new BigDecimal(position.getX()), new BigDecimal(position.getY()));
    }

    public void update(final double t) {
        //Update position
        BigDecimal newX = position.getX().add(speed.multiply(new BigDecimal(Math.cos(angle.doubleValue())))
                .multiply(new BigDecimal(t))).setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP);
        BigDecimal newY = position.getY().add(speed.multiply(new BigDecimal(Math.sin(angle.doubleValue())))
                .multiply(new BigDecimal(t))).setScale(Rounding.SCALE, Rounding.ROUNDING_MODE_UP);

        position = new Point<>(newX, newY);
    }

    public double getDistance(final Particle p) {
        return Math.sqrt(Math.pow(position.getX().subtract(p.position.getX()).doubleValue(), 2)
                + Math.pow(position.getY().subtract(p.position.getY()).doubleValue(), 2));
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

        return id.equals(p.id) && position.equals(p.position) && speed.equals(p.speed) && angle.equals(p.angle)
                && mass.equals(p.mass);
    }

    @Override
    public int hashCode() {
        int result = 11;

        result = 19 * result + id.hashCode();
        result = 19 * result + position.hashCode();
        result = 19 * result + speed.hashCode();
        result = 19 * result + angle.hashCode();
        result = 19 * result + mass.hashCode();

        return result;
    }

    public Point<BigDecimal> getPosition() {
        return position;
    }

    public Point<Double> getDoublePosition() {
        return new Point<>(position.getX().doubleValue(), position.getY().doubleValue());
    }

    public BigDecimal getAngle() {
        return angle;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public Integer getId() {
        return id;
    }

    public Double getMass() {
        return mass;
    }

    public BigDecimal getRadius() {
        return radius;
    }

}
