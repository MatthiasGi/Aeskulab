package de.gianfelice.aeskulab.data.container;

import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.gianfelice.aeskulab.data.entities.Log;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * This container collects all {@link Log} objects and allows to display them
 * inside a tree.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Log
 */
public class LogContainer extends HierarchicalContainer {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	/** The original order of the columns. */
	public static final Object[] NATURAL_COLS = new Object[] {"date", "text"};

	/** The headers of the columns. */
	public static final String[] COL_HEADERS = new String[] {"Zeit", "Inhalt"};
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Initialises the container and loads all logs to it.
	 */
	public LogContainer() {
		addContainerProperty("date", Date.class, null);
		addContainerProperty("text", String.class, null);
		List<Log> logs = DBManager.getCommunicator().list(Log.class);
		for (Log log : logs) addLog(log);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Adds a log to the container.
	 * 
	 * @param log The new log
	 */
	public void addLog(Log log) {
		Item item = addItem(log.getId());
		if (item == null) return;
		item.getItemProperty("date").setValue(log.getDate());
		item.getItemProperty("text").setValue(log.getText());
		setChildrenAllowed(log.getId(), false);
		
		Log parent = log.getParent();
		if (parent == null) return;
		
		if (parent.getParent() != null) {
			removeItem(log.getId());
			return;
		}
		setChildrenAllowed(parent.getId(), true);
		setParent(log.getId(), parent.getId());
	}

}