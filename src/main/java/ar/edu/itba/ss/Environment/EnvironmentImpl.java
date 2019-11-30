package ar.edu.itba.ss.Environment;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.Pedestrian;

import java.util.List;

public class EnvironmentImpl implements Environment<Pedestrian> {

    private State<Pedestrian> state;

    private double width;

    private double heught;

    private double scapeCenter;

    private double entranceCenter;

    public EnvironmentImpl() { }

    public EnvironmentImpl(double width, double heught, double scapeCenter,
                           double entranceCenter, List<Pedestrian> pedestrians) {
        this.width = width;
        this.heught = heught;
        this.scapeCenter = scapeCenter;
        this.entranceCenter = entranceCenter;
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
    public void updateMemberState(Pedestrian member) {

    }

    @Override
    public boolean hasFinished() {
        return true;
    }
}
