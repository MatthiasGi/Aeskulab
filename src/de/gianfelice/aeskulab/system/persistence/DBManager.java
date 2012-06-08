package de.gianfelice.aeskulab.system.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class to create and manage {@code DBCommunicator} classes. 
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 1.1.2
 */
public class DBManager {

	// ------------------------------ Attribute(s) -----------------------------
	/** A {@link HashMap} with all {@link DBCommunicator} classes. */
	private static Map<String, DBCommunicator> communicators =
			new HashMap<String, DBCommunicator>();

	/** The default {@link DBCommunicator} for the application. */
    private static DBCommunicator defaultCommunicator;

    // ------------------------------- Method(s) -------------------------------
    /**
     * Register a new {@link JPACommunicator} to the application.
     *
     * @param name the name
     * @param isDefault the is default
     * @throws InstantiationException Should never be thrown by this method
     * @throws IllegalAccessException If a communicator with the given name
     * already exists
     */
    public static void registerCommunicator(String name, boolean isDefault)
            throws InstantiationException, IllegalAccessException {
    	registerCommunicator(JPACommunicator.class, name, isDefault);
    }

    /**
     * Register a new {@link DBCommunicator} to the application.
     *
     * @param com the com
     * @param name the name
     * @param isDefault the is default
     * @throws InstantiationException If the class of the communicator
     * implementation is abstract or an interface
     * @throws IllegalAccessException If a communicator with the given name
     * already exists
     */
    public static void registerCommunicator(Class<? extends DBCommunicator> com,
    		String name, boolean isDefault) throws InstantiationException,
    		IllegalAccessException {
    	System.out.println(com);

        if (communicators.containsKey(name))
        	throw new IllegalArgumentException();

        DBCommunicator comImpl = com.newInstance();
        comImpl.init(name);
        communicators.put(name, comImpl);

        if (isDefault) defaultCommunicator = comImpl;
    }

    /**
     * Returns the default communicator of the application. 
     * 
     * @return The default communicator instance
     */
    public static DBCommunicator getCommunicator() {
        return defaultCommunicator;
    }

    /**
     * Get the communicator for a specific configuration.
     *
     * @param name the name
     * @return      The communicator instance for the given name
     */
    public static DBCommunicator getCommunicator(String name) {
        return communicators.get(name);
    }

    /**
     * Define a new default communicator for the manager.
     * 
     * @param name Name of the communicator
     */
    public static void setDefaultCommunicator(String name) {
        if (name == null || name.isEmpty())
        	throw new IllegalArgumentException();

        DBCommunicator com = getCommunicator(name);
        if (com == null) throw new IllegalArgumentException("Not found");

        defaultCommunicator = com;
    }

    /**
     * Removes a communicator from the manager.
     * 
     * @param name Persistence-unit name (defined in the persistence.xml)
     */
    public static void removeCommunicator(String name) {
        if (communicators.containsKey(name)) {
            DBCommunicator com = communicators.get(name);

            // If the communicator is the default one, remove the default value
            if (com == defaultCommunicator) defaultCommunicator = null;

            com.kill();
            communicators.remove(name);
        }
    }

    /**
     * Removes all communicators references from the manager.
     */
    public static void clear() {
        Set<String> names = new HashSet<String>(communicators.keySet());
        for (String name : names) {
            removeCommunicator(name);
        }
    }

}