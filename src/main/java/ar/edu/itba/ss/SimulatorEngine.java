package ar.edu.itba.ss;

import ar.edu.itba.ss.Interface.Environment;

public class SimulatorEngine<T> {

    /**
     * This class keep iteration over the environment state
     *
     */

    private Environment<T> environment;

    public SimulatorEngine(Environment<T> environment) {
        this.environment = environment;
    }

    public void simulate() {
        while (!environment.hasFinished()) {
            // This loop will have to:
            // run through each member of the environment and update their state
            // update the state of the environment
            // check if the simulation has finished

        }
    }
}

