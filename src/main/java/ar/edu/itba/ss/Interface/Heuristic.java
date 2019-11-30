package ar.edu.itba.ss.Interface;

import ar.edu.itba.ss.Pedestrian.Entity;
import mikera.vectorz.AVector;

public interface Heuristic<T extends Entity> {

    AVector directionToTargetFrom(T pedestrian);

}
