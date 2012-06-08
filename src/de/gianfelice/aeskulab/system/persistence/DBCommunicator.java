package de.gianfelice.aeskulab.system.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface which defines the methods needed for a database communication. The
 * responsibilities of classes, which inherit this interface, are to connect to
 * the database and manage the objects which are stored to or deleted/fetched
 * from the database.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 1.1.3
 */
public interface DBCommunicator {

	// ------------------------------- Method(s) -------------------------------
    /**
     * Initialises this communicator for the given configuration name.
     * 
     * @param name Configuration name
     */
    public void init(String name);

    /**
     * Fetch a specific entity object from the database.
     *
     * @param <A> the generic type
     * @param c the c
     * @param id the id
     * @return     The object, that matched the given parameters
     */
    public <A extends PersistentObject> A find(Class<A> c, Long id);

    /**
     * Fetches all entities in the database of the given entity type.
     *
     * @param <A> the generic type
     * @param c the c
     * @return     A list of all fetched objects
     */
    public <A extends PersistentObject> List<A> list(Class<A> c);

    /**
     * Fetches all entities in the database for the given query.
     *
     * @param <A> the generic type
     * @param queryStr the query str
     * @param parameters the parameters
     * @return            A list of all fetched objects
     */
    public <A extends PersistentObject> List<A> list(String queryStr,
            Map<String, Object> parameters);

    /**
     * Fetches all entities in the database for the given query.
     *
     * @param <A> the generic type
     * @param queryStr the query str
     * @param parameters the parameters
     * @param startIndex the start index
     * @param amount the amount
     * @return            A list of all fetched objects
     */
    public <A extends PersistentObject> List<A> list(String queryStr,
            Map<String, Object> parameters, int startIndex, int amount);

    /**
     * Fetch a specific entity object from the database for the given query.
     *
     * @param <A> the generic type
     * @param queryStr the query str
     * @param parameters the parameters
     * @return            A list of all fetched objects
     */
    public <A extends PersistentObject> A find(String queryStr,
            Map<String, Object> parameters);

    /**
     * Store an entity to the database.
     * 
     * @param object An instance of the entity to be stored
     */
    public void store(PersistentObject object);

    /**
     * Store a set of entities within the same transaction.
     * 
     * @param <A>     The type of object
     * @param objects Set of entities which are to be stored as a batch within
     *                the same transaction
     */
    public <A extends PersistentObject> void storeAll(Collection<A> objects);

    /**
     * Remove an entity from the database.
     * 
     * @param object The entity to be removed
     */
    public void delete(PersistentObject object);

    /**
     * Remove a set of entities from the database within the same transaction.
     * 
     * @param <A>     The type of object
     * @param objects Set of entities which are to be removed as a batch within
     *                the same transaction
     */
    public <A extends PersistentObject> void deleteAll(Collection<A> objects);

    /**
     * Update all the fields in the entity to the most up-to-date version of
     * data found in the database. Any changes made to the entity object before
     * calling this method will be overwritten.
     * 
     * @param <A>    The type of object
     * @param object The entity object you wish to refresh
     */
    public <A extends PersistentObject> void refresh(A object);

    /**
     * Close the facade. Closes the connection.
     */
    public void close();

    /**
     * Kill the facade, closes the facade and removes all possible references.
     */
    public void kill();

    /**
     * Gives the total number of records for the given entity type.
     *
     * @param c the c
     * @return   Total number of records or -1 if an exception occurred
     */
    public Long count(Class<? extends PersistentObject> c);

    /**
     * Gives the total number of records for the given entity type which
     * matches the where clause.
     *
     * @param c the c
     * @param whereClause the where clause
     * @param parameters the parameters
     * @return             Total number of records or -1 if an exception
     * occurred
     */
    public Long count(Class<? extends PersistentObject> c, String whereClause,
            Map<String, Object> parameters);

    /**
     * Fetches the values for a specific field for all entities which match the
     * set criteria.
     *
     * @param c the c
     * @param field the field
     * @param whereClause the where clause
     * @param parameters the parameters
     * @return             A list of all fetched objects
     */
    public List<?> getFieldValues(Class<? extends PersistentObject> c,
            String field, String whereClause, Map<String, Object> parameters);
    
}