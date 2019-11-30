package ar.edu.itba.ss.Pedestrian;


import ar.edu.itba.ss.Interface.NeighbourFinder;

public class NeighbourFinderImpl implements NeighbourFinder {

    private double cellSize;

    public NeighbourFinderImpl(double cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public boolean areNeighbours(Entity p1, Entity p2) {
        return p1.getDistanceTo(p2) <= (p1.getRadius() + p2.getRadius());
    }

    public static boolean inContact(Entity p1, Entity p2) {
        return p1.getDistanceTo(p2) <= (p1.getRadius() + p2.getRadius());
    }
}
