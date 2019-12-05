package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.Comparator;

public class ExponentialHeuristic extends HumanHeuristic {

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

    private Vector2 getDistanceToDanger(Human human, Environment<Pedestrian> environment) {
       return environment.getEnvironmentState().getMemebers().stream()
                .filter(pedestrian -> pedestrian instanceof Zombie)
                .map(zombie -> {
                    Vector2 vec = human.getDirectionTo(zombie);
                    double factor = human.getDistanceTo(zombie.getCoordinate());
                    vec.multiply(factor);
                    return vec;
                }).min(Comparator.comparingDouble(vec -> vec.magnitude()))
               .orElse(new Vector2());
    }

    private Vector2 getDistanceToGoal(Human human, Environment environment) {
        Vector2 distanceToGoal = (Vector2) environment.getFinalGoal().clone();
        distanceToGoal.sub(human.getCoordinate());
        distanceToGoal.add(-1 * human.getRadius());
        return distanceToGoal;
    }
}
