package ar.edu.itba.ss.Pedestrian;

import mikera.vectorz.Vector2;

public abstract class Entity {
    private double mass;
    private int number;
    private double radius;
    private Vector2 coordinate;
    private Vector2 velocity;
    private Vector2 desireDirection;
    private double maxDisplacementMaginutd;
    private double escapeMagnitud;
    private double minRadius;
    private double maxRadius;
    private double beta;
    // TODO: this should not exist
    public static  double MAX_RADIUS = 0.37;
    public static  double MIN_RADIUS = 0.10;
    public static final double MASS = 0.1;
    public static final double MAX_SPEED = 0.95;
    public static final double SCAPE_SPEED = 0.95;
    public static final double THAO = 0.5;

    @Deprecated
    protected Entity(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.desireDirection = new Vector2(Math.cos(angle) * velocity, Math.sin(angle) * velocity);
        this.velocity = new Vector2();
        this.radius = radius;
    }

    @Deprecated
    protected Entity(int number, double x, double y, double vx, double vy, double mass) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.velocity = new Vector2();
        this.desireDirection = new Vector2(vx, vy).toNormal();
    }

    @Deprecated
    protected Entity(int number, double x, double y, double vx, double vy, double mass, double radius) {
        this.number = number;
        this.mass = mass;
        this.coordinate = new Vector2(x, y);
        this.velocity = new Vector2(vx, vy);
        this.radius = radius;
        this.desireDirection = this.velocity.toNormal();
    }

    // Estas son las unicas a usar

    protected Entity(Entity other) {
        this.number = other.number;
        this.mass = other.mass;
        this.coordinate = other.coordinate.clone();
        this.radius = other.radius;
        this.minRadius = other.minRadius;
        this.maxRadius = other.maxRadius;
        this.escapeMagnitud = other.escapeMagnitud;
        this.maxDisplacementMaginutd = other.maxDisplacementMaginutd;
        this.velocity = other.velocity;
    }

    protected Entity(int number, double x, double y, double maxDisplacementMaginutd, double escapeMagnitud,
                     double maxRadius, double minRadius, double mass, double beta) {
        this.number = number;
        this.radius = maxRadius;
        this.coordinate = new Vector2(x, y);
        this.escapeMagnitud = escapeMagnitud;
        this.maxDisplacementMaginutd = maxDisplacementMaginutd;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.mass = mass;
        this.beta = beta;
        this.velocity = new Vector2();
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
    }

    @Override
    public String toString() {
        return coordinate.x + " " + coordinate.y + " " + velocity.x + " " + velocity.y + " " + radius;
    }

    public double getDistanceTo(Entity entity) {
        return this.coordinate.distance(entity.coordinate);
    }

    public Vector2 getVectorTo(Entity entity) {
        Vector2 resp = entity.coordinate.clone();
        resp.sub(this.coordinate);

        return resp;
    }

    public Vector2 getDirectionTo(Entity entity) {
        Vector2 vec = getVectorTo(entity).toNormal();
        return vec == null? new Vector2() : vec;
    }

    public double getDistanceTo(Vector2 other) {
        return Math.max(0, this.coordinate.distance(other));
    }

    public double getVelocityModule() {
        return this.velocity.magnitude();
    }

    public double getMaxDisplacementMaginutd() {
        return maxDisplacementMaginutd;
    }

    public void setMaxDisplacementMaginutd(double maxDisplacementMaginutd) {
        this.maxDisplacementMaginutd = maxDisplacementMaginutd;
    }

    public double getEscapeMagnitud() {
        return escapeMagnitud;
    }

    public void setEscapeMagnitud(double escapeMagnitud) {
        this.escapeMagnitud = escapeMagnitud;
    }

    public double getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }

    public double getDisplacementMagnitud() {
        // as seen on the paper
        return maxDisplacementMaginutd * Math.pow((radius - minRadius) / (maxRadius - minRadius), beta);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int prime2 = 47;
        int result = 1;
        result = prime * result + prime2 * number;
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
