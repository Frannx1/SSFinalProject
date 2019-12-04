package ar.edu.itba.ss.Pedestrian.Human;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.HumanHeuristic;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;

public class Human extends Pedestrian {
    /**
     * This class represents a human pedestrian
     * it implements de contractile entities model
     * it's main objective is to get to the goal without transforming into a zombie!
     */

    private HumanHeuristic heuristic;

    private boolean infected;

    public Human(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(number, x, y, velocity, angle, mass, radius);
        infected = false;
    }

    public Human(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
        infected = false;
    }

    public Human(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
        infected = false;
    }

    public Human(int number, double x, double y, double maxDisplacementMaginutd, double scapeMagnitud,
                 double maxRadius, double minRadius, double mass, double beta, double visualField) {
        super(number, x, y, maxDisplacementMaginutd, scapeMagnitud, maxRadius, minRadius, mass, beta, visualField);
        infected = false;
    }

    public Human(Human other) {
        super(other);
        this.heuristic = other.heuristic;
        this.infected = other.infected;
    }

    public Zombie transform() {
        //TODO: Ver si hay  algo del estado del zombie que cambiar o guardar
        infected = true;
        return new Zombie(this);
    }

    @Override
    public Pedestrian updatePosition(double deltaT, Environment environment) {
        // el cambio de posicion viene dado por la suma vectorial
        // new position = old position + velocidad * delta T
        // el update debe ser en base a las condiciones del ambiente

        Vector2 velocityComponent = getVelocity().clone();
        velocityComponent.multiply(deltaT);
        Vector2 newPosition = getCoordinate();
        newPosition.add(velocityComponent);

        newPosition = (Vector2) environment.validatePosition(newPosition, this);
        setCoordinate(newPosition);
        return new Human(this);
    }

    @Override
    public Pedestrian updateVelocity(double deltaT, Environment environment) {
        updateVelocitySate(deltaT, environment);
        return new Human(this);
    }

    @Override
    public AVector getDirectionToTarget(Environment environment) {
        return this.heuristic.directionToTargetFrom(this, environment);
    }

    @Override
    public Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = (HumanHeuristic) heuristic;
    }

    @Deprecated
    public boolean wasBitten(List<Entity> neighbours) {
        for(Entity p : neighbours) {
            boolean contact = NeighbourFinderImpl.inContact(this, p);

            if (contact && p instanceof Zombie )
                return true;
        }
        return false;
    }

    public void bite() {
        infected = true;
    }

    public boolean wasBitten() {
        return infected;
    }

    public boolean hasArrived(Environment environment) {
        return environment.getFinalGoal().distance(getCoordinate()) <= getRadius();
    }

    @Override
    public String toString() {
        return getNumber() + " " + getCoordinate().x + " " +
                getCoordinate().y + " " + getVelocity().x + " " +
                getVelocity().y + " " +getRadius() + " " + getMass() + " true";
    }

}
