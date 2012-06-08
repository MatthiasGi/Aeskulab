package de.gianfelice.aeskulab.data.container;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.DefaultItemSorter;

import de.gianfelice.aeskulab.data.entities.Qualification;
import de.gianfelice.aeskulab.system.persistence.DBManager;

/**
 * A simple container for all {@link Qualification} objects.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 * @see     Qualification
 */
public class QualificationContainer extends BeanContainer<Long, Qualification> {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Instantiates a new container.
	 */
	public QualificationContainer() {
		super(Qualification.class);
		setBeanIdProperty("name");
		addAll(DBManager.getCommunicator().list(Qualification.class));
		setItemSorter(new DefaultItemSorter());
		sort(new Object[] {"name"}, new boolean[] {true});
	}

}