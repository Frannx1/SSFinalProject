package ar.edu.itba.ss.Pedestrian;

import mikera.vectorz.Vector2;

public abstract class Entity {
    private double mass;
    private int number;
    private double radius;
    private Vector2 coordinate;
    private Vector2 velocity;
    private boolean scape;
    private Vector2 desireDirection;

    // TODO: this should not exist
    public static  double MAX_RADIUS = 0.37;
    public static  double MIN_RADIUS = 0.10;
    public static final double MASS = 0.1;
    public static final double MAX_SPEED = 0.95;
    public static final double SCAPE_SPEED = 0.95;

    protected Entity(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.desireDirection = new Vector2(Math.cos(angle) * velocity, Math.sin(angle) * velocity);
        this.scape = false;
        this.velocity = new Vector2();
        this.radius = radius;
    }

    protected Entity(int number, double x, double y, double vx, double vy, double mass) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.velocity = new Vector2();
        this.desireDirection = new Vector2(vx, vy).toNormal();
    }

    protected Entity(int number, double x, double y, double vx, double vy, double mass, double radius) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.velocity = new Vector2(vx, vy);
        this.radius = radius;
        this.desireDirection = this.velocity.toNormal();
    }

    protected Entity(Entity other) {
        this.number = other.number;
        this.mass = other.mass;
        this.coordinate = other.coordinate;
        this.desireDirection = other.desireDirection;
        this.scape = other.scape;
        this.velocity = other.velocity;
        this.radius = other.radius;
    }

    public void setDesireDirection(Vector2 direction) {
        this.desireDirection = direction;
    }

    public Vector2 getDesireDirection() {
        return this.desireDirection;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getMass() {
        return this.mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        if (radius >= MAX_RADIUS)
            this.radius = MAX_RADIUS;
        else
            this.radius = radius;
    }

    public Vector2 getCoordinate() {
        return this.coordinate;
    }

    public void setCoordinate( Vector2 coordinate) {
        this.coordinate = coordinate;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void resetVelocity() {
        this.velocity = this.desireDirection;
        this.scape = true;
    }

    public boolean isScape() {
        return this.scape;
    }

    public void setScape(boolean scape) {
        this.scape = scape;
    }

    @Override
    public String toString() {
        return coordinate.x + " " + coordinate.y + " " + velocity.x + " " + velocity.y + " " + radius;
    }

    public double getDistanceTo(Entity entity) {
        return this.coordinate.distance(entity.coordinate);
    }

    public Vector2 getDirectionTo(Entity entity) {
        Vector2 resp = this.coordinate.clone();
        resp.sub(entity.coordinate);
        return resp;
    }

    public double getDistanceTo(Vector2 other) {
        return Math.max(0, this.coordinate.distance(other));
    }

    public double getVelocityModule() {
        return this.velocity.magnitude();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        return number == other.getNumber();
    }

}
