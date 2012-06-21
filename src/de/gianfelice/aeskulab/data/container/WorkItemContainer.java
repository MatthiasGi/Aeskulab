package de.gianfelice.aeskulab.data.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.util.BeanContainer;

import de.gianfelice.aeskulab.data.entities.Work;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A simple container for all {@link Work} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Work
 * @see     WorkContainer
 */
public class WorkItemContainer extends BeanContainer<Long, Work> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** This columns should be visible. */
	public static final Object[] NATURAL_COLS = new Object[] {"caption",
		"place"};

	/** The headers for the visible columns. */
	public static final String[] COL_HEADERS = new String[] {"Betreff", "Ort"};

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public WorkItemContainer() {
		super(Work.class);
		setBeanIdProperty("id");
		String query = "SELECT w FROM Work w WHERE w.ready = :ready";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ready", false);
		List<Work> works = DBManager.getCommunicator().list(query, params);
		addAll(works);
	}

}