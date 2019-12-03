package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;


public class Simple extends HumanHeuristic {
    /**
     * This implementation was meant for testing purposes only
     */


    @Override
    public AVector directionToTargetFrom(Human human, Environment environment) {
        Vector2 resp = (Vector2) environment.getFinalGoal().clone();
        resp.sub(human.getCoordinate().clone());
        return resp.toNormal();
    }
}
