package ar.edu.itba.ss.Pedestrian;


import ar.edu.itba.ss.Interface.NeighbourFinder;

import java.util.HashMap;
import java.util.LinkedList;

public class CellIndexMethod {
    private HashMap<Integer, LinkedList<Integer>> cells;
    private HashMap<Integer, Integer> particleInCell;
    private double cellSize;
    private double l;
    private Entity[] entities;
    private NeighbourFinder neighbourFinder;

    public CellIndexMethod(double cellSize, double l, Entity[] ls, NeighbourFinder nf){
        cells = new HashMap<>();
        particleInCell = new HashMap<>();
        this.cellSize = cellSize;
        entities = ls;
        this.l = l;
        neighbourFinder = nf;
    }

    public void setEntities(Entity[] p){
        this.entities = p;
    }

    public void calculateCells(){
        cells.clear();
        for(Entity p : entities){
            int row = (int)Math.floor(p.getCoordinate().y / cellSize);
            int column = (int)Math.floor(p.getCoordinate().x / cellSize);
            int cell = (int)(row * (l/cellSize) + column);
            particleInCell.put(p.getNumber(), cell);
            cells.computeIfAbsent(cell, k -> new LinkedList<>());
            LinkedList<Integer> ls = cells.get(cell);
            ls.add(p.getNumber());
            cells.put(cell, ls);
        }
    }

//    public LinkedList<Entity> superPositionWithOneParticle(double x, double y){
//        Entity entity = new Entity(x, y, -1, 0, 0, 0, 0);
//        int row = (int)Math.floor(y / cellSize);
//        int col = (int)Math.floor(x / cellSize);
//        LinkedList<Entity> ret = new LinkedList<>();
//        for(int i = row-1; i <= row+1 && i < l/cellSize; i++){
//            for(int j = col-1; j <= col+1 && j < l/cellSize; j++){
//                if(row + i >= 0 && col + j >= 0 && cells.get(j + (int)(l/cellSize * i)) != null){
//                    for(int p : cells.get(j + (int)(l/cellSize * i) )){
//                        if(neighbourFinder.areNeighbours(entity, entities[p])){
//                            ret.add(entities[p]);
//                        }
//                    }
//                }
//            }
//        }
//        return ret;
//    }

    public LinkedList<Entity> getNeighbours(int number){
        LinkedList<Entity> ret = new LinkedList<>();
        if(number >= entities.length){
            return ret;
        }
        Entity p1 = entities[number];
        int cell = particleInCell.get(number);
        int row = (int)(cell / (l/cellSize));
        int col = (int)(cell % (l/cellSize));
        for(int i = row-1; i <= row+1 && i < l/cellSize; i++){
            for(int j = col-1; j <= col+1 && j < l/cellSize; j++){
                if(row + i >= 0 && col + j >= 0 && cells.get(j + (int)(l/cellSize * i)) != null){
                    for(int p : cells.get(j + (int)(l/cellSize * i) )){
                        if(number != p && neighbourFinder.areNeighbours(p1, entities[p])){
                            ret.add(entities[p]);
                        }
                    }
                }
            }
        }
        return ret;
    }
}
