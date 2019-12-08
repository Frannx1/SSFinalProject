package ar.edu.itba.ss;

import ar.edu.itba.ss.Environment.EnvironmentImpl;
import ar.edu.itba.ss.Environment.StateImpl;
import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.FranHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.HumanHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.MagneticHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import ar.edu.itba.ss.Pedestrian.Zombie.ZombieHeuristic;
import mikera.vectorz.Vector2;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class App {
    static double width = 15;
    static double height = 15;
    static double simulationTime = 50;
    static double deltaT = 0.1;
    static double entranceFrequency = 0.6;
    static double beta = 0.9;
    static double maxDisplacementVelocity = 1.5;
    static double escapeMagnitud = 5;
    static double zombieDisplacementMagnitud = 0.80;
    static double scapeCenter = height /2;
    static double entranceCenter = scapeCenter;
    static double mass = 58;
    static double visualField = 10;
    static double minRadius = 0.1;
    static double maxRadius = 0.11;
    static double goalRadius = 0.5;
    static double entranceRadius = 5;

    // for vector field
    static double separation = 0.5;
    static int zombieRate = 20;

    // first we create the pedestrians
    static int humanPopulation = 50;
    static int zombiePopulation = 10;

    public static void main(String[] args) {
        normal();
    }

    public static void normal() {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, scapeCenter,
                entranceCenter, goalRadius, null, entranceRadius);

        Set<Pedestrian> pedestrians = new HashSet<>();
        addZombies(pedestrians, environment, zombiePopulation);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, simulationTime, entranceFrequency);
        addHumans(environment, humanPopulation, engine.getHumanQueue(), pedestrians.size());

        System.out.println("starting engine");
        engine.simulate();
        System.out.println("simulation finished!");
    }

    public static void vectorField() {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, scapeCenter,
                entranceCenter, goalRadius, null);

        Set<Pedestrian> pedestrians = new HashSet<>();
        addPedestriansToVectorField(pedestrians, environment, separation, zombieRate);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, 2 * deltaT);
        System.out.println("starting engine");
        engine.simulate();
        System.out.println("simulation finished!");
    }

    private static void addPedestriansToVectorField(Set<Pedestrian> pedestrians, Environment environment, double separation, int zombieRate) {
        for (double x = separation/2; x < environment.getWidth() - separation/2; x += separation) {
            for (double y = separation/2; y < environment.getWidth() - separation/2; y += separation) {
                Pedestrian pedestrian;
                Heuristic heuristic;
                if (pedestrians.size() % zombieRate == 0 && Math.random() > 0.5) {
                    pedestrian = new Zombie(pedestrians.size(), x, y, zombieDisplacementMagnitud, escapeMagnitud, maxRadius,
                            minRadius, mass, beta, visualField);
                    heuristic = new ZombieHeuristic();
                } else {
                    pedestrian = new Human(pedestrians.size(), x, y, maxDisplacementVelocity , escapeMagnitud, maxRadius,
                            minRadius, mass, beta, visualField);
                    //TODO: factory
                    heuristic = new FranHeuristic();
                }
                pedestrian.setHeuristic(heuristic);
                pedestrians.add(pedestrian);
            }
        }
    }

    private static void addHumans(Environment environment, int size, Queue<Human> humanQueue, int index) {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + " humans created");
            double yPosition = (Math.random() * environment.getEntranceRadius()) +
                    (((Vector2) environment.getStartingPoint()).y - environment.getEntranceRadius()/2);
            Human human = new Human(index++, ((Vector2) environment.getStartingPoint()).x,
                    yPosition, maxDisplacementVelocity , escapeMagnitud, maxRadius,
                    minRadius, mass, beta, visualField);
            human.setHeuristic(new FranHeuristic());
            humanQueue.offer(human);
        }

    }

    private static void addZombies(Set<Pedestrian> pedestrians, Environment environment, int size) {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + " zombies created");
            boolean inserted = false;
            while(!inserted) {
                double yPosition = Math.random() * environment.getHeight();
                Zombie zombie = new Zombie(pedestrians.size(), ((Vector2) environment.getFinalGoal()).x,
                        yPosition, zombieDisplacementMagnitud, escapeMagnitud, maxRadius,
                        minRadius, mass, beta, visualField);
                zombie.setHeuristic(new ZombieHeuristic());
                if(!isCollition(zombie, pedestrians)) {
                    pedestrians.add(zombie);
                    inserted = true;
                }
            }
        }
    }

    private static boolean isCollition(Pedestrian newPedestrian, Set<Pedestrian> currentPedestrians) {
        return currentPedestrians.stream()
                .anyMatch(pedestrian ->
                        pedestrian.getDistanceTo(newPedestrian) <= pedestrian.getRadius() + newPedestrian.getRadius());
    }
}
