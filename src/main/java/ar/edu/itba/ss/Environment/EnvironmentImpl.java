package ar.edu.itba.ss.Environment;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Wall;
import mikera.vectorz.AVector;
import mikera.vectorz.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnvironmentImpl implements Environment<Pedestrian> {

    private State<Pedestrian> state;

    private CellIndexMethod cim;

    private double width;

    private double height;

    private double scapeCenter;

    private double entranceCenter;

    private double simulatedTime;

    private double goalDiameter;

    private double entranceDiameter;

    private int survivors;

    public EnvironmentImpl() {}

    public EnvironmentImpl(double width, double height, double scapeCenter,
                           double entranceCenter, double goalDiameter, Set<Pedestrian> pedestrians, double entranceDiameter) {
        this.width = width;
        this.height = height;
        this.scapeCenter = scapeCenter;
        this.entranceCenter = entranceCenter;
        this.simulatedTime = 0;
        this.state = new StateImpl(pedestrians);
        this.goalDiameter = goalDiameter;
        this.entranceDiameter = entranceDiameter;
        this.survivors = 0;
    }

    @Override
    public State getEnvironmentState() {
        return state;
    }

    @Override
    public void setEnvironmentState(State<Pedestrian> state) {
        this.state = state;
    }

    @Override
    public AVector validatePosition(AVector coordinate, Pedestrian pedestrian) {
     // validate is the given coordinate is within the given environment limits
        Vector2 vec = (Vector2) coordinate.clone();
        if(vec.x + pedestrian.getRadius() > width)
            vec.x = width - pedestrian.getRadius();
        if(vec.x - pedestrian.getRadius() < 0)
            vec.x = pedestrian.getRadius();
        if(vec.y + pedestrian.getRadius() > height)
            vec.y = height - pedestrian.getRadius();
        if(vec.y - pedestrian.getRadius() < 0)
            vec.y = pedestrian.getRadius();
        return vec;
    }

    @Override
    public double getEntranceDiameter() {
        return entranceDiameter;
    }

    @Override
    public AVector validateVelocity(AVector velocity, Pedestrian pedestrian) {
        return velocity;
    }

    @Override
    public List<Entity> getNeighbours(Pedestrian member) {
        //List<Entity> neighbours = this.cim.getNeighbours(member.getNumber());
        List<Entity> neighbours = new ArrayList<>();
        List<Pedestrian> members = this.state.getMembers().stream().filter(m -> member.isSameClass(m))
                .collect(Collectors.toList());
        members.remove(member);
        neighbours.addAll(members);
        neighbours.addAll(addWalls(member));
        return neighbours;
    }

    @Override
    public void moveSimulation(double deltaT, double entranceFrequency) {
        // move forward the simulation by one time step
        List<Pedestrian> pedestrians = state.getMembers();
//                .stream().forEach(pedestrian -> updateMemberState(pedestrian, deltaT));
        synchronized (pedestrians) {
                state = state.update(deltaT, this);
        }
        simulatedTime += deltaT;
    }

    @Override
    public boolean hasFinished(double simulationT) {

        return simulationT <= this.simulatedTime && state.getMembers().stream()
                .filter(pedestrian -> pedestrian instanceof Human).count() == 0;
    }

    /**
     * We assume a horizontal field
     * @return
     */
    @Override
    public AVector getStartingPoint() {
        return new Vector2(0.5, entranceCenter);
    }

    /**
     * We assume a horizontal field
     * @return
     */
    @Override
    public AVector getFinalGoal() {
        return new Vector2(width - 0.5, scapeCenter);
    }

    @Override
    public double getFinalGoalDiameter() {
        return goalDiameter;
    }

    @Override
    public CellIndexMethod getCim() {
        return cim;
    }

    @Override
    public void setCim(CellIndexMethod cim) {
        this.cim = cim;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getSimulatedTime() {
        return simulatedTime;
    }

    private List<Entity> addWalls(Entity entity) {
        Vector2 coordinates = entity.getCoordinate();
        List<Entity> wallEntities = new ArrayList<>();
        if (coordinates.x  <= entity.getRadius()) {
            wallEntities
                    .add(new Wall(-1, 0 ,coordinates.y, 0.0, 0.0, 0.0, 0));
        } else if (coordinates.x  + entity.getRadius() >= width) {
            wallEntities
                    .add(new Wall(-1, width ,coordinates.y, 0.0, 0.0, 0.0, 0));
        } else if(coordinates.y <= entity.getRadius()) {
            wallEntities
                    .add(new Wall(-1, coordinates.x, 0.0, 0.0, 0.0, 0.0, 0));
        } else if(coordinates.y + entity.getRadius() >= height) {
            wallEntities
                    .add(new Wall(-1, coordinates.x, height, 0.0, 0.0, 0.0, 0));
        }

        return wallEntities;
    }

    private List<Entity> getWallsNearest(Entity entity) {
        Vector2 coordinates = entity.getCoordinate();
        List<Entity> wallEntities = new ArrayList<>();
        wallEntities.add(new Wall(-1, 0 ,coordinates.y, 0.0, 0.0, 0.0, 0));
        wallEntities.add(new Wall(-1, coordinates.x, height, 0.0, 0.0, 0.0, 0));
        wallEntities.add(new Wall(-1, coordinates.x, 0.0, 0.0, 0.0, 0.0, 0));

        if (entity.getDistanceTo((Vector2) getFinalGoal()) >= 4 * getFinalGoalDiameter())
            wallEntities.add(new Wall(-1, width ,coordinates.y, 0.0, 0.0, 0.0, 0));

        return wallEntities;
    }

    @Override
    public List<Vector2> getDirectionsToWall(Pedestrian pedestrian) {
        List<Entity> walls = getWallsNearest(pedestrian);
        return walls.stream().map(pedestrian::getVectorTo).collect(Collectors.toList());
    }

    @Override
    public void addSurvivor() {
        survivors++;
    }

    @Override
    public int getSurvivors() {
        return survivors;
    }

}
