package ar.edu.itba.ss.Environment;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Wall;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;
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

    private double goalRadius;

    public EnvironmentImpl() { }

    public EnvironmentImpl(double width, double height, double scapeCenter,
                           double entranceCenter, double goalRadius, Set<Pedestrian> pedestrians) {
        this.width = width;
        this.height = height;
        this.scapeCenter = scapeCenter;
        this.entranceCenter = entranceCenter;
        this.simulatedTime = 0;
        this.state = new StateImpl(pedestrians);
        this.goalRadius = goalRadius;
    }

    @Override
    public void updateMemberState(Pedestrian member, double deltaT) {

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
    public AVector validateVelocity(AVector velocity, Pedestrian pedestrian) {
        return velocity;
    }

    @Override
    public List<Entity> getNeighbours(Pedestrian member) {
        //List<Entity> neighbours = this.cim.getNeighbours(member.getNumber());
        List<Entity> neighbours = new ArrayList<>();
        List<Pedestrian> members = this.state.getMemebers().stream().filter(m -> member.isSameClass(m))
                .collect(Collectors.toList());
        members.remove(member);
        neighbours.addAll(members);
        neighbours.addAll(addWalls(member));
        return neighbours;
    }

    @Override
    public void moveSimulation(double deltaT) {
        // move forward the simulation by one time step
        List<Pedestrian> pedestrians = Collections.synchronizedList(state.getMemebers());
//                .stream().forEach(pedestrian -> updateMemberState(pedestrian, deltaT));
        synchronized (pedestrians) {
                state = state.update(deltaT, this);
        }
        simulatedTime += deltaT;
    }

//    @Override
//    public void updateMemberState(Pedestrian member, double deltaT) {
//        // aca tenemos que tener en cuenta que si es un humano y lo mordieron ... hay que sacarlo y tranformarlo
//        if((state = state.update(member, deltaT, this)) != null
//                &&  (member instanceof Human)
//                && ((Human) member).wasBitten()) {
//            Zombie zombie = ((Human) member).transform();
//            state = state.removeMember(member);
//            state = state.addMember(zombie);
//        }
//    }

    @Override
    public boolean hasFinished(double simulationT) {
        return simulationT <= this.simulatedTime;
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
    public double getFinalGoalRadius() {
        return goalRadius;
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
                    .add(new Wall(-1, coordinates.x , 0.0, 0.0, 0.0, 0.0, 0));
        } else if(coordinates.y + entity.getRadius() >= height) {
            wallEntities
                    .add(new Wall(-1, coordinates.x , height, 0.0, 0.0, 0.0, 0));
        }

        return wallEntities;
    }

}
