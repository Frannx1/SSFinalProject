package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class HumanHeuristic implements Heuristic<Human> {

    @Override
    public abstract AVector directionToTargetFrom(Human human, Environment environment);


    protected Vector2 getDistanceToDanger(Human human, Environment<Pedestrian> environment) {
        return getAllZombiesVector(human, environment).stream().min(Comparator.comparingDouble(vec -> vec.magnitude()))
                .orElse(new Vector2());
    }

    protected Vector2 getDistanceToGoal(Human human, Environment environment) {
        Vector2 distanceToGoal = (Vector2) environment.getFinalGoal().clone();
        distanceToGoal.sub(human.getCoordinate());
        distanceToGoal.add(-1 * human.getRadius());
        return distanceToGoal;
    }


    protected List<Vector2> getAllZombiesVector(Human human, Environment<Pedestrian> environment) {
        return environment.getEnvironmentState().getMembers().stream().filter(pedestrian -> pedestrian instanceof Zombie)
                .map(zombie ->{
                    Vector2 vec = human.getDirectionTo(zombie);
                    double factor = human.getDistanceTo(zombie.getCoordinate()) - human.getRadius() - zombie.getRadius();
                    vec.multiply(factor);
                    return vec;
                }).collect(Collectors.toList());
    }
}

