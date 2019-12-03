package ar.edu.itba.ss.Pedestrian;

public class Wall extends Entity {
    public Wall(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(x, y, number, velocity, angle, mass, radius);
    }

    public Wall(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
    }

    public Wall(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
    }

    public Wall(Entity other) {
        super(other);
    }

    public Wall(int number, double x, double y, double maxDisplacementMaginutd, double escapeMagnitud, double maxRadius, double minRadius, double mass, double beta) {
        super(number, x, y, maxDisplacementMaginutd, escapeMagnitud, maxRadius, minRadius, mass, beta);
    }

}
