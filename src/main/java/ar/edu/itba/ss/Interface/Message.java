package ar.edu.itba.ss.Interface;


public enum Message implements MessagePrinter {
    Welcome {
        public void print() {
            System.out.println("To get help write:\nhelp\n\n");
        }
    },
    Help {
        public void print() {
            System.out.println("To simulate pedestrians with default parameters (width = 15, height = 15, " +
                    "simulationTime = 50 seg, humanEntranceFrequency = 0.6 (pedestrians/seg), humanPopulation = 50, " +
                    "zombiePopulation = 10, humanMaxVelocity = 1.5, zombieMaxVelocity = 0.8, collisionEscapeMagnitude 1.5, exitGoalDiameter = 0.5):");
            System.out.println("simulate normal [zombieWall]");
            System.out.println("To simulate pedestrians with a few custom parameters:");
            System.out.println("simulate [width] [height] [simulationTime] [humanEntranceFrequency] [humanPopulation] " +
                    "[zombiePopulation] [exitGoalDiameter] [zombieWall]");
            System.out.println("To simulate pedestrians with a default parameters and multiple times with different size (increments of 5.0) and fix density:");
            System.out.println("simulate multiple density [humanDensity] [zombieDensity] [iterations] [zombieWall]");

            System.out.println("To exit application: \nexit\n\n");
        }
    },
    SimulationRunning {
        public void print() {
            System.out.println("\nSimulation started running...");
        }
    },
    SimulationEnded {
        public void print() {
            System.out.println("\nSimulation finished.");
        }
    },
    InvalidParams {
        public void print() {
            System.out.println("\nParameters were not valid. To get help write:\nhelp\n\n");
        }
    }
    
}
