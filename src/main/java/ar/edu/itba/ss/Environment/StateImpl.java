package ar.edu.itba.ss.Environment;

import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.Pedestrian;

import java.util.List;

public class StateImpl implements State<Pedestrian> {

    /**
     * This is the actual implementation of an environment state
     * it will contain the positions of each active member as well as their information.
     *
     *
     */

    private List<Pedestrian> members;

    public StateImpl(List<Pedestrian> members) {
        this.members = members;
    }

    @Override
    public boolean update(Pedestrian member) {
        return false;
    }
}
