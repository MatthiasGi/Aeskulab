package de.gianfelice.aeskulab.system.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * This class is an abstract superclass for all Entity classes in the
 * application. This class defines variables and methods, which are common for
 * all Entity classes.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 1.2.0
 */
@MappedSuperclass
public abstract class PersistentObject implements Serializable {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version ID. */
	private static final long serialVersionUID = 1L;

	/** The automatic generated ID for the object. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    /**
     * The concurrency version is a number which is used for optimistic locking
     * in the database.
     */
    @Column(nullable = false)
    @Version
    protected Long consistencyVersion;

    // ------------------------------- Method(s) -------------------------------
    /**
     * Get the ID for this entity.
     * 
     * @return The primary key of this entity
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the concurrency version number for this entity.
     * 
     * @return Current consistency version
     * @see    PersistentObject#consistencyVersion
     */
    public Long getConsistencyVersion() {
        return consistencyVersion;
    }
	
	/**
	 * Stores the entity and saves all changes.
	 */
	protected void store() {
		DBManager.getCommunicator().store(this);
	}

}