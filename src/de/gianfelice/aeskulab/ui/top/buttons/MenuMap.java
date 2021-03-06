package de.gianfelice.aeskulab.ui.top.buttons;

import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;

import de.gianfelice.aeskulab.ui.MainLayout;
import de.gianfelice.aeskulab.ui.tabs.TabMap;
import de.gianfelice.aeskulab.ui.tabs.Tab;
import de.gianfelice.aeskulab.ui.top.MenuComponent;

/**
 * A link to the strategic map provided by this application.
 * 
 * @author  Matthias Gianfelice
 * @version 1.1.0
 * @see     TabMap
 */
public class MenuMap extends MenuComponent {

	// ------------------------------ Attribute(s) -----------------------------
	/** The default serial version id. */
	private static final long serialVersionUID = 1L;
	
	/** The main layout. */
	private MainLayout layout;

	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * Creates the button and assigns the layout.
	 * 
	 * @param layout The main layout
	 */
	public MenuMap(MainLayout layout) {
		super();
		this.layout = layout;
	}

	// ------------------------------- Method(s) -------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component getContent() {
		Embedded emb = new Embedded();
		emb.setSource(new ClassResource("res/30/map.png",getApplication()));
		emb.setHeight("30px");
		emb.setWidth("30px");
		return emb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tab getTab() {
		return new TabMap(layout);
	}

}