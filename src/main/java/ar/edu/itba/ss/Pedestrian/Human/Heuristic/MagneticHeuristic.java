package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;


public class MagneticHeuristic extends HumanHeuristic {
    /**
     * In this implementation, we play with what could be consider an electromagnetic force.
     * The Idea is, the goal applies a constant force twards itself wich makes every human move forward.
     * But zombies (as electrons of same charge) lay an magnetic field which repels human in the y direction.
     */

    @Override
    public AVector directionToTargetFrom(Human human, Environment environment) {
        Vector2 distanceToGoal = getDistanceToGoal(human, environment);
        distanceToGoal.multiply(Math.exp(distanceToGoal.magnitude()));

        List<Vector2> zombies = getAllZombiesVector(human, environment);

        zombies.stream().forEach(zombie -> {
            zombie.multiply(Math.exp(- 3 * zombie.magnitude()));
            distanceToGoal.sub(zombie);
        });
        return distanceToGoal.toNormal();
    }

}
