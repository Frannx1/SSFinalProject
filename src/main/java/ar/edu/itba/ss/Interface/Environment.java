package ar.edu.itba.ss.Interface;

public interface Environment<T> {
    /**
     * Every environment should be have an internal state which is updated with by every ative memeber in it.
     *
     */

    void updateMemberState(T member);

    State getEnvironmentState();

    void setEnvironmentState(State<T> state);

    boolean hasFinished();
}
