package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.exceptions.LibraryException;

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
     * @throws LibraryException
     */
    T add (T item) throws LibraryException;

    /**
     * updates entity in the database based on matching id
     * @param item new data, the id must match an existing one
     * @return updated version of the data
     * @throws LibraryException
     */
    T update (T item) throws LibraryException;

    /**
     * deletes entity from the database based on matching id
     * @param item data that needs to be deleted, the id must match an existing one
     * @throws LibraryException
     */
    void delete (T item) throws LibraryException;

    /**
     * returns entity from the database that matches id
     * @param id unique data of an entity in the database
     * @return entity found based on id
     * @throws LibraryException
     */
    T searchById (int id) throws LibraryException;

    /**
     * return all data from the database
     * @return list of all entities from database
     * @throws LibraryException
     */
    List<T> getAll () throws LibraryException;
    /**
     * deletes all rows from the database
     * @return list of rows that were in the table before deletion
     * @throws LibraryException
     */
    List<T> removeAll () throws LibraryException;

    /**
     * prints all data from the database in the format defined by the toString method
     * @throws LibraryException
     */
    void viewAll() throws LibraryException;

    /**
     * inserts multiple rows into a table
     * @param items elements that are inserted into the table
     * @return list of successfully inserted elements
     *
     */
    List<T> insertAll(List<T> items);
}
