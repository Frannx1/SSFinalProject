package ar.edu.itba.ss.Pedestrian.Human;

import ar.edu.itba.ss.Interface.Heuristic;
import mikera.vectorz.AVector;

public abstract class HumanHeuristic implements Heuristic<Human> {

    @Override
    public abstract AVector directionToTargetFrom(Human pedestrian);

}

