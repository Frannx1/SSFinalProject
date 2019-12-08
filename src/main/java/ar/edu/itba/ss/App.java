package ar.edu.itba.ss;

import ar.edu.itba.ss.Environment.EnvironmentImpl;
import ar.edu.itba.ss.Environment.StateImpl;
import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Interface.Message;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.FranHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import ar.edu.itba.ss.Pedestrian.Zombie.ZombieHeuristic;
import mikera.vectorz.Vector2;

import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class App {

    static double width = 15;
    static double height = 15;
    static double simulationTime = 50;
    static double deltaT = 0.1;
    static double humanEntrancePeriod = 0.6;
    static double beta = 0.9;
    static double mass = 58;
    static double exitGoalDiameter = 0.5;
    static double entranceDiameter = 5;

    static int humanPopulation = 50;
    static double humanMaxVelocity = 1.5;
    static double humanVisualField = 10;
    static double humanMinRadius = 0.1;
    static double humanMaxRadius = 0.11;
    static double humanCollisionEscapeMagnitude = 1.5;

    static int zombiePopulation = 10;
    static double zombieMaxVelocity = 0.80;
    static double zombieVisualField = 10;
    static double zombieMinRadius = 0.1;
    static double zombieMaxRadius = 0.11;
    static double zombieCollisionEscapeMagnitude = 1.5;

    // for vector field
    static double separation = 0.5;
    static int zombieRate = 20;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean exit = false;
        Message.Welcome.print();
        while(!exit) {
            exit = processCommand(in.nextLine());
        }
        in.close();
    }

    private static boolean processCommand(String s) {
        String[] args = s.split(" ");
        if( args.length == 1 && args[0].equals("help"))
            Message.Help.print();
        else if(args.length == 3 && args[0].equals("simulate") && args[1].equals("normal")) {
            normal(width, height, simulationTime, humanEntrancePeriod, humanPopulation, zombiePopulation,
                    humanMaxVelocity, humanVisualField, humanMinRadius, humanMaxRadius,
                    humanCollisionEscapeMagnitude, zombieMaxVelocity, zombieVisualField, zombieMinRadius, zombieMaxRadius,
                    zombieCollisionEscapeMagnitude, exitGoalDiameter, Boolean.parseBoolean(args[2]), "simulation.data");
        }
        else if(args.length == 9 && args[0].equals("simulate")) {
            normal(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]),
                    Double.parseDouble(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]),
                    humanMaxVelocity, humanVisualField, humanMinRadius, humanMaxRadius,
                    humanCollisionEscapeMagnitude, zombieMaxVelocity, zombieVisualField, zombieMinRadius, zombieMaxRadius,
                    zombieCollisionEscapeMagnitude, Integer.parseInt(args[7]), Boolean.parseBoolean(args[8]), "simulation.data");
        }
        else if(args.length == 21 && args[0].equals("simulate")  && args[1].equals("high")  && args[2].equals("custom")) {
            normal(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                    Double.parseDouble(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]),
                    Double.parseDouble(args[11]), Double.parseDouble(args[12]), Double.parseDouble(args[13]), Double.parseDouble(args[14]), Double.parseDouble(args[15]),
                    Double.parseDouble(args[16]), Double.parseDouble(args[17]), Double.parseDouble(args[18]), Double.parseDouble(args[19]), Double.parseDouble(args[20]),
                    Integer.parseInt(args[9]), Boolean.parseBoolean(args[10]), "simulation.data");
        }
        else if(args.length == 7 && args[0].equals("simulate")  && args[1].equals("multiple")  && args[2].equals("density")) {
            runMultipleSizeSameDensity(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Integer.parseInt(args[5]), Boolean.parseBoolean(args[6]));
        }
        else if(args.length == 1 && args[0].equals("exit"))
            return true;
        else
            Message.InvalidParams.print();
        return false;
    }

    private static void normal(double width, double height, double simulationTime, double humanEntrancePeriod,
                               int humanPopulation, int zombiePopulation, double humanMaxVelocity,
                               double humanVisualField, double humanMinRadius, double humanMaxRadius, double humanCollisionEscapeMagnitude,
                               double zombieMaxVelocity, double zombieVisualField, double zombieMinRadius, double zombieMaxRadius, double zombieCollisionEscapeMagnitude,
                               double exitGoalDiameter, boolean zombieWall, String filename) {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, height/2,
                height/2, exitGoalDiameter, null, entranceDiameter);

        Set<Pedestrian> pedestrians = new HashSet<>();
        addZombies(pedestrians, environment, zombiePopulation, zombieMaxVelocity, zombieVisualField, zombieMinRadius, zombieMaxRadius,
                zombieCollisionEscapeMagnitude, zombieWall);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, simulationTime, humanEntrancePeriod);
        addHumans(environment, humanPopulation, engine.getHumanQueue(), pedestrians.size(), humanMaxVelocity, humanVisualField, humanMinRadius, humanMaxRadius,
                humanCollisionEscapeMagnitude);

        Message.SimulationRunning.print();
        engine.simulate(filename);
        Message.SimulationEnded.print();
    }

    public static void vectorField() {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, height/2,
                height/2, exitGoalDiameter, null, entranceDiameter);

        Set<Pedestrian> pedestrians = new HashSet<>();
        addPedestriansToVectorField(pedestrians, environment, separation, zombieRate);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, 2 * deltaT, humanEntrancePeriod);
        System.out.println("starting engine");
        engine.simulate("simulation.data");
        System.out.println("simulation finished!");
    }

    private static void addPedestriansToVectorField(Set<Pedestrian> pedestrians, Environment environment, double separation, int zombieRate) {
        for (double x = separation/2; x < environment.getWidth() - separation/2; x += separation) {
            for (double y = separation/2; y < environment.getWidth() - separation/2; y += separation) {
                Pedestrian pedestrian;
                Heuristic heuristic;
                if (pedestrians.size() % zombieRate == 0 && Math.random() > 0.5) {
                    pedestrian = new Zombie(pedestrians.size(), x, y, zombieMaxVelocity, zombieCollisionEscapeMagnitude, zombieMaxRadius,
                            zombieMinRadius, mass, beta, zombieVisualField);
                    heuristic = new ZombieHeuristic();
                } else {
                    pedestrian = new Human(pedestrians.size(), x, y, humanMaxVelocity , humanCollisionEscapeMagnitude, humanMaxRadius,
                            humanMinRadius, mass, beta, humanVisualField);
                    //TODO: factory
                    heuristic = new FranHeuristic();
                }
                pedestrian.setHeuristic(heuristic);
                pedestrians.add(pedestrian);
            }
        }
    }


    private static void addHumans(Environment<Pedestrian> environment, int size, Queue<Human> humanQueue, int index,
                                  double humanMaxVelocity, double humanVisualField, double humanMinRadius,
                                  double humanMaxRadius, double humanCollisionEscapeMagnitude) {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + " humans created");
            double yPosition = (Math.random() * environment.getEntranceDiameter()) +
                    (((Vector2) environment.getStartingPoint()).y - environment.getEntranceDiameter()/2);
            Human human = new Human(index++, ((Vector2) environment.getStartingPoint()).x, yPosition, humanMaxVelocity,
                    humanCollisionEscapeMagnitude, humanMaxRadius, humanMinRadius, mass, beta, humanVisualField);
            human.setHeuristic(new FranHeuristic());
            humanQueue.offer(human);
        }

    }

    private static void addZombies(Set<Pedestrian> pedestrians, Environment<Pedestrian> environment, int size,
                                   double zombieMaxVelocity, double zombieVisualField, double zombieMinRadius,
                                   double zombieMaxRadius, double zombieCollisionEscapeMagnitude, boolean zombieWall) {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + " zombies created");
            boolean inserted = false;
            while(!inserted) {
                double xPosition = zombieWall? ((Vector2) environment.getFinalGoal()).x : Math.random() * environment.getWidth();
                double yPosition = Math.random() * environment.getHeight();
                Zombie zombie = new Zombie(pedestrians.size(), xPosition, yPosition, zombieMaxVelocity,
                        zombieCollisionEscapeMagnitude, zombieMaxRadius, zombieMinRadius, mass, beta, zombieVisualField);
                zombie.setHeuristic(new ZombieHeuristic());
                if(!isCollision(zombie, pedestrians)) {
                    pedestrians.add(zombie);
                    inserted = true;
                }
            }
        }
    }

    private static boolean isCollision(Pedestrian newPedestrian, Set<Pedestrian> currentPedestrians) {
        return currentPedestrians.stream()
                .anyMatch(pedestrian ->
                        pedestrian.getDistanceTo(newPedestrian) <= pedestrian.getRadius() + newPedestrian.getRadius());
    }

    private static void runMultipleSizeSameDensity(double humanDensity, double zombieDeinsity, int runs, boolean zombieWall) {
        double increment = 5;
        double size = width;
        for (int i = 0; i < runs; i++) {
            int humanSize =  (int) Math.ceil(humanDensity * (size * size));
            int zombieSize = (int) Math.ceil(zombieDeinsity * (size * size));

            normal(size, size, simulationTime, humanEntrancePeriod, humanSize, zombieSize, humanMaxVelocity,
                    humanVisualField, humanMinRadius, humanMaxRadius, humanCollisionEscapeMagnitude, zombieMaxVelocity,
                    zombieVisualField, zombieMinRadius, zombieMaxRadius, zombieCollisionEscapeMagnitude,
                    exitGoalDiameter, zombieWall, "sim_" + i + ".data");

            size += increment;
        }
    }
}
