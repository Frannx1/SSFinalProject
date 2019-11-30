package ar.edu.itba.ss.Pedestrian.Human;

import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import mikera.vectorz.AVector;

public class Human extends Pedestrian {
    /**
     * This class represents a human pedestrian
     * it implements de contractile entities model
     * it's main objective is to get to the goal without transforming into a zombie!
     */

    private HumanHeuristic heuristic;

    public Human(double x, double y, int number, double velocity, double angle, double mass, double radius) {
        super(number, x, y, velocity, angle, mass, radius);
    }

    public Human(int number, double x, double y, double vx, double vy, double mass) {
        super(number, x, y, vx, vy, mass);
    }

    public Human(int number, double x, double y, double vx, double vy, double mass, double radius) {
        super(number, x, y, vx, vy, mass, radius);
    }

    public Human(Pedestrian other) {
        super(other);
    }


    public Zombie transform() {
        //TODO: Ver si hay  algo del estado del zombie que cambiar o guardar
        return new Zombie(this);
    }

    @Override
    public Human updatePosition() {
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
        this.heuristic = (HumanHeuristic) heuristic;
    }

}
