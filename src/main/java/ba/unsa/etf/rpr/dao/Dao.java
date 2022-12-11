package ba.unsa.etf.rpr.dao;

import java.util.List;

/**
 * generic interface related to basic manipulations with data in the database
 * @author Amina Bajric
 */
public interface Dao<T> {
    /**
     * adding data to the database
     * @param item that needs to be added
     * @return item that is added
     */
    T add (T item);

    /**
     * updates entity in the database based on matching id
     * @param item new data, the id must match an existing one
     * @return updated version of the data
     */
    T update (T item);

    /**
     * deletes entity from the database based on matching id
     * @param item data that needs to be deleted, the id must match an existing one
     */
    void delete (T item);

    /**
     * returns entity from the database that matches id
     * @param id unique data of an entity in the database
     * @return entity found based on id
     */
    T getById (int id);

    /**
     * return all data from the database
     * @return list of all entities from database
     */
    List<T> getAll ();
}
