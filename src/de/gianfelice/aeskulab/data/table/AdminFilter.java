package de.gianfelice.aeskulab.data.table;

import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;

import de.gianfelice.aeskulab.data.container.UserContainer;

/**
 * A simple filter, that can be added to the {@link UserContainer}. It will
 * only display administrators.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 */
public class AdminFilter implements Filter {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	public boolean passesFilter(Object itemId, Item item)
			throws UnsupportedOperationException {
		Object property = item.getItemProperty("admin").getValue();
		if (property == null) return false;
		return (Boolean) property;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean appliesToProperty(Object propertyId) {
		return "admin".equals(propertyId);
	}

}