package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;

import java.util.List;

public abstract class HumanHeuristic implements Heuristic<Human> {

    @Override
    public abstract AVector directionToTargetFrom(Human human, Environment environment);

}

