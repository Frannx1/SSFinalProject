package ar.edu.itba.ss;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SimulatorEngine<T> {

    /**
     * This class keep iteration over the environment state
     *
     */

    private Environment<T> environment;

    private double deltaT;

    private double simulationTime;

    private CellIndexMethod cim;

    private StringBuffer buffer;

    public SimulatorEngine(Environment<T> environment, double deltaT, double simulationTime) {
        this.environment = environment;
        this.deltaT = deltaT;
        this.simulationTime = simulationTime;
        this.cim = new CellIndexMethod(1, Math.max(environment.getHeight(),
                environment.getWidth()), (Entity[]) environment.getEnvironmentState().getMemebers().toArray(new Entity[] {}),
                new NeighbourFinderImpl(1));
        this.environment.setCim(this.cim);
        this.buffer = new StringBuffer();
    }

    public void simulate() {
        while (!environment.hasFinished(simulationTime)) {
            // This loop will have to:
            // run through each member of the environment and update their state
            // update the state of the environment
            // check if the simulation has finished
            environment.getEnvironmentState().save(buffer);
            cim.calculateCells();
            environment.moveSimulation(deltaT);
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

