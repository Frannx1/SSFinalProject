package ar.edu.itba.ss.Pedestrian;

import mikera.vectorz.AVector;

public class Zombie extends Pedestrian {

    public Zombie infect(Human human) {
        return human.transform();
    }

    @Override
    public Pedestrian updatePosition() {
        return null;
    }

    @Override
    public AVector getTarget() {
        return null;
    }
}
