package ar.edu.itba.ss.Pedestrian.Zombie;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        setMaxDisplacementMaginutd(this.getMaxDisplacementMaginutd());
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

    public boolean infect(Human human) {
        //TODO: notar que el humano sigue existiendo.
        setTarget(null);
        human.bite();
        return true;
    }

    @Override
    public Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = (ZombieHeuristic) heuristic;
    }

    @Override
    public boolean isSameClass(Pedestrian pedestrian) {
        return (pedestrian instanceof Zombie);
    }

    public Optional<Human> getTarget() {
        return target;
    }

    public void setTarget(Human target) {
        this.target = Optional.ofNullable(target);
    }

    private void checkForHuman(Zombie zombie, Environment environment) {
        List<Entity> neighbours = environment.getEnvironmentState().getMembers();
        neighbours = neighbours.stream().filter(entity -> entity instanceof Human)
                .filter(entity -> NeighbourFinderImpl.isNear(entity, zombie, getVisualField()))
                .sorted((o1, o2) -> ((Double)o1.getDistanceTo(zombie)).compareTo(o2.getDistanceTo(zombie)))
                .collect(Collectors.toList());

        for(Entity e : neighbours) {
            if(NeighbourFinderImpl.inContact(e, zombie) && !((Human)e).wasBitten() ) {
                e.setMaxDisplacementMaginutd(zombie.getMaxDisplacementMaginutd());
                zombie.infect((Human) e);
                return;
            }
        }
        Human human = (Human) neighbours.stream().findFirst().orElse(null);
        zombie.setTarget(human);
    }

    @Override
    public String toString() {
        return getNumber() + " " + getCoordinate().x + " " +
                getCoordinate().y + " " + getVelocity().x + " " +
                getVelocity().y + " " + getRadius() + " " + getMass() + " 255";
    }
}
