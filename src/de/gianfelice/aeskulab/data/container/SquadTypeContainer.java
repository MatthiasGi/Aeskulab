package de.gianfelice.aeskulab.data.container;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.DefaultItemSorter;

import de.gianfelice.aeskulab.data.entities.SquadType;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A simple container for all {@link SquadType} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     SquadType
 */
public class SquadTypeContainer extends BeanContainer<Long, SquadType> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public SquadTypeContainer() {
		super(SquadType.class);
		setBeanIdProperty("id");
		addAll(DBManager.getCommunicator().list(SquadType.class));
		setItemSorter(new DefaultItemSorter());
	}

}
