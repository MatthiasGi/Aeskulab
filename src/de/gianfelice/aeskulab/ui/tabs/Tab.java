package de.gianfelice.aeskulab.ui.tabs;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;

/**
 * Simple abstract do define the tabs and handle them.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.0
 */
public abstract class Tab extends Panel {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the tab and sets the style.
	 */
	public Tab() {
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(getContentContainer());
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Contains the actual content of the tab.
	 * 
	 * @return The content of the tab
	 */
	public abstract ComponentContainer getContentContainer();
	
}