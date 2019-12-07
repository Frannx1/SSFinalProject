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

    private Set<Pedestrian> members;

    public StateImpl(Set<Pedestrian> members) {
        if(members!= null)
            this.members = Collections.synchronizedSet(members);
    }

    private void update(Pedestrian member, double deltaT, Environment environment) {
        this.members.remove(member);
        if(!(member instanceof Human) || !((Human) member).hasArrived(environment) ) {
            Pedestrian newMember = member.updatePosition(deltaT, environment)
                    .updateVelocity(deltaT, environment);
            if(member instanceof Human && ((Human) member).wasBitten())
                newMember = ((Human) member).transform();
            this.members.add(newMember);
        }
    }

    @Override
    public State<Pedestrian> update(double deltaT, Environment environment) {
        Pedestrian[] pedestrians = members.toArray(new Pedestrian[]{});
        for(int i = 0; i < pedestrians.length; i++)
            update(pedestrians[i], deltaT, environment);

        return new StateImpl(members);
    }

    @Override
    public State<Pedestrian> removeMember(Pedestrian member) {
        this.members.remove(member);
        return new StateImpl(new HashSet<>(this.members));
    }

    @Override
    public State<Pedestrian> addMember(Pedestrian member) {

        this.members.add(member);
        return new StateImpl(new HashSet<>(this.members));
    }

    @Override
    public List<Pedestrian> getMemebers() {
        return new ArrayList<>(members);
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
        Set<Pedestrian> pedestrians = Arrays.stream(lines).map(str -> {
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
        }).collect(Collectors.toSet());

        return new StateImpl(pedestrians);
    }
}
