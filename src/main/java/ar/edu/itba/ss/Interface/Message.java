package ar.edu.itba.ss.Interface;


public enum Message implements MessagePrinter {
    Welcome {
        public void print() {
            System.out.println("To get help write:\nhelp\n\n");
        }
    },
    Help {
        public void print() {

            System.out.println("To simulate population:");
            System.out.println("simulate [DT] [number of entities] [Time]");
            System.out.println("To generate fundamental graph:");
            System.out.println("fundamental");
            System.out.println("To run width comparison:");
            System.out.println("widthComparison");
            System.out.println("To run approximator:");
            System.out.println("approximation [pathToFile] [minBeta] [maxBeta]");
            System.out.println("To run velocity graph:");
            System.out.println("velocities [numberOfParticles] [simulationTime]");
            System.out.println("To exit application: exit\n\n");
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
