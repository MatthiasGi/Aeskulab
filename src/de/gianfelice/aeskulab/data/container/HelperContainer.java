package de.gianfelice.aeskulab.data.container;

import com.vaadin.data.util.BeanContainer;

import de.gianfelice.aeskulab.data.entities.Helper;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A simple container for all {@link Helper} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Helper
 */
public class HelperContainer extends BeanContainer<Long, Helper> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** This columns should be visible. */
	public static final Object[] NATURAL_COLS = new Object[] {"lastName",
		"firstName", "telNumber", "qualifications", "image"};
	
	/** The headers for the visible columns. */
	public static final String[] COL_HEADERS = new String[] {"Nachname",
		"Vorname", "Nummer", "Qualifikationen", "Bild"};

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public HelperContainer() {
		super(Helper.class);
		setBeanIdProperty("id");
		addAll(DBManager.getCommunicator().list(Helper.class));
	}

}