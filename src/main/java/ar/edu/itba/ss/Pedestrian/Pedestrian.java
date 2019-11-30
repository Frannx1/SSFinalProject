package ar.edu.itba.ss.Pedestrian;


import mikera.vectorz.AVector;

public abstract class Pedestrian extends Entity {

       public abstract Pedestrian updatePosition();

       public abstract AVector getTarget();
}
