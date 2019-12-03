package ar.edu.itba.ss;

import ar.edu.itba.ss.Pedestrian.CellIndexMethod;
import ar.edu.itba.ss.Pedestrian.Entity;
import ar.edu.itba.ss.Pedestrian.NeighbourFinderImpl;
import mikera.vectorz.Vector2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static ar.edu.itba.ss.Pedestrian.Entity.*;

public class Environmentold {
    /**
     * This class represents the circular path in which all entities float
     * for the sake of complexity we'll think of it as a straight line with periodic contour.
     */
/*
    private double width;
    private double length;

    private List<Entity> entities;
    private CellIndexMethod cim;

    private double simulationDt;

    public static final double dt = MIN_RADIUS / (2*Math.max(MAX_SPEED, SCAPE_SPEED));

    // constants taken from Parisi's paper.
    public static  double VELOCITY_SCALING_FACTOR = 0.874;
    public static final  double thao =  0.5;

    private IOManager ioManager;

    private double wallDistance;
    public Environmentold(double width, double length, double dt, List<Entity> entities, IOManager ioManager) {
        this.ioManager = ioManager;
        this.length = length;
        this.simulationDt = dt;
        this.entities = entities.stream()
                .sorted(Comparator.comparingLong(Entity::getNumber)).collect(Collectors.toList());
        // remember, each cell must be able to contain at least a particle
        double cellSize = 1;
        // we do this to add entities simulating the walls at both sides of the corridor.
        this.width = width;
        this.wallDistance = cellSize;

        this.cim = new CellIndexMethod(cellSize, Math.max(width, length),
                this.entities.toArray(new Entity[]{}), new NeighbourFinderImpl(cellSize));

    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void simulate(long simulationTime) {

         * pseudo code:
         *  - calculate current time position.
         *  -   if two entities are touching, repel themselves with scape velocity.
         *      Bring their radius to the minimum.
         *  - continue their natural trend to move forward (apply moving force).


        StringBuffer buffer = new StringBuffer();
        buffer.append(entities.size());
        buffer.append("\n\n");
        double timeSteps = 0;
        while (timeSteps < simulationTime) {
            this.cim.calculateCells();
            double currentTime = timeSteps;
            Vector2[] mean = new Vector2[1];
            mean[0] = new Vector2();
            entities.stream().forEach(p -> {
                if(currentTime % simulationDt < simulationDt)
                    buffer.append(p.toString() + "\n");
                updateVelocity(p);
                updateRadius(p);
                updatePosition(p);
                mean[0].add(p.getVelocity());

            });

            if(currentTime % simulationDt < simulationDt) {
                buffer.append(entities.size() + "\n");
                buffer.append('\n');
            }

            timeSteps += dt;
        }
        ioManager.generateOutputFiles(buffer);
    }

    public List<Double> runFundamental(long simulationTime) {
        double timeSteps = 0;
        List<Double> meanVelocities = new ArrayList<>();
        while (timeSteps < simulationTime) {
            this.cim.calculateCells();

            // calculate the mean velocity for each time step
            // and the std for each time step

            Vector2[] mean = new Vector2[1];
            mean[0] = new Vector2();
            entities.stream().forEach(p -> {

                updateVelocity(p);
                updateRadius(p);
                updatePosition(p);

                mean[0].add(p.getVelocity());

            });


            meanVelocities.add(mean[0].magnitude()/ entities.size());
            timeSteps += dt;
        }

        return meanVelocities;
    }

    public List<Double> runFundamental(long simulationTime, double beta){
        this.VELOCITY_SCALING_FACTOR = beta;
        return runFundamental(simulationTime);
    }

    private void updatePosition(Entity p) {
        // X(t + simulationDt) = X(t) + simulationDt * Vd(t)

        Vector2 velocityCorrection = (Vector2) p.getVelocity().multiplyCopy(dt);
        p.getCoordinate().add(velocityCorrection);
        if (p.getCoordinate().y > length) {
            Random random = new Random();
            p.setCoordinate(new Vector2(random.nextDouble() * width, 0));
        }
        if(p.getCoordinate().x > width) {
            p.getCoordinate().x = width - p.getRadius();
            p.setScape(true);
        }

        if(p.getCoordinate().x <= p.getRadius()) {
            p.getCoordinate().x = p.getRadius();
            p.setScape(true);
        }

    }

    private void updateVelocity(Entity p) {
        List<Entity> neighbours = cim.getNeighbours(p.getNumber());
        neighbours.addAll(addWalls(p));
        Vector2 scapeDirection = new Vector2();
        for (Entity n : neighbours) {
            boolean contact = NeighbourFinderImpl.inContact(p, n);
            if (contact) {
                // the mother fucker who wrote this decided to return null if the vector to normalize is zero.
                Vector2 vec = (Vector2) p.getCoordinate().addCopy(n.getCoordinate().multiplyCopy(-1)).toNormal();
                if(vec != null)
                    scapeDirection.add(vec);
            }
        }

        if (scapeDirection.magnitude() != 0) {
            scapeDirection = scapeDirection.toNormal();
            scapeDirection.multiply(SCAPE_SPEED);
            p.setScape(true);
            p.setVelocity(scapeDirection);
        }  else {
            Vector2 newVelocity = new Vector2();
            double factor = (p.getRadius() - MIN_RADIUS) / (MAX_RADIUS - MIN_RADIUS);
            factor = MAX_SPEED * Math.pow(factor, VELOCITY_SCALING_FACTOR);
            newVelocity.y = 1;
            newVelocity.multiply(factor);

            p.setVelocity(newVelocity);
            p.setScape(false);
        }

    }

    private void updateRadius(Entity p) {
        if (p.isScape()) {
            p.setRadius(MIN_RADIUS);
            return;
        }
        double newRadius = p.getRadius() + MAX_RADIUS / (thao / dt);
        p.setRadius(newRadius);
    }

    private List<Entity> addWalls(Entity entity) {
        Vector2 coordinates = entity.getCoordinate();
        List<Entity> wallEntities = new ArrayList<>();
//        if (coordinates.x  <= entity.getRadius()) {
//            wallEntities
//                    .add(new Entity(-1, 0 ,coordinates.y, 0.0, 0.0, 0.0, 0));
//        } else if (coordinates.x  + entity.getRadius() >= width) {
//            wallEntities
//                    .add(new Entity(-1, width ,coordinates.y, 0.0, 0.0, 0.0, 0));
//        }
        return wallEntities;
    }
*/
}

