package ar.edu.itba.ss;

import ar.edu.itba.ss.Environment.EnvironmentImpl;
import ar.edu.itba.ss.Environment.StateImpl;
import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.Heuristic;
import ar.edu.itba.ss.Interface.Message;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.CustomHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.FixedMagneticHeuristic;
import ar.edu.itba.ss.Pedestrian.Human.Heuristic.HumanHeuristicFactory;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
import ar.edu.itba.ss.Pedestrian.Zombie.ZombieHeuristic;
import mikera.vectorz.Vector2;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

    static int zombiePopulation = 30;
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
            exit = processCommand(in.nextLine(), in);
        }
        in.close();
    }

    private static boolean processCommand(String s, Scanner in) {
        String[] args = s.split(" ");
        if( args.length == 1 && args[0].equals("help"))
            Message.Help.print();
        else if(args.length == 3 && args[0].equals("simulate") && args[1].equals("normal")) {
            normal(width, height, simulationTime, humanEntrancePeriod, humanPopulation, zombiePopulation,
                    humanMaxVelocity, humanVisualField, humanMinRadius, humanMaxRadius,
                    humanCollisionEscapeMagnitude, zombieMaxVelocity, zombieVisualField, zombieMinRadius, zombieMaxRadius,
                    zombieCollisionEscapeMagnitude, exitGoalDiameter, Boolean.parseBoolean(args[2]), "simulation.data", true);
        }
        else if(args.length == 9 && args[0].equals("simulate")) {
            normal(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]),
                    Double.parseDouble(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]),
                    humanMaxVelocity, humanVisualField, humanMinRadius, humanMaxRadius,
                    humanCollisionEscapeMagnitude, zombieMaxVelocity, zombieVisualField, zombieMinRadius, zombieMaxRadius,
                    zombieCollisionEscapeMagnitude, Integer.parseInt(args[7]), Boolean.parseBoolean(args[8]), "simulation.data", true);
        }
        else if(args.length == 21 && args[0].equals("simulate")  && args[1].equals("high")  && args[2].equals("custom")) {
            normal(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                    Double.parseDouble(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]),
                    Double.parseDouble(args[11]), Double.parseDouble(args[12]), Double.parseDouble(args[13]), Double.parseDouble(args[14]), Double.parseDouble(args[15]),
                    Double.parseDouble(args[16]), Double.parseDouble(args[17]), Double.parseDouble(args[18]), Double.parseDouble(args[19]), Double.parseDouble(args[20]),
                    Integer.parseInt(args[9]), Boolean.parseBoolean(args[10]), "simulation.data", true);
        }
        else if(args.length == 7 && args[0].equals("simulate")  && args[1].equals("multiple")  && args[2].equals("density")) {
            try {
                runMultipleSizeSameDensity(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Integer.parseInt(args[5]), Boolean.parseBoolean(args[6]));
            } catch (IOException e) {
                System.out.println("Sorry something went wrong, try restarting the script");
            }
        }
        else if(args.length == 3 && args[0].equals("modify") && args[1].equals("heuristic")) {
            try {
                HumanHeuristicFactory.HumanHeuristicType heuristicType = HumanHeuristicFactory.HumanHeuristicType.valueOf(args[2].toUpperCase());
                HumanHeuristicFactory.setHumanHeuristicType(heuristicType);
                if (heuristicType.equals(HumanHeuristicFactory.HumanHeuristicType.CUSTOM)) {
                    Message.SelectFormula.print();
                    String formula = getFormula(Message.SelectGoalFormula, in);
                    CustomHeuristic.setGoalFormula(formula);
                    formula = getFormula(Message.SelectWallFormula, in);
                    CustomHeuristic.setWallFormula(formula);
                    formula = getFormula(Message.SelectZombieFormula, in);
                    CustomHeuristic.setZombieFormula(formula);
                    Message.SelectZombieLimit.print();
                    CustomHeuristic.setZombieLimit(Integer.parseInt(in.nextLine()));
                }
                Message.HeuristicModify.print();

            } catch (IllegalArgumentException e) {
                Message.InvalidParams.print();
            }
        }
        else if(args.length == 1 && args[0].equals("exit"))
            return true;
        else
            Message.InvalidParams.print();
        return false;
    }

    private static int normal(double width, double height, double simulationTime, double humanEntrancePeriod,
                               int humanPopulation, int zombiePopulation, double humanMaxVelocity,
                               double humanVisualField, double humanMinRadius, double humanMaxRadius, double humanCollisionEscapeMagnitude,
                               double zombieMaxVelocity, double zombieVisualField, double zombieMinRadius, double zombieMaxRadius, double zombieCollisionEscapeMagnitude,
                               double exitGoalDiameter, boolean zombieWall, String filename, boolean save) {

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
        int survivors = engine.simulate(filename, save);
        Message.SimulationEnded.print();
        return survivors;
    }

    public static void vectorField() {

        Environment<Pedestrian> environment = new EnvironmentImpl(width, height, height/2,
                height/2, exitGoalDiameter, null, entranceDiameter);

        Set<Pedestrian> pedestrians = new HashSet<>();
        addPedestriansToVectorField(pedestrians, environment, separation, zombieRate);
        environment.setEnvironmentState(new StateImpl(pedestrians));

        SimulatorEngine<Pedestrian> engine = new SimulatorEngine<>(environment, deltaT, 2 * deltaT, humanEntrancePeriod);
        System.out.println("starting engine");
        engine.simulate("simulation.data", true);
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
                    try {
                        heuristic = HumanHeuristicFactory.newHumanHeuristic();
                    } catch (IllegalAccessException | InstantiationException e) {
                        System.out.println("There was an unhandled problem with the human heuristic creation.");
                        heuristic = new FixedMagneticHeuristic();
                    }
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
            double yPosition = (Math.random() * environment.getEntranceDiameter()) +
                    (((Vector2) environment.getStartingPoint()).y - environment.getEntranceDiameter()/2);
            Human human = new Human(index++, ((Vector2) environment.getStartingPoint()).x, yPosition, humanMaxVelocity,
                    humanCollisionEscapeMagnitude, humanMaxRadius, humanMinRadius, mass, beta, humanVisualField);
            try {
                human.setHeuristic(HumanHeuristicFactory.newHumanHeuristic());
            } catch (IllegalAccessException | InstantiationException e) {
                System.out.println("There was an unhandled problem with the human heuristic creation.");
            }
            humanQueue.offer(human);
        }

    }

    private static void addZombies(Set<Pedestrian> pedestrians, Environment<Pedestrian> environment, int size,
                                   double zombieMaxVelocity, double zombieVisualField, double zombieMinRadius,
                                   double zombieMaxRadius, double zombieCollisionEscapeMagnitude, boolean zombieWall) {
        for (int i = 0; i < size; i++) {
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

    private static void runMultipleSizeSameDensity(double humanDensity, double zombieDeinsity, int runs, boolean zombieWall) throws IOException {
        double increment = 0.5;
        double size = width;
        StringBuffer buffer = new StringBuffer();
        BufferedWriter writer = new BufferedWriter(new FileWriter("survivors.data"));
        for (int i = 0; i < runs; i++) {
            int humanSize =  (int) Math.ceil(humanDensity * (size * size));
            int zombieSize = (int) Math.ceil(zombieDeinsity * (size * size));
            List<Integer> survivors = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                survivors.add(normal(size, size, simulationTime, humanEntrancePeriod, humanSize, zombieSize, humanMaxVelocity,
                        humanVisualField, humanMinRadius, humanMaxRadius, humanCollisionEscapeMagnitude, zombieMaxVelocity,
                        zombieVisualField, zombieMinRadius, zombieMaxRadius, zombieCollisionEscapeMagnitude,
                        exitGoalDiameter, zombieWall, "sim_" + i + ".data", false));
            }
            survivors.stream().forEach(survivor -> buffer.append(survivor + " "));
            buffer.append('\n');
            size += increment;
        }
        SimulatorEngine.flushBuffer(buffer, writer);
    }

    private static String getFormula(Message message, Scanner in) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine eng = mgr.getEngineByName("JavaScript");

        boolean okey = false;
        String formula = null;
        message.print();
        while(!okey) {
            formula = in.nextLine();
            String evalFormoula = formula;
            try {
                eng.eval(evalFormoula
                        .replace("X", String.valueOf(1))
                        .replace("Y", String.valueOf(1))
                        .replace("Vx", String.valueOf(1))
                        .replace("Vy", String.valueOf(1))
                        .replace("R", String.valueOf(1))
                        .replace("D", String.valueOf(1)));
                okey = true;
            } catch (ScriptException e) {
                Message.InvalidSelectFormula.print();
                okey = false;
            }
        }
        return formula;
    }
}
