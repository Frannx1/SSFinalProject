package ar.edu.itba.ss.Pedestrian;


import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;

public abstract class Pedestrian extends Entity {

    protected double visualField;

    protected Pedestrian(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(number, x, y, velocity, angle, mass, radius);

    }

    protected Pedestrian(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
    }

    protected Pedestrian(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
    }

    protected Pedestrian(Pedestrian other) {
        super(other);
        this.visualField = other.visualField;
    }

    protected Pedestrian(int number, double x, double y, double maxDisplacementMaginutd, double scapeMagnitud,
                         double maxRadius, double minRadius, double mass, double beta, double visualField) {
        super(number, x, y, maxDisplacementMaginutd, scapeMagnitud, maxRadius, minRadius, mass, beta);
        this.visualField = visualField;
    }

    public abstract Pedestrian updatePosition(double deltaT, Environment environment);

    public abstract Pedestrian updateVelocity(double deltaT, Environment environment);

    protected void updateVelocitySate(double deltaT, Environment environment) {
        // El Contractile Particle system justamente va a modificar el valor de la velocidad
        List<Entity> neighbours = environment.getNeighbours(this);
        updateRadius(neighbours, deltaT);
        // now update velocity
        Vector2 velocity;
        if (this.getRadius() > this.getMinRadius()) {
            AVector direction = getDirectionToTarget(environment);
            direction = (direction == null)? new Vector2() : direction.toNormal();
            Vector2 targetVersor = (direction == null)? new Vector2() : (Vector2) direction;
            velocity = targetVersor;
            if (velocity == null)
                System.out.println("la ptm");
            velocity.multiply(getDisplacementMagnitud());

        } else {
            Vector2 scapeVersor = calculateScapeVersor(neighbours);
            velocity = scapeVersor;
            scapeVersor.multiply(getEscapeMagnitud());
        }

        setVelocity((Vector2) environment.validateVelocity(velocity, this));
    }

    public abstract AVector getDirectionToTarget(Environment environment);

    public abstract Heuristic getHeuristic();

    public abstract void setHeuristic(Heuristic heuristic);

    protected void updateRadius(List<Entity> neighbours, double deltaT) {
        for (Entity e : neighbours) {
            boolean contact = NeighbourFinderImpl.inContact(this, e);
            if (contact) {
                this.setRadius(this.getMinRadius());
                return;
            }
        }
        double radius = getRadius();
        radius = radius + getMaxRadius() / (THAO / deltaT);
        this.setRadius(radius);
    }

    protected Vector2 calculateScapeVersor(List<Entity> neighbours) {
        Vector2 scapeDirection = new Vector2();
        for (Entity n : neighbours) {
            boolean contact = NeighbourFinderImpl.inContact(this, n);
            if (contact) {
                // the mother fucker who wrote this decided to return null if the vector to normalize is zero.
                Vector2 vec = (Vector2) this.getCoordinate().addCopy(n.getCoordinate().multiplyCopy(-1)).toNormal();
                if(vec != null)
                    scapeDirection.add(vec);
            }
        }
        return scapeDirection;
    }

    protected double getVisualField() {
        return visualField;
    }

    protected void setVisualField(double visualField) {
        this.visualField = visualField;
    }
}

