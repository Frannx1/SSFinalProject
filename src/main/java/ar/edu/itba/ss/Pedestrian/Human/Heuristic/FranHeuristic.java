package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;


public class FranHeuristic extends HumanHeuristic {
    /**
     * In this implementation, we play with what could be consider an electromagnetic force.
     * The Idea is, the goal applies a constant force twards itself wich makes every human move forward.
     * But zombies (as electrons of same charge) lay an magnetic field which repels human in the y direction.
     */

    @Override
    public AVector directionToTargetFrom(Human human, Environment environment) {
        Vector2 direction = getDistanceToGoal(human, environment);
        direction.multiply(1.5 * Math.exp(direction.magnitude()));

        List<Vector2> zombiesDistanceVector = getAllZombiesVector(human, environment);



        zombiesDistanceVector.forEach(zombieVector -> {
            zombieVector.multiply(300 * 1 / Math.pow(zombieVector.magnitude(), 2));
            direction.sub(zombieVector);
        });
        return direction.toNormal();
    }

}
