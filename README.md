# RaptorV8
## This is a simulation engine
The `App` is the main class. At the moment, all initial config is being 
done through this class. 


### Main Components:

    Engine
    Environment
    Environment State
    Pedestrian
    


## Basics

- `Engine: responsible for the simulation loop`  
- `Environment: Object representing the environment, dimensions and space distribution. 
Based on inversion of control, this class will check for wall impacts.`
- `State: class containing the active state of an environment, this means all the moving parts (active members).
in our case this will be Pedestrians.` 
- `Pedestrian: the most basic abstraction of what an active member of this environment should do.` 





