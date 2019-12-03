package ar.edu.itba.ss.Interface;

import java.util.List;

public interface State<T> extends Persistable<State<T>> {

    /**
     * A State contains the information of the environment
     * each action taken on the environment will affect the state.
     * The generic stands for the active members of the environment
     *
     */

    State<T> update(T member, double deltaT, Environment environment);

    State<T> removeMember(T member);

    State<T> addMember(T member);

    List<T> getMemebers();


}

