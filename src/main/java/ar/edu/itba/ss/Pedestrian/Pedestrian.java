package ar.edu.itba.ss.Pedestrian;


import ar.edu.itba.ss.Interface.Heuristic;
import mikera.vectorz.AVector;

public abstract class Pedestrian extends Entity {

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
    }


    public abstract Pedestrian updatePosition();

    public abstract AVector getDirectionToTarget();

    public abstract Heuristic getHeuristic();

    public abstract void setHeuristic(Heuristic heuristic);

}

