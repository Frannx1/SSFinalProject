package ar.edu.itba.ss.Interface;

import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;

public interface Environment<T extends Entity> {
    /**
     * Every environment should be have an internal state which is updated with by every ative memeber in it.
     *
     */

    State<T> getEnvironmentState();

    void setEnvironmentState(State<T> state);

    AVector validatePosition(AVector coordinate, T entity);

    double getEntranceRadius();

    AVector validateVelocity(AVector velocity, T entity);

    List<Entity> getNeighbours(T member);

    void moveSimulation(double deltaT, double entranceFrequency);

    boolean hasFinished(double simulationT);

    AVector getStartingPoint();

    AVector getFinalGoal();

    double getFinalGoalRadius();

    CellIndexMethod getCim();

    void setCim(CellIndexMethod cim);

    double getWidth();

    double getHeight();

    double getSimulatedTime();

    List<Vector2> getDirectionsToWall(T entity);

    void addSurvivor();

    int getSurvivors();

}
