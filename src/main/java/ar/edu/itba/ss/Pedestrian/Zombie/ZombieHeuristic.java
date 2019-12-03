package ar.edu.itba.ss.Pedestrian.Zombie;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.Random;

public class ZombieHeuristic implements Heuristic<Zombie> {

    //TODO: esto va a depender del delta_t, podemos llevarlo a zombie.
    private static long maxDurations = 40;
    private long duration = 0;
    private Vector2 randomMove;

    @Override
    public AVector directionToTargetFrom(Zombie zombie, Environment environment) {
        if (zombie.getTarget().isPresent())
            return zombie.getDirectionTo(zombie.getTarget().get());
        else
            return randomMoveWithDuration();
    }

    private AVector randomMoveWithDuration() {
        //TODO: ver si choco pared?
        if (duration % maxDurations == 0) {
            duration++;
            randomMove = new Vector2(Math.random() * Math.pow(-1, new Random().nextInt()),
                    Math.random()* Math.pow(-1, new Random().nextInt()));
        }

        return randomMove;
    }

}
