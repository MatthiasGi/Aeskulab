package de.gianfelice.aeskulab.data.container;

import com.vaadin.data.util.BeanContainer;

import de.gianfelice.aeskulab.data.entities.Place;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A simple container for all {@link Place} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Place
 */
public class PlaceContainer extends BeanContainer<Long, Place> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** This columns should be visible. */
	public static final Object[] NATURAL_COLS = new Object[] {"name", "image"};

	/** The headers for the visible columns. */
	public static final String[] COL_HEADERS = new String[] {"Name", "Bild"};

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public PlaceContainer() {
		super(Place.class);
		setBeanIdProperty("id");
		addAll(DBManager.getCommunicator().list(Place.class));
	}

}