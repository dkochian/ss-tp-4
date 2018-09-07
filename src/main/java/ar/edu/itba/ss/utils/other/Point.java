package ar.edu.itba.ss.utils.other;

public class Point<T extends Number> {

    private final T x;
    private final T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point {x=" + x + ", y=" + y + "}";
    }
}