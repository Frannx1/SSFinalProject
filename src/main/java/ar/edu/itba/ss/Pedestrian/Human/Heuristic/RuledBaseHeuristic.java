package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

public class RuledBaseHeuristic extends HumanHeuristic {

    @Override
    public AVector directionToTargetFrom(Human human, Environment environment) {
        Vector2 distanceToGoal = getDistanceToGoal(human, environment);
        Vector2 distanceToDanger = getDistanceToDanger(human, environment);
        if(distanceToDanger.magnitude() < distanceToGoal.magnitude() && distanceToGoal.magnitude() > human.getRadius()) {
            distanceToDanger.multiply(-1);
            distanceToGoal.multiply(0.3);
            distanceToDanger.add(distanceToGoal);
            distanceToDanger = distanceToDanger.toNormal();
            return distanceToDanger == null? new Vector2() : distanceToDanger;
        }
        distanceToGoal = distanceToGoal.toNormal();
        return distanceToGoal == null? new Vector2() : distanceToGoal;
    }


}
