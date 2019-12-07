package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.Comparator;
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

        double m = 1;
        if (direction.magnitude() > 6)
            m *= 0.9;
        if (direction.magnitude() > 5)
            m *= 0.9;
        if (direction.magnitude() < 5)
            m *= 1.1;
        if (direction.magnitude() < 2.5)
            m *= 1.1;
        if (direction.magnitude() < 2)
            m *= 1.1;
        if (direction.magnitude() < 1.5)
            m *= 1.1;

        direction.multiply(1.5 * Math.exp(m));

        List<Vector2> zombiesDistanceVector = getAllZombiesVector(human, environment);
        zombiesDistanceVector.sort(Comparator.comparingInt(o -> (int) o.magnitude()));

        List<Vector2> wallDistanceVector = environment.getDirectionsToWall(human);

        wallDistanceVector.forEach(wallVector -> {
            wallVector.multiply(1 / Math.pow(wallVector.magnitude(), 1));
            direction.sub(wallVector);
        });

        zombiesDistanceVector.stream().limit(5).forEach(zombieVector -> {
            zombieVector.multiply(5  / Math.pow(zombieVector.magnitude(), 2));
            direction.sub(zombieVector);
        });
        return direction.toNormal();
    }

}
