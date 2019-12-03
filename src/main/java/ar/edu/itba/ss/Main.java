package ar.edu.itba.ss;

import ar.edu.itba.ss.Interface.Message;
import ar.edu.itba.ss.Pedestrian.Entity;
import mikera.vectorz.Vector2;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.ss.Pedestrian.Entity.MAX_SPEED;
import static ar.edu.itba.ss.Pedestrian.Entity.MIN_RADIUS;


public class Main {

//    public static final double WIDTH = 2;
//    public static final double HEIGHT = 13;
//
//    private static long run  = 0;
//    // variables for fundamental graph execution.
//    public static final double SIMULATIONI_DT = 0.1;
//    public static final long SIMULATION_TIME = 100;
//
//    public static void main(String[] args) {
//        if(args.length > 0) {
//            System.out.println("First run the program with no arguments.");
//            return;
//        }
//        Scanner in = new Scanner(System.in);
//        boolean exit = false;
//        Message.Welcome.print();
//        while(!exit)
//        {
//            exit = processCommand(in.nextLine());
//        }
//        in.close();
//
//    }
//
//    private static boolean processCommand(String s) {
//        String[] args = s.split(" ");
//        if( args.length == 1 && args[0].equals("help"))
//            Message.Help.print();
//        else if(args[0].equals("simulate") && args.length == 4) {
//            List<Entity> entities = generateParticles(Long.valueOf(args[2]));
//            IOManager ioManager = new IOManager("simulation-" + args[1] + "-" + args[2] + "-" + args[3] + "-" + WIDTH);
//            Environmentold environment = new Environmentold(WIDTH, HEIGHT, Double.valueOf(args[1]), entities, ioManager);
//            environment.simulate(Long.valueOf(args[3]));
//            System.out.println("finish simulation!");
//
//        } else if(args[0].equals("fundamental") && args.length == 1) {
//            runFundamental();
//            System.out.println("process finished!");
//        } else if(args[0].equals("widthComparison") && args.length == 1) {
//            runWidthComparison();
//            System.out.println("process finished!");
//
//        } else if(args[0].equals("approximation")&& args.length == 4 ){
//
//
//            try {
//                List<List<Double>> fundamentalValues;
//                fundamentalValues = IOManager.loadFundamental(args[1]);
//                runApproximation(fundamentalValues.get(0), fundamentalValues.get(1), SIMULATION_TIME,
//                        Double.valueOf(args[2]), Double.valueOf(args[3]) );
//                System.out.println("Process finished!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        } else if(args[0].equals("velocities") && args.length ==3) {
//            runVelocityGraph(Long.valueOf(args[1]), Long.valueOf(args[2]));
//            System.out.println("Process finished!");
//        }else if(args.length == 1 && args[0].equals("exit"))
//            return true;
//        else
//            Message.InvalidParams.print();
//
//        return false;
//    }
//
//    private static List<Entity> generateParticles(long populationSize) {
//        double r, x, y;
//        int k = 0; // superposition maximum
//        Random xRand = new Random();
//        Random yRand = new Random();
//        LinkedList<Entity> particlesAuxes = new LinkedList<>();
//
//        while (k < populationSize) {
//            r = MIN_RADIUS;
//            x = xRand.nextDouble() * (WIDTH - 2 * r) + r;
//
//            y = yRand.nextDouble() * (HEIGHT - 2 * r) + r;
//            if(!checkSuperposition(particlesAuxes, x, y, r)) {
//                k++;
//                Entity p = new Entity(particlesAuxes.size(), x, y, 0, MAX_SPEED, 1.0, r);
//                particlesAuxes.add(p);
//            }
//        }
//        return particlesAuxes.stream().collect(Collectors.toList());
//    }
//
//    private static boolean checkSuperposition(List<Entity> entities, double x, double y, double r) {
//        for (Entity other : entities) {
//            if (other.getRadius() + r > other.getDistanceTo(new Vector2(x, y)))
//                return true;
//        }
//        return false;
//    }
//
//
//    private static void runFundamental() {
//        int runNumber = 5;
//        List<List<Double>> velocitiesPerRun = new ArrayList<>();
//        IOManager densityManager = new IOManager("density" + "-" + runNumber);
//        for(int i = 5; i <= 300; i += 5) {
//            double density = i / (WIDTH * HEIGHT);
//            List<Entity> entities = generateParticles(i);
//
//
//            Environmentold environment = new Environmentold(WIDTH, HEIGHT, SIMULATIONI_DT, entities, null);
//            velocitiesPerRun.add(environment.runFundamental(SIMULATION_TIME));
//            densityManager.output.append(density + "\t");
//        }
//        densityManager.generateOutputFiles();
//
//        IOManager ioManager =new IOManager("fundamental-" + runNumber);
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < velocitiesPerRun.get(0).size(); i++) {
//            for (int j = 0; j < velocitiesPerRun.size(); j++) {
//                buffer.append(velocitiesPerRun.get(j).get(i) + "\t");
//            }
//            buffer.append("\n");
//        }
//
//        ioManager.generateOutputFiles(buffer);
//    }
//
//
//    private static void runWidthComparison() {
//        long numberOfParticles = 300;
//
//        double[] widthArray = new double[]{1,2,3,4,5};
//        for(int i = 0; i < widthArray.length; i++) {
//            runWidthFundamental(widthArray[i], i, numberOfParticles);
//        }
//
//    }
//
//
//    private  static void runWidthFundamental(double width, int runNumber, long numberOfParticles) {
//        double max_density = 10;
//        List<List<Double>> velocitiesPerRun = new ArrayList<>();
//        IOManager densityManager = new IOManager("widthComparison" + "-" + runNumber);
//        for(int i = 5; i <= numberOfParticles; i += 5) {
//            double density = i / (width * HEIGHT);
//            if(density > max_density)
//                break;
//            List<Entity> entities = generateParticles(i);
//
//
//            Environmentold environment = new Environmentold(width, HEIGHT, SIMULATIONI_DT, entities, null);
//            velocitiesPerRun.add(environment.runFundamental(SIMULATION_TIME));
//            densityManager.output.append(density + "\t");
//        }
//        densityManager.generateOutputFiles();
//
//        IOManager ioManager =new IOManager("fundamentalComparison-" + runNumber);
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < velocitiesPerRun.get(0).size(); i++) {
//            for (int j = 0; j < velocitiesPerRun.size(); j++) {
//                buffer.append(velocitiesPerRun.get(j).get(i) + "\t");
//            }
//            buffer.append("\n");
//        }
//
//        ioManager.generateOutputFiles(buffer);
//    }
//
//
//    private static void runApproximation(List<Double> densities, List<Double> meanVelocities, long simulationTime, double minBeta, double maxBeta){
//        /**
//         * In this particular case we are comparing our model with
//         * Predtechenskii and Milinskii's model. By finding the best β that
//         * minimizes the MSE.
//         */
//
//        final long amountOfPartitions = 100;
//        List<Double> errorList = new ArrayList<>();
//        List<Double> betaList = new ArrayList<>();
//        //initialize exploring space
//        for (double i = 0; i < maxBeta; i += (maxBeta - minBeta)/amountOfPartitions ) {
//            betaList.add(i);
//        }
//        int index= 1;
//        for(Double beta: betaList) {
//            System.out.println("Corrida " + index);
//            index++;
//            List<Double> modelVelocities = new ArrayList<>();
//
//            for (Double density : densities) {
//                long populationSize = (long) (density * WIDTH * HEIGHT);
//                List<Entity> entities = generateParticles(populationSize);
//                Environmentold environment = new Environmentold(WIDTH, HEIGHT, SIMULATIONI_DT, entities, null);
//                List<Double> velocitiesPerDensity = environment.runFundamental(simulationTime, beta);
//                // calculate mean velocity for that density
//                double meanVelocity = velocitiesPerDensity.stream().mapToDouble(p -> p).average().getAsDouble();
//                modelVelocities.add(meanVelocity);
//            }
//
//            //calculate MSE
//            double error = 0;
//            for (int i = 0; i < modelVelocities.size(); i++) {
//                error += Math.pow(meanVelocities.get(i) - modelVelocities.get(i), 2);
//
//
//            }
//            error = error/ modelVelocities.size();
//            errorList.add(error);
//        }
//
//        // now we just create the corresponding files
//
//        IOManager ioManager = new IOManager("Betaerrors-" + minBeta + "-" + maxBeta );
//        betaList.stream().forEach(
//                value -> ioManager.output.append(value + "\t" + errorList.get(betaList.indexOf(value)) + "\n"));
//        ioManager.generateOutputFiles();
//
//    }
//
//
//    public static void runVelocityGraph(long populationSize, long simulationTime) {
//
//        List<Entity> entities = generateParticles(populationSize);
//        Environmentold environment = new Environmentold(WIDTH, HEIGHT, SIMULATIONI_DT, entities, null);
//        List<Double> meanVelocities = environment.runFundamental(simulationTime);
//        IOManager manager = new IOManager("meanVelocities-" + populationSize + "-" + simulationTime + "-" + run);
//        run++;
//        double time = 0;
//        int index = 0;
//        for(Double vel : meanVelocities) {
//            if(index % 100 == 0)
//                manager.output.append(time + "\t" + vel + "\n");
//            time += SIMULATIONI_DT;
//            index++;
//        }
//
//        manager.generateOutputFiles();
//    }
//

}
