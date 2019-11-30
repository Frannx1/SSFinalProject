package ar.edu.itba.ss.Pedestrian.Zombie;

import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import mikera.vectorz.AVector;

import java.util.Optional;

public class Zombie extends Pedestrian {

    private ZombieHeuristic heuristic;
    private Optional<Human> target;

    public Zombie(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(number, x, y, velocity, angle, mass, radius);
    }

    public Zombie(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
    }

    public Zombie(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
    }

    public Zombie(Pedestrian other) {
        super(other);
    }

    public Zombie infect(Human human) {
        //TODO: notar que el humano sigue existiendo.
        return human.transform();
    }

    @Override
    public Pedestrian updatePosition() {
        return null;
    }

    @Override
    public AVector getDirectionToTarget() {
        return this.heuristic.directionToTargetFrom(this);
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
        this.target = Optional.of(target);
    }

}

