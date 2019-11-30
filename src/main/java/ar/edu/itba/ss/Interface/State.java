package ar.edu.itba.ss.Interface;

public interface State<T> {

    /**
     * A State contains the information of the environment
     * each action taken on the environment will affect the state.
     * The generic stands for the active members of the environment
     *
     */

    boolean update(T member);

}

