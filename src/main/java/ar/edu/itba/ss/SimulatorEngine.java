package ar.edu.itba.ss;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;
import ar.edu.itba.ss.Pedestrian.Pedestrian;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class SimulatorEngine<T extends Entity> {

    /**
     * This class keep iteration over the environment state
     *
     */

    private Environment<T> environment;

    private double deltaT;

    private double entranceFrequency;

    private double lastEntrance;

    private double simulationTime;

    private CellIndexMethod cim;

    private StringBuffer buffer;

    private Queue<Human> humanQueue;

    public SimulatorEngine(Environment<T> environment, double deltaT, double simulationTime, double entranceFrequency) {
        this.environment = environment;
        this.deltaT = deltaT;
        this.simulationTime = simulationTime;
        this.cim = new CellIndexMethod(1, Math.max(environment.getHeight(),
                environment.getWidth()), (Entity[]) environment.getEnvironmentState().getMembers().toArray(new Entity[] {}),
                new NeighbourFinderImpl(1));
        this.environment.setCim(this.cim);
        this.buffer = new StringBuffer();
        this.humanQueue = new ArrayDeque<>();
        this.entranceFrequency = entranceFrequency;
        this.lastEntrance = 0;
    }

    public Queue<Human> getHumanQueue() {
        return humanQueue;
    }

    public void simulate() {
        while (!environment.hasFinished(simulationTime)) {
            // This loop will have to:
            // run through each member of the environment and update their state
            // update the state of the environment
            // check if the simulation has finished
            double simulationTime = environment.getSimulatedTime();
            if(simulationTime - lastEntrance >= entranceFrequency && !humanQueue.isEmpty()){
                lastEntrance = simulationTime;
                ((State<Pedestrian>) environment.getEnvironmentState()).addMember(humanQueue.poll());
            }
            environment.getEnvironmentState().save(buffer);
            cim.calculateCells();
            environment.moveSimulation(deltaT, entranceFrequency);
        }
        generateOutputFiles(buffer, "simulation.data");
    }

    public void generateOutputFiles(StringBuffer stringBuffer, String outputName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
            writer.write(stringBuffer.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.toString());
        }
    }
}

