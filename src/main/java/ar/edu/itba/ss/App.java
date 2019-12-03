package ar.edu.itba.ss;

import ar.edu.itba.ss.Environment.EnvironmentImpl;
import ar.edu.itba.ss.Environment.StateImpl;
import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.Simple;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import ar.edu.itba.ss.Pedestrian.Zombie.ZombieHeuristic;
import mikera.vectorz.Vector2;

import java.util.ArrayList;
import java.util.List;

public class App {
    static double width = 15;
    static double height = 15;
    static double simulationTime = 50;
    static double deltaT = 0.1;
    static double beta = 0.9;
    static double maxDisplacementVelocity = 1.5;
    static double escapeMagnitud = 1.5;
    static double zombieDisplacementMagnitud = 0.5;
    static double scapeCenter = height /2;
    static double entranceCenter = scapeCenter;
    static double mass = 58;
    static double visualField = 3;
    static double minRadius = 0.1;
    static double maxRadius = 0.3;

    // first we create the pedestrians
    static int humanPopulation = 1;
    static int zombiePopulation = 1;

    public static void main(String[] args) {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, scapeCenter,
                entranceCenter, null);

        List<Pedestrian> pedestrians = new ArrayList<>();
        addHumans(pedestrians, environment, humanPopulation);
        addZombies(pedestrians, environment, zombiePopulation);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, simulationTime);
        System.out.println("starting engine");
        engine.simulate();
        System.out.println("simulation finished!");
    }

    private static void addHumans(List<Pedestrian> pedestrians, Environment environment, int size) {
        for (int i = 0; i < size; i++) {
            Human human = new Human(pedestrians.size(), ((Vector2)environment.getStartingPoint()).x,
                    ((Vector2)environment.getStartingPoint()).y, zombieDisplacementMagnitud, escapeMagnitud, maxRadius,
                    minRadius, mass, beta, visualField);
            human.setHeuristic(new Simple());
            pedestrians.add(human);
        }

    }

    private static void addZombies(List<Pedestrian> pedestrians, Environment environment, int size) {
        for (int i = 0; i < size; i++) {
            Zombie zombie = new Zombie(pedestrians.size(), ((Vector2)environment.getFinalGoal()).x,
                    ((Vector2)environment.getFinalGoal()).y, maxDisplacementVelocity, escapeMagnitud, maxRadius,
                    minRadius, mass, beta, visualField);
            zombie.setHeuristic(new ZombieHeuristic());
            pedestrians.add(zombie);
        }
    }
}
