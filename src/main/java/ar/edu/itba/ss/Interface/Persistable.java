package ar.edu.itba.ss.Interface;


public interface Persistable<T> {
    /**
     * A member of this interface should be able to be written and read from a file.
     */

    void save(StringBuffer buffer);

    T load(StringBuffer buffer);
}
