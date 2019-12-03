package ar.edu.itba.ss.Interface;

import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.List;

public interface Environment<T> {
    /**
     * Every environment should be have an internal state which is updated with by every ative memeber in it.
     *
     */

    void updateMemberState(T member, double deltaT);

    State getEnvironmentState();

    void setEnvironmentState(State<T> state);

    AVector validatePosition(AVector coordinate, Pedestrian pedestrian);

    AVector validateVelocity(AVector velocity, Pedestrian pedestrian);

    List<Entity> getNeighbours(T member);

    void moveSimulation(double deltaT);

    boolean hasFinished(double simulationT);

    AVector getStartingPoint();

    AVector getFinalGoal();

    CellIndexMethod getCim();

    void setCim(CellIndexMethod cim);

    double getWidth();

    double getHeight();

}
