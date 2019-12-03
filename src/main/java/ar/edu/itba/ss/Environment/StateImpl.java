package ar.edu.itba.ss.Environment;

import ar.edu.itba.ss.Interface.Environment;
import ar.edu.itba.ss.Interface.State;
import ar.edu.itba.ss.Pedestrian.Human.Human;
import ar.edu.itba.ss.Pedestrian.Pedestrian;
import ar.edu.itba.ss.Pedestrian.Zombie.Zombie;

import java.util.*;
import java.util.stream.Collectors;

public class StateImpl implements State<Pedestrian> {

    /**
     * This is the actual implementation of an environment state
     * it will contain the positions of each active member as well as their information.
     *
     *
     */

    private List<Pedestrian> members;

    public StateImpl(List<Pedestrian> members) {
        if(members!= null)
            this.members = Collections.synchronizedList(members);
    }

    @Override
    public State<Pedestrian> update(Pedestrian member, double deltaT, Environment environment) {

        Pedestrian newMember = member.updatePosition(deltaT, environment)
                .updateVelocity(deltaT, environment);
        this.members.remove(member);
        this.members.add(newMember);
        return new StateImpl(new ArrayList<>(this.members));
    }

    @Override
    public State<Pedestrian> removeMember(Pedestrian member) {
        this.members.remove(member);
        return new StateImpl(new ArrayList<>(this.members));
    }

    @Override
    public State<Pedestrian> addMember(Pedestrian member) {

        this.members.add(member);
        return new StateImpl(new ArrayList<>(this.members));
    }

    @Override
    public List<Pedestrian> getMemebers() {
        return members;
    }

    @Override
    public void save(StringBuffer buffer) {
        buffer.append(members.size() + "\n\n");
        members.stream().forEach(pedestrian -> {
          buffer.append(pedestrian.toString() + "\n");
        });
    }

    // TODO: check this shit
    @Override
    public State<Pedestrian> load(StringBuffer buffer) {
        // the buffer must contain a simple state
        // this will not support a series of states

        String[] lines = buffer.toString().split("\n");
        List<Pedestrian> pedestrians = Arrays.stream(lines).map(str -> {
            Scanner scanner = new Scanner(str);
            int number = scanner.nextInt();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double vx = scanner.nextDouble();
            double vy = scanner.nextDouble();
            double mass = scanner.nextDouble();
            double radius = scanner.nextDouble();
            boolean human = scanner.nextBoolean();
            if(human)
                return new Human(number, x, y, vx, vy, mass);
            else
                return new Zombie(number, x, y, vx, vy, mass);
        }).collect(Collectors.toList());

        return new StateImpl(pedestrians);
    }
}
