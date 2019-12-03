package ar.edu.itba.ss.Pedestrian.Zombie;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;
import java.util.Optional;

public class Zombie extends Pedestrian {

    private ZombieHeuristic heuristic;
    private Optional<Human> target;


    public Zombie(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(number, x, y, velocity, angle, mass, radius);
    }

    public Zombie(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
        target = Optional.empty();
    }

    public Zombie(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
        target = Optional.empty();
    }

    public Zombie(int number, double x, double y, double maxDisplacementMaginutd, double scapeMagnitud,
                 double maxRadius, double minRadius, double mass, double beta, double visualField) {
        super(number, x, y, maxDisplacementMaginutd, scapeMagnitud, maxRadius, minRadius, mass, beta, visualField);
        target = Optional.empty();
    }

    public Zombie(Pedestrian other) {
        super(other);
        this.heuristic = new ZombieHeuristic();
        this.target = Optional.empty();
    }

    public Zombie(Zombie other) {
        super(other);
        this.heuristic = other.heuristic;
        this.target = other.target;
    }

    @Override
    public Pedestrian updatePosition(double deltaT, Environment environment) {
        // el cambio de posicion viene dado por la suma vectorial
        // new position = old position + velocidad * delta T
        if(getVelocity() == null)
            System.out.println("aca");
        Vector2 velocityComponent = getVelocity().clone();
        velocityComponent.multiply(deltaT);
        Vector2 newPosition = getCoordinate();
        newPosition.add(velocityComponent);

        newPosition = (Vector2) environment.validatePosition(newPosition, this);
        setCoordinate(newPosition);
        checkForHuman(this, environment);
        return new Zombie(this);
    }

    @Override
    public Pedestrian updateVelocity(double deltaT, Environment environment) {
        updateVelocitySate(deltaT, environment);
        return new Zombie(this);
    }


    @Override
    public AVector getDirectionToTarget(Environment environment) {
        return this.heuristic.directionToTargetFrom(this, environment);
    }

    public Zombie infect(Human human) {
        //TODO: notar que el humano sigue existiendo.
        return human.transform();
    }

    @Override
    public Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = (ZombieHeuristic) heuristic;
    }

    public Optional<Human> getTarget() {
        return target;
    }

    public void setTarget(Human target) {
        this.target = Optional.ofNullable(target);
    }

    private void checkForHuman(Zombie zombie, Environment environment) {

        List<Entity> neighbours = environment.getNeighbours(this);
        for(Entity e : neighbours) {
            boolean visible = NeighbourFinderImpl.isNear(this, e, visualField);
            if (visible && ( e instanceof Human)) {
                setTarget((Human)e);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return getNumber() + " " + getCoordinate().x + " " +
                getCoordinate().y + " " + getVelocity().x + " " +
                getVelocity().y + " " + getRadius() + " " + getMass() + " false";
    }
}

