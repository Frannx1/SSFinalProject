package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.Comparator;
import java.util.List;


public class CustomHeuristic extends HumanHeuristic {

    /**
     * In this implementation, we play with what could be consider an electromagnetic force.
     * The Idea is, the goal applies a constant force twards itself wich makes every human move forward.
     * But zombies (as electrons of same charge) lay an magnetic field which repels human in the y direction.
     */

    private static String goalFormula = "x";
    private static String wallFormula = "x";
    private static String zombieFormula = "x";

    private static int zombieLimit = 5;

    public static void setGoalFormula(String goalFormula) {
        CustomHeuristic.goalFormula = goalFormula;
    }

    public static void setWallFormula(String wallFormula) {
        CustomHeuristic.wallFormula = wallFormula;
    }

    public static void setZombieFormula(String zombieFormula) {
        CustomHeuristic.zombieFormula = zombieFormula;
    }

    public static void setZombieLimit(int zombieLimit) {
        CustomHeuristic.zombieLimit = zombieLimit;
    }
    @Override
    public AVector directionToTargetFrom(Human human, Environment environment) {
        Vector2 direction = getDistanceToGoal(human, environment);
        double mGoal;

        mGoal = new Expression(replaceInFormula(goalFormula, human, direction.magnitude())).calculate();
        direction.multiply(mGoal);

        List<Vector2> zombiesDistanceVector = getAllZombiesVector(human, environment);
        zombiesDistanceVector.sort(Comparator.comparingInt(o -> (int) o.magnitude()));

        List<Vector2> wallDistanceVector = environment.getDirectionsToWall(human);

        wallDistanceVector.forEach(wallVector -> {
            wallVector.multiply(new Expression(replaceInFormula(wallFormula, human, wallVector.magnitude())).calculate());
            direction.sub(wallVector);
        });

        zombiesDistanceVector.stream().limit(zombieLimit).forEach(zombieVector -> {
            zombieVector.multiply(new Expression(replaceInFormula(zombieFormula, human, zombieVector.magnitude())).calculate());
            direction.sub(zombieVector);
        });
        return direction.toNormal();
    }

    public static String replaceInFormula(String formula, Human human, double distance) {
        return formula.replace("X", String.valueOf(human.getCoordinate().x))
                .replace("Y", String.valueOf(human.getCoordinate().y))
                .replace("Vx", String.valueOf(human.getVelocity().x))
                .replace("Vy", String.valueOf(human.getVelocity().x))
                .replace("R", String.valueOf(human.getRadius()))
                .replace("D", String.valueOf(distance));
    }

}
