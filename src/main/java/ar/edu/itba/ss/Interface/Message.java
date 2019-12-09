package ar.edu.itba.ss.Interface;


public enum Message implements MessagePrinter {
    Welcome {
        public void print() {
            System.out.println("To get help write:\nhelp\n\n");
        }
    },
    Help {
        public void print() {
            System.out.println("To simulate human and zombies pedestrians with default parameters (width = 15, height = 15, " +
                    "simulationTime = 50 seg, humanEntrancePeriod = 0.6 (s), humanPopulation = 50, zombiePopulation = 10, " +
                    "exitGoalDiameter = 0.5, \nextra humans: humanMaxVelocity = 1.5, humanVisualField = 10.0, " +
                    "humanMinRadius = 1.0, humanMaxRadius = 1.1, humanCollisionEscapeMagnitude 1.5, \nextra zombies: " +
                    "zombieMaxVelocity = 0.8, zombieVisualField = 10.0, zombieMinRadius = 1.0, zombieMaxRadius = 1.1," +
                    " zombieCollisionEscapeMagnitude = 1.5):");
            System.out.println("simulate normal [zombieWall]\n");

            System.out.println("To simulate human and zombies pedestrians with a few custom parameters:");
            System.out.println("simulate [width] [height] [simulationTime] [humanEntrancePeriod] [humanPopulation] " +
                    "[zombiePopulation] [exitGoalDiameter] [zombieWall]\n");

            System.out.println("To simulate human and zombies pedestrians with a high custom parameters:");
            System.out.println("simulate high custom [width] [height] [simulationTime] [humanEntranceFrequency] [humanPopulation] " +
                    "[zombiePopulation] [exitGoalDiameter] [zombieWall] [humanMaxVelocity] [humanVisualField] [humanMinRadius] [humanMaxRadius] " +
                    "[humanCollisionEscapeMagnitude] [zombieMaxVelocity] [zombieVisualField] [zombieMinRadius] [zombieMaxRadius] [zombieCollisionEscapeMagnitude]\n");

            System.out.println("To simulate human and zombies pedestrians with a default parameters and multiple times with different size (increments of 5.0) and fix density:");
            System.out.println("simulate multiple density [humanDensity] [zombieDensity] [iterations] [zombieWall]\n");

            System.out.println("To modify the human heuristic (options: magnetic, fixed_magnetic, linear, ruled, custom):");
            System.out.println("modify heuristic [human_heuristic]\n");

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
    HeuristicModify{
        public void print() {
            System.out.println("\nHeuristic changed successfully.\n\n");
        }
    },
    InvalidSelectFormula {
        public void print() {
            System.out.println("\nThere was a problem with this formula, please try again.\n");
        }
    },
    SelectGoalFormula {
        public void print() {
            System.out.println("\nInsert goal distance formula. The x variable is the distance to the goal:");
        }
    },
    SelectWallFormula {
        public void print() {
            System.out.println("\nInsert wall distance formula. The x variable is the distance to a wall:");
        }
    },
    SelectZombieFormula {
        public void print() {
            System.out.println("\nInsert zombie distance formula. The x variable is the distance to a zombie:");
        }
    },
    SelectZombieLimit {
        public void print() {
            System.out.println("\nInsert the number of nearest zombies to acknowledge for decision making process:");
        }
    },
    InvalidParams {
        public void print() {
            System.out.println("\nParameters were not valid. To get help write:\nhelp\n\n");
        }
    }
    
}
