package de.gianfelice.aeskulab.ui.top.buttons;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;

import de.gianfelice.aeskulab.ui.tabs.TabLog;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.top.MenuComponent;

/**
 * A link to the log section of the application.
 * 
 * @author  Matthias Gianfelice
 * @version 1.0.1
 * @see     TabLog
 */
public class MenuLog extends MenuComponent {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component getContent() {
		Embedded emb = new Embedded();
		emb.setSource(new ClassResource("res/30/log.png", getApplication()));
		emb.setHeight("30px");
		emb.setWidth("30px");
		return emb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tab getTab() {
		return new TabLog();
	}

}